package com.fanstech.mothersong.main_fragment;

import android.app.Activity;
import android.os.Build;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.DanceTeam.EstablishDanceTeamActivity;
import com.fanstech.mothersong.DanceTeam.FollowDanceTeamActivity;
import com.fanstech.mothersong.DanceTeam.JoinAddreNameActivity;
import com.fanstech.mothersong.DanceTeam.JoinDanceListActivity;
import com.fanstech.mothersong.DanceTeam.MyDanceTeamActivity;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.ui.CollectionActivity;
import com.fanstech.mothersong.ui.FeedbackActivity;
import com.fanstech.mothersong.ui.LoginActivity;
import com.fanstech.mothersong.ui.MessageActivity;
import com.fanstech.mothersong.ui.ModifyUserActivity;
import com.fanstech.mothersong.ui.SetUpActivity;
import com.fanstech.mothersong.ui.ViewingHistoryActivity;
import com.fanstech.mothersong.utils.AllUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.fanstech.mothersong.video.MyVideoActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 作者：胖胖祥
 * 时间：2016/6/12 0012 上午 11:50
 * 功能模块：我的
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private Button my_login;
    private RelativeLayout noLogin, yesLogin;
    private ImageViewUtil my_head;
    private TextView my_name, my_dance, my_follow, my_record, my_video, my_community, my_setup, my_modify, my_feedback, my_message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_my, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * 初始化
     */

    @Override
    protected void initView() {

        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if(!isNetworkAvailable()){
            Toast.makeText(mActivity,"连接网络失败，请检查！",Toast.LENGTH_LONG).show();
        }

        noLogin = (RelativeLayout) findViewById(R.id.my_noLogin);
        yesLogin = (RelativeLayout) findViewById(R.id.my_yesLogin);
        my_login = (Button) findViewById(R.id.my_login);
        my_login.setOnClickListener(this);
        my_head = (ImageViewUtil) findViewById(R.id.my_head);
        my_name = (TextView) findViewById(R.id.my_name);

        my_dance = (TextView) findViewById(R.id.my_dance);//我的舞队
        my_dance.setOnClickListener(this);
        my_follow = (TextView) findViewById(R.id.my_follow);//关注的舞队
        my_follow.setOnClickListener(this);
        my_record = (TextView) findViewById(R.id.my_record);//观看历史
        my_record.setOnClickListener(this);
        my_video = (TextView) findViewById(R.id.my_video);//我的视频
        my_video.setOnClickListener(this);
        my_community = (TextView) findViewById(R.id.my_community);//我的收藏
        my_community.setOnClickListener(this);
        my_setup = (TextView) findViewById(R.id.my_setup);//我的设置
        my_setup.setOnClickListener(this);
        my_modify = (TextView) findViewById(R.id.my_modify);//编辑
        my_modify.setOnClickListener(this);
        my_feedback = (TextView) findViewById(R.id.my_feedback);//意见反馈
        my_feedback.setOnClickListener(this);
        my_message = (TextView) findViewById(R.id.my_message);//我的信息
        my_message.setOnClickListener(this);
        getUserMessage();

    }


    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.my_login://登录
                startActivity(LoginActivity.class, bundle, 10);
                break;
            case R.id.my_dance://我的舞队

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(MyDanceTeamActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_follow://关注的舞队

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(FollowDanceTeamActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_record://发表的说说

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(ViewingHistoryActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_video://我的视频

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(MyVideoActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_community://我的收藏

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(CollectionActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_setup://我的设置

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(SetUpActivity.class, bundle, 0);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_modify://编辑按钮

                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(ModifyUserActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.my_feedback://意见反馈


                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(FeedbackActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.my_message://我的信息


                if (MyApplication.getInstance().isLogin()) {//已登录
                    startActivity(MessageActivity.class);
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {

            case 10:
                Toast.makeText(this.getActivity(), "成功登录", Toast.LENGTH_LONG).show();
                getUserMessage();
                break;

            case 0:
                getUserMessage();
                break;

        }

    }

    //获取用户资料
    public void getUserMessage() {

        //判断是否登录了
        String token = SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, "");

        if (AllUtil.isNotNull(token)) {//已经登录

            my_name.setText(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname, ""));
            ImageLoader.getInstance().displayImage(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.head, ""), my_head,
                    ImageLoadOptions.getOptions());
            MyApplication.getInstance().setLogin(true);
            yesLogin.setVisibility(View.VISIBLE);
            noLogin.setVisibility(View.GONE);

        } else {

            MyApplication.getInstance().setLogin(false);
            yesLogin.setVisibility(View.GONE);
            noLogin.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onStart() {
        super.onStart();

        if(AllUtil.isNotNull(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""))){
            ImageLoader.getInstance().displayImage(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.head, ""), my_head,
                    ImageLoadOptions.getOptions());
        }

        my_name.setText(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname,""));

    }
}
