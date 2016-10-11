package com.fanstech.mothersong.DanceTeam;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.JoinDanceAdapter;
import com.fanstech.mothersong.adapter.JoinDanceAddreAdapter;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.bean.FieldMessage;
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
 * 时间：2016/6/27 0027 下午 1:58
 * 功能模块：加入舞队列表
 */
public class JoinDanceListActivity extends BaseActivity implements JoinDanceAdapter.AddDance {

    private ListView listView;
    private List<DanceMessage> lists = new ArrayList<>();
    private MaterialRefreshLayout refresh;
    private JoinDanceAdapter adapter;
    private String squareId;//广场id
    private TextView no;//提示

    @Override
    protected int getLayoutId() {
        return R.layout.activity_join_dance_list;
    }

    @Override
    protected void initViews() {

        listView = (ListView) findViewById(R.id.join_dance_list);
        squareId = getIntent().getStringExtra("id");
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
                        lists.clear();
                        getDanceList(squareId);

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


                    }
                }, 500);


            }

        });

        getDanceList(squareId);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:

                    if (lists.size() <= 0) {
                        no.setVisibility(View.VISIBLE);
                    }
                    if(adapter == null){

                        adapter = new JoinDanceAdapter(mActivity, lists);
                        adapter.setAddDance(JoinDanceListActivity.this);
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
     * 获取某个主场下的舞队列表
     *
     * @param id 主场id
     */
    public void getDanceList(String id) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("squareId", id);

        RewriteRequest addre = new RewriteRequest(UrlConfig.SELECTDANCE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "" + jsonObject.toString());

                try {

                    JSONArray array = new JSONArray(jsonObject.getString("obj"));
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = (JSONObject) array.get(i);
                        DanceMessage danceMessage = gson.fromJson(object.toString(), DanceMessage.class);
                        lists.add(danceMessage);

                    }

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

        addre.setTag("dance");
        MyApplication.getHttpQueues().add(addre);
        addre.setShouldCache(true);


    }


    //加入舞队(1)和关注舞队(2)按钮监听
    @Override
    public void addDance(int label, String danceId) {


        //申请加入舞队
        if (label == 1) {

            Log.e("收藏", "点击了加入" + danceId);
            SubmitDance(danceId);
        }
        //关注舞队
        else if (label == 2) {

            Log.e("关注", "点击了加入" + danceId);
            addFollow(danceId);
        }

    }

    //申请加入舞队
    public void SubmitDance(String danceId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("danceId", danceId);

        RewriteRequest submit = new RewriteRequest(UrlConfig.ADDDANCE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

                try {

                    String state = jsonObject.getString("state");

                    if (state.equals("200")) {
                        Toast.makeText(mActivity, jsonObject.getString("obj"), Toast.LENGTH_LONG).show();
                    } else if (state.equals("400")) {
                        Toast.makeText(mActivity, jsonObject.getString("obj"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        submit.setTag("submit");
        MyApplication.getHttpQueues().add(submit);
        submit.setShouldCache(true);

    }

    public void addFollow(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);

        RewriteRequest follow = new RewriteRequest(UrlConfig.ADDFOLLOW, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

                try {

                    if (jsonObject.getString("state").equals("200")) {
                        Toast.makeText(mActivity, jsonObject.getString("obj"), Toast.LENGTH_LONG).show();
                    } else if (jsonObject.getString("state").equals("400")) {
                        Toast.makeText(mActivity, jsonObject.getString("obj"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        follow.setTag("follow");
        MyApplication.getHttpQueues().add(follow);
        follow.setShouldCache(true);

    }

}
