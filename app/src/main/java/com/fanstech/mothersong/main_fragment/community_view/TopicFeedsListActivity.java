package com.fanstech.mothersong.main_fragment.community_view;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommunityAdapter;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/12 0012 上午 9:46
 *功能模块：话题下的帖子
 */
public class TopicFeedsListActivity extends BaseActivity {

    private List<FeedItem> lists;
    private CommunityAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private ProgressBar load;
    private String topicID;
    private TextView no;
    private String nextPageUrl;//下一页的url

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    if(lists.size()<=0){
                        no.setVisibility(View.VISIBLE);
                    }
                    if (nextPageUrl != null && !nextPageUrl.equals("")) {
                        refresh.setLoadMore(true);
                    }

                    load.setVisibility(View.GONE);
                    adapter.onDateChange(lists);
                    refresh.finishRefreshLoadMore();//结束上拉
                    refresh.finishRefresh();//结束下拉
                    break;

            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topic_feeds_list;
    }

    @Override
    protected void initViews() {

        setTitle("列表");
        topicID = this.getIntent().getStringExtra("topicID");
        no = (TextView) findViewById(R.id.no);
        listView = (ListView) findViewById(R.id.list);
        load = (ProgressBar) findViewById(R.id.load);
        load.setVisibility(View.VISIBLE);
        refresh = (MaterialRefreshLayout) findViewById(R.id.refresh);
        refresh.setIsOverLay(true);//修改下拉刷新样式
        refresh.setWaveShow(true);//修改下拉刷新样式
        refresh.setWaveColor(0x00000000);//修改下拉刷新样式
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                //下拉刷新
                refresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        // 更新数据
                        lists.clear();
                        getFeedItems();

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

        lists = new ArrayList<>();
        adapter = new CommunityAdapter(mActivity,lists);
        listView.setAdapter(adapter);

        getFeedItems();

    }

    /**
     * 获取数据
     */
    public void getFeedItems(){

        mCommSDK.fetchTopicFeed(topicID, new Listeners.FetchListener<FeedsResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {

                //查询是否有下一页
                for (FeedItem item : feedsResponse.result) {
                    lists.add(item);
                    nextPageUrl = item.nextPageUrl;
                }

                handler.sendEmptyMessage(1);
            }
        });

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
