package com.fanstech.mothersong.ui;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommentListAdapter;
import com.fanstech.mothersong.adapter.CommunityAdapter;
import com.fanstech.mothersong.adapter.CommunityCommentAdapter;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.beans.Comment;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedCommentResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/15 0015 下午 2:01
 *功能模块：用户评论列表
 */
public class CommentListActivity extends BaseActivity {

    private List<FeedItem> lists = new ArrayList<>();
    private CommentListAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private ProgressBar load;
    private TextView no;//提示

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    @Override
    protected void initViews() {

        setTitle("评论列表");
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

        getComment();
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
     * 获取评论列表
     */
    public void getComment(){

        mCommSDK.fetchReceivedComments(0, new Listeners.SimpleFetchListener<FeedCommentResponse>() {
            @Override
            public void onComplete(FeedCommentResponse feedCommentResponse) {

                Log.e("feedCommentResponse","feedCommentResponse"+feedCommentResponse.result);
                lists = feedCommentResponse.result;
                handler.sendEmptyMessage(1);
            }
        });


    }

}
