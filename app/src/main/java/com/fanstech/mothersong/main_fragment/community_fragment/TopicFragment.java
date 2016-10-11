package com.fanstech.mothersong.main_fragment.community_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommunityAdapter;
import com.fanstech.mothersong.adapter.TopicAdapter;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.umeng.comm.core.beans.Category;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.Response;
import com.umeng.comm.core.nets.responses.CategoryResponse;
import com.umeng.comm.core.nets.responses.TopicResponse;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/8 0008 下午 2:34
 *功能模块：话题
 */
public class TopicFragment extends BaseFragment implements TopicAdapter.addFollow{


    private ListView listView;
    private List<Topic>lists;
    private TopicAdapter adapter;
    private ProgressBar load;
    private MaterialRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.community_topic, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    protected void initView() {

        listView = (ListView) findViewById(R.id.community_topic_list);
        load = (ProgressBar) findViewById(R.id.load);
        load.setVisibility(View.VISIBLE);
        refresh = (MaterialRefreshLayout) findViewById(R.id.refresh);
        refresh.setIsOverLay(true);//修改下拉刷新样式
        refresh.setWaveShow(true);//修改下拉刷新样式
        refresh.setWaveColor(0x00000000);//修改下拉刷新样式
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                //下拉刷新
                refresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // 更新数据
                        refresh.finishRefresh();//结束下拉拉

                    }
                }, 500);


            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                //上拉刷新
                refresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // 更新数据
                        refresh.finishRefreshLoadMore();//结束上拉

                    }
                }, 500);


            }

        });

        getTopic();

    }


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    load.setVisibility(View.GONE);
                    adapter = new TopicAdapter(mActivity,lists);
                    adapter.setAddFollow(TopicFragment.this);
                    listView.setAdapter(adapter);

                    break;

            }

        }
    };



    /**
     * 获取所有话题
     */
    public void getTopic(){

        mCommSDK.fetchTopics(new Listeners.FetchListener<TopicResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(TopicResponse topicResponse) {
                lists = topicResponse.result;
                handler.sendEmptyMessage(1);
            }
        });

    }

    @Override
    public void addFollow(Topic topic) {

        SubmitAddFollow(topic);

    }

    /**
     * 添加关注
     */
    public void SubmitAddFollow(Topic topic){

        mCommSDK.followTopic(topic, new Listeners.SimpleFetchListener<Response>() {
            @Override
            public void onComplete(Response response) {

                Toast.makeText(mActivity,"关注成功",Toast.LENGTH_LONG).show();

            }
        });

    }

}
