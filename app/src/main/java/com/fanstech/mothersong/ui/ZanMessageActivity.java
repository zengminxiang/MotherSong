package com.fanstech.mothersong.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommentListAdapter;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Like;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.LikeMeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/18 0018 下午 12:38
 *功能模块：赞的信息列表
 */
public class ZanMessageActivity extends BaseActivity {

    private List<FeedItem> lists = new ArrayList<>();
    private CommentListAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private ProgressBar load;
    private TextView no;//提示

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zan_message;
    }

    @Override
    protected void initViews() {

        setTitle("点赞列表");
        listView = (ListView) findViewById(R.id.listview);
        load = (ProgressBar) findViewById(R.id.load);
        no = (TextView) findViewById(R.id.no);
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

        getUserZan();
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
                    adapter = new CommentListAdapter(mActivity,lists);
                    listView.setAdapter(adapter);

                    break;

            }

        }
    };

    /**
     * 获取赞的列表
     */
    public void getUserZan(){

        mCommSDK.fetchLikedRecords(CommConfig.getConfig().loginedUser.id, new Listeners.FetchListener<LikeMeResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(LikeMeResponse likeMeResponse) {

                lists = likeMeResponse.result;
                handler.sendEmptyMessage(1);

            }
        });

    }

}
