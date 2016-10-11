package com.fanstech.mothersong.video;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.MyVideoAdapter;
import com.fanstech.mothersong.adapter.VideoListAdapter;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
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
 * 时间：2016/6/27 0027 下午 2:03
 * 功能模块：我的视频
 */
public class MyVideoActivity extends BaseActivity {

    private ListView listView;
    private MaterialRefreshLayout refresh;
    private List<VideoMessage> lists = new ArrayList<>();
    private VideoListAdapter adapter;
    private TextView no_video;
    private ProgressBar load;

    private int pagecount = 1;//总页数
    private int page = 1;//当前页数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_video;
    }

    @Override
    protected void initViews() {

        setTitle("我的视频");
        listView = (ListView) findViewById(R.id.my_video_list);
        no_video = (TextView) findViewById(R.id.no_video);
        load = (ProgressBar) findViewById(R.id.load);

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
                        // 更新数据
                        page = 1;
                        lists.clear();
                        getMyVideo();

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

                            getMyVideo();

                        }

                    }
                }, 500);


            }

        });

        getMyVideo();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    page++;
                    load.setVisibility(View.GONE);
                    //如果页数大于1就设置可以上拉加载
                    if (pagecount > 1) {
                        refresh.setLoadMore(true);
                    }
                    if (lists.size() <= 0) {
                        no_video.setVisibility(View.VISIBLE);
                        break;
                    }

                    if (adapter == null) {

                        adapter = new VideoListAdapter(mActivity, lists);
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
     * 查询我的视频
     */
    public void getMyVideo() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("page", page + "");

        RewriteRequest myVideo = new RewriteRequest(UrlConfig.GETMYVIDEO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    JSONObject object = new JSONObject(jsonObject.getString("obj"));
                    JSONArray array = new JSONArray(object.getString("items"));

                    Gson gson = new Gson();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject message = (JSONObject) array.get(i);
                        VideoMessage video = gson.fromJson(message.toString(), VideoMessage.class);
                        lists.add(video);

                    }

                    //获取到总页数
                    JSONObject p = new JSONObject(object.getString("paginator"));
                    String count = p.getString("totalPages");
                    pagecount = Integer.parseInt(count);
                    Log.e("pagecount", "pagecountn " + pagecount);

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        myVideo.setTag("myVideo");
        MyApplication.getHttpQueues().add(myVideo);
        myVideo.setShouldCache(true);

    }

}
