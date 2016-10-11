package com.fanstech.mothersong.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommunityAdapter;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/27 0027 上午 11:32
 *功能模块：发表的说说
 */
public class ViewingHistoryActivity extends BaseActivity {

    private List<FeedItem> lists;
    private CommunityAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private ProgressBar load;
    private TextView no;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_viewing_history;
    }

    @Override
    protected void initViews() {

        setTitle("我的说说");
        listView = (ListView) findViewById(R.id.feeds_list);
        no = (TextView) findViewById(R.id.no);
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

        getMyFeeds();

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    if(lists.size()<=0){

                        no.setVisibility(View.VISIBLE);

                    }

                    load.setVisibility(View.GONE);
                    adapter = new CommunityAdapter(mActivity,lists);
                    listView.setAdapter(adapter);

                    break;

            }

        }
    };

    public void getMyFeeds(){

        mCommSDK.fetchUserTimeLine(CommConfig.getConfig().loginedUser.id, new Listeners.FetchListener<FeedsResponse>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedsResponse feedsResponse) {

                lists = new ArrayList<FeedItem>();
                for (FeedItem item : feedsResponse.result) {
                    lists.add(item);
                }

                handler.sendEmptyMessage(1);

            }
        });

    }

}
