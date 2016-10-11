package com.fanstech.mothersong.main_fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommunityAdapter;
import com.fanstech.mothersong.bean.UserMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.main_fragment.community_fragment.ActivityFragment;
import com.fanstech.mothersong.main_fragment.community_fragment.FollowFragment;
import com.fanstech.mothersong.main_fragment.community_fragment.HotFragment;
import com.fanstech.mothersong.main_fragment.community_fragment.TopicFragment;
import com.fanstech.mothersong.main_fragment.community_publish.ui.PublishActivity;
import com.fanstech.mothersong.main_fragment.community_view.CommunityDetailsActivity;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.nets.responses.FeedsResponse;
import com.umeng.comm.core.nets.responses.TopicResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/6/12 0012 上午 11:49
 * 功能模块：社区
 */
public class CommunityFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout hot, follow, activity, topic;

    private FollowFragment followFragment;
    private HotFragment hotFragment;
    private ActivityFragment activityFragment;
    private TopicFragment topicFragment;

    //保留当前的显示的fragment的标签
    private final static String FOLLOW = "FOLLOW";
    private final static String HOT = "HOT";
    private final static String TOPIC = "TOPIC";
    private final static String ACTIVITY = "ACTIVITY";


    private TextView communtiy_follow, communtiy_hot, communtiy_topic, communtiy_activit;
    private ImageView my_message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_community, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }

        if(!isNetworkAvailable()){
            Toast.makeText(mActivity,"连接网络失败，请检查！",Toast.LENGTH_LONG).show();
        }

        hot = (RelativeLayout) findViewById(R.id.hot);
        hot.setOnClickListener(this);
        follow = (RelativeLayout) findViewById(R.id.follow);
        follow.setOnClickListener(this);
        activity = (RelativeLayout) findViewById(R.id.activity);
        activity.setOnClickListener(this);
        topic = (RelativeLayout) findViewById(R.id.topic);
        topic.setOnClickListener(this);
        communtiy_follow = (TextView) findViewById(R.id.communtiy_follow);
        communtiy_hot = (TextView) findViewById(R.id.communtiy_hot);
        communtiy_topic = (TextView) findViewById(R.id.communtiy_topic);
        communtiy_activit = (TextView) findViewById(R.id.communtiy_activit);
        my_message = (ImageView) findViewById(R.id.my_message);
        my_message.setOnClickListener(this);

        followFragment = new FollowFragment();
        activityFragment = new ActivityFragment();
        hotFragment = new HotFragment();
        topicFragment = new TopicFragment();
        initData();


    }


    private void setTabClassification(int mClassificationFlag) {

        // 关注
        communtiy_follow.setBackgroundResource(mClassificationFlag == 0 ? R.drawable.switch_textviet_left: R.drawable.switch_textview_left_checked);
        communtiy_follow.setTextColor(getResources().getColor(mClassificationFlag == 0 ? R.color.bg_white : R.color.white));
        //热门
        communtiy_hot.setBackgroundResource(mClassificationFlag == 1 ? R.drawable.switch_textview_in: R.drawable.switch_textview_in_checked);
        communtiy_hot.setTextColor(getResources().getColor(mClassificationFlag == 1 ? R.color.bg_white : R.color.white));
        //话题
        communtiy_topic.setBackgroundResource(mClassificationFlag == 2 ? R.drawable.switch_textview_in: R.drawable.switch_textview_in_checked);
        communtiy_topic.setTextColor(getResources().getColor(mClassificationFlag == 2 ? R.color.bg_white : R.color.white));
        //活动
        communtiy_activit.setBackgroundResource(mClassificationFlag == 3 ? R.drawable.switch_textview_right: R.drawable.switch_textview_right_checked);
        communtiy_activit.setTextColor(getResources().getColor(mClassificationFlag == 3 ? R.color.bg_white : R.color.white));

    }


    protected void initData() {
        change(FOLLOW);
        setTabClassification(0);
    }


    //切换fragment
    private void change(String nowTag) {

        if (nowTag.equals(FOLLOW)) {
            android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.lssy_content, followFragment, "FOLLOW").commit();

        } else if (nowTag.equals(HOT)) {
            android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.lssy_content, hotFragment, "HOT").commit();

        } else if (nowTag.equals(TOPIC)) {
            android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.lssy_content, topicFragment, "TOPIC").commit();


        } else if (nowTag.equals(ACTIVITY)) {

            android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.lssy_content, activityFragment, "ACTIVITY").commit();

        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.follow:
            case R.id.hot:
            case R.id.topic:
            case R.id.activity:
                switchTabFragment(v.getId());
                break;

            case R.id.my_message:
                if (!MyApplication.getInstance().isLogin()) {
                    Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(PublishActivity.class);

                break;

            default:
                break;
        }

    }


    private void switchTabFragment(int checkedId) {

        switch (checkedId) {
            case R.id.follow:
                change(FOLLOW);
                setTabClassification(0);
                break;
            case R.id.hot:
                change(HOT);
                setTabClassification(1);
                break;
            case R.id.topic:
                change(TOPIC);
                setTabClassification(2);
                break;
            case R.id.activity:
                change(ACTIVITY);
                setTabClassification(3);
                break;

        }
    }


}
