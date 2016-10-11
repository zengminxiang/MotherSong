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
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/8 0008 下午 2:33
 *功能模块：热门社区
 */
public class HotFragment extends BaseFragment{

    private List<FeedItem> lists;
    private CommunityAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private ProgressBar load;

    private String nextPageUrl;//下一页的url

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    load.setVisibility(View.GONE);

                    if (nextPageUrl != null && !nextPageUrl.equals("")) {
                        refresh.setLoadMore(true);
                    }

                    adapter.onDateChange(lists);
                    refresh.finishRefreshLoadMore();//结束上拉
                    refresh.finishRefresh();//结束下拉

                    break;

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.community_hot, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

        listView = (ListView) findViewById(R.id.community_list);
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
                        lists.clear();
                        getHotMessage();

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
                        if (nextPageUrl != null && !nextPageUrl.equals("")) {

                            getNextPage();

                        } else {

                            Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_LONG).show();
                            refresh.finishRefreshLoadMore();//结束上拉

                        }

                    }
                }, 500);


            }

        });

        lists = new ArrayList<FeedItem>();
        adapter = new CommunityAdapter(mActivity, lists);
        listView.setAdapter(adapter);

        getHotMessage();

    }

    /**
     * 获取热门数据
     */
    public void getHotMessage(){

        mCommSDK.fetchHotestFeeds(new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {

                lists = new ArrayList<FeedItem>();
                for (FeedItem item : feedsResponse.result) {
                    lists.add(item);
                    nextPageUrl = item.nextPageUrl;
                }

                handler.sendEmptyMessage(1);
            }
        },30,0);

    }

    /**
     * 获取下一页的数据
     */
    public void getNextPage() {

        mCommSDK.fetchNextPageData(nextPageUrl, FeedsResponse.class, new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {

                for (FeedItem item : feedsResponse.result) {
                    lists.add(item);
                    nextPageUrl = item.nextPageUrl;
                }

                handler.sendEmptyMessage(1);
            }
        });


    }


}
