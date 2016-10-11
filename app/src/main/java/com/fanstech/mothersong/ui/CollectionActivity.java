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
 *作者：胖胖祥
 *时间：2016/6/27 0027 下午 12:05
 *功能模块：收藏视频发
 */
public class CollectionActivity extends BaseActivity {

    private ListView listView;
    private List<VideoMessage> lists = new ArrayList<>();
    private VideoListAdapter adapter;
    private MaterialRefreshLayout refresh;

    private ProgressBar load;
    private TextView no;
    private int pagecount=1;//总页数
    private int page = 1;//当前页数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initViews() {

        setTitle("我的收藏");
        listView = (ListView) findViewById(R.id.collection_list);
        load = (ProgressBar) findViewById(R.id.load);
        no = (TextView) findViewById(R.id.no);
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
                        page = 1;
                        lists.clear();
                        getMyCollection();

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

                            getMyCollection();

                        }

                    }
                }, 500);


            }

        });
        getMyCollection();


    }


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    page++;
                    load.setVisibility(View.GONE);

                    //如果页数大于1就设置可以上拉加载
                    if(pagecount>1){
                        refresh.setLoadMore(true);
                    }
                    if (lists.size() <= 0) {
                        no.setVisibility(View.VISIBLE);
                        break;
                    }

                    if(adapter == null ){

                        adapter = new VideoListAdapter(mActivity,lists);
                        listView.setAdapter(adapter);

                    }else{

                        adapter.onDateChange(lists);
                        refresh.finishRefreshLoadMore();//结束上拉
                        refresh.finishRefresh();//结束下拉

                    }
                    break;

            }

        }
    };


    /**
     * 获取收藏的视频
     */
    public void getMyCollection(){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token,""));
        mMap.put("page", page+"");

        RewriteRequest myCollection = new RewriteRequest(UrlConfig.GETCOLLECTION, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject","jsonObject"+jsonObject);

                try {

                    JSONObject object = new JSONObject(jsonObject.getString("obj"));
                    JSONArray array = new JSONArray(object.getString("items"));

                    Gson gson = new Gson();

                    for(int i=0; i<array.length();i++){

                        JSONObject message = (JSONObject) array.get(i);
                        VideoMessage video = gson.fromJson(message.toString(),VideoMessage.class);
                        lists.add(video);

                    }

                    //获取到总页数
                    JSONObject p = new JSONObject(object.getString("paginator"));
                    String count = p.getString("totalPages");
                    pagecount = Integer.parseInt(count);
                    Log.e("pagecount","pagecountn "+pagecount);

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        },mMap);

        myCollection.setTag("myCollection");
        MyApplication.getHttpQueues().add(myCollection);
        myCollection.setShouldCache(true);

    }



}
