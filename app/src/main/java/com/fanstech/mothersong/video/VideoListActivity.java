package com.fanstech.mothersong.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.VideoListAdapter;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.ui.FeedbackActivity;
import com.fanstech.mothersong.utils.UrlConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：胖胖祥
 * 时间：2016/6/17 0017 上午 11:10
 * 功能模块：视频列表
 */
public class VideoListActivity extends BaseActivity {

    private ListView listView;
    private MaterialRefreshLayout refresh;
    private List<VideoMessage> lists = new ArrayList<>();
    ;
    private VideoListAdapter adapter;
    private final int LOADING = 1;
    private Button function_button;//头部右边按钮
    private ProgressBar load;
    private int pagecount;//总页数
    private int page = 1;//当前页数


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initViews() {

        setTitle("视频列表");
        load = (ProgressBar) findViewById(R.id.load);
        function_button = (Button) findViewById(R.id.function_button);
        function_button.setVisibility(View.VISIBLE);
        function_button.setText("上传");
        function_button.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.video_list);
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

                        page = 1;
                        lists.clear();
                        getVideoListMessage("", "", page);

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
                        if (page > pagecount) {

                            Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_LONG).show();
                            refresh.finishRefreshLoadMore();//结束上拉

                        } else {

                            getVideoListMessage("", "", page);

                        }

                    }
                }, 500);


            }

        });

        getVideoListMessage("", "", page);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOADING:

                    load.setVisibility(View.GONE);
                    page++;

                    if (adapter == null) {

                        adapter = new VideoListAdapter(VideoListActivity.this, lists);
                        listView.setAdapter(adapter);

                    } else {

                        adapter.onDateChange(lists);
                        refresh.finishRefreshLoadMore();//结束上拉
                        refresh.finishRefresh();//结束下拉

                    }

                    break;
            }

        }
    };


    /**
     * 获取视频列表
     *
     * @param title 标题，用作搜索
     * @param sort  排序，1最新，2最热
     * @param p     当前页
     */

    public void getVideoListMessage(String title, String sort, int p) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("title", "");
        mMap.put("sort", "1");
        mMap.put("page", p + "");

        RewriteRequest video = new RewriteRequest(UrlConfig.VIDEOLIST, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "视频数据：" + jsonObject);
                try {
                    if (jsonObject.getString("state").equals("200")) {

                        Gson gson;
                        JSONObject obj = new JSONObject(jsonObject.getString("obj"));
                        JSONArray videos = new JSONArray(obj.getString("items"));
                        for (int i = 0; i < videos.length(); i++) {

                            gson = new Gson();
                            JSONObject j = videos.getJSONObject(i);
                            VideoMessage video = gson.fromJson(j.toString(), VideoMessage.class);
                            lists.add(video);

                        }

                        //获取到总页数
                        JSONObject p = new JSONObject(obj.getString("paginator"));
                        String count = p.getString("totalPages");
                        pagecount = Integer.parseInt(count);
                        Log.e("pagecount","pagecountn "+pagecount);

                        handler.sendEmptyMessage(LOADING);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    load.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        video.setTag("video");
        MyApplication.getHttpQueues().add(video);
        video.setShouldCache(true);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.function_button:

                if (MyApplication.getInstance().isLogin()) {//已登录
                    Intent intent = new Intent(this, UploadVideoActivity.class);
                    this.startActivity(intent);
                } else {

                    Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();

                }
                break;

        }

    }
}
