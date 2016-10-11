package com.fanstech.mothersong.main_fragment;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.VideoListAdapter;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
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
 *作者：胖胖祥
 *时间：2016/6/12 0012 上午 11:47
 *功能模块：达人
 */
public class MasterFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener{

    private ListView listView;
    private RefreshLayout swipeLayout;
    private List<VideoMessage> lists;
    private VideoListAdapter adapter;
    private final int LOADING = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_master, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){

                case LOADING:

                    adapter = new VideoListAdapter(mActivity,lists);
                    listView.setAdapter(adapter);
                    break;

            }

        }
    };

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


        swipeLayout = (RefreshLayout) findViewById(R.id.video_list_refresh);
        swipeLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        listView = (ListView) findViewById(R.id.master_list);
        setListener();
        getVideoListMessage("","","");
    }

    /**
     * 获取视频列表
     * @param title 标题，用作搜索
     * @param sort 排序，1最新，2最热
     * @param page 当前页
     */

    public void getVideoListMessage(String title,String sort,String page){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("title","");
        mMap.put("sort","1");
        mMap.put("page","1");

        RewriteRequest video = new RewriteRequest(UrlConfig.VIDEOLIST, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject","视频数据："+jsonObject);
                try {
                    if(jsonObject.getString("state").equals("200")){

                        Gson gson;
                        lists = new ArrayList<>();
                        JSONObject obj = new JSONObject(jsonObject.getString("obj"));
                        JSONArray videos = new JSONArray(obj.getString("items"));
                        for(int i = 0;i<videos.length();i++){

                            gson = new Gson();
                            JSONObject j = videos.getJSONObject(i);
                            VideoMessage video = gson.fromJson(j.toString(),VideoMessage.class);
                            lists.add(video);

                        }

                        //获取到总页数
                        JSONObject p = new JSONObject(obj.getString("paginator"));
                        String pagecount = p.getString("totalCount");

                        handler.sendEmptyMessage(LOADING);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },mMap);

        video.setTag("video");
        MyApplication.getHttpQueues().add(video);
        video.setShouldCache(true);

    }

    //设置监听
    private void setListener() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);

    }


    @Override
    public void onLoad() {

        swipeLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                // 更新完后调用该方法结束刷新
                swipeLayout.setLoading(false);
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        swipeLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                // 更新完后调用该方法结束刷新
                swipeLayout.setRefreshing(false);
            }
        }, 500);
    }
}
