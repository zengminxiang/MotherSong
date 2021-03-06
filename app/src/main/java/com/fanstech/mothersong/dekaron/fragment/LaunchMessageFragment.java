package com.fanstech.mothersong.dekaron.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.LaunchMessageAdapter;
import com.fanstech.mothersong.adapter.PKDanceListAdapter;
import com.fanstech.mothersong.bean.LaunchMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：胖胖祥
 * 时间：2016/7/6 0006 上午 10:16
 * 功能模块：发起的pk信息列表
 */
public class LaunchMessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener{

    private List<LaunchMessage> lists;
    private LaunchMessageAdapter adapter;
    private ListView listView;
    private RefreshLayout swipeLayout;
    private ProgressBar load;
    private TextView no;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.launch_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:

                    if (lists.size() <= 0) {
                        no.setText("没有投票的舞队");
                        no.setVisibility(View.VISIBLE);
                    } else {

                        adapter = new LaunchMessageAdapter(mActivity, lists);
                        listView.setAdapter(adapter);

                    }
                    load.setVisibility(View.GONE);

                    break;

            }
        }
    };


    @Override
    protected void initView() {

        load = (ProgressBar) findViewById(R.id.load);
        no = (TextView) findViewById(R.id.no);
        swipeLayout = (RefreshLayout) findViewById(R.id.video_list_refresh);
        swipeLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        listView = (ListView) findViewById(R.id.launch_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lists.get(position).getState().equals("0")) {
                    Toast.makeText(mActivity, "等待对方接受pk", Toast.LENGTH_LONG).show();
                }

            }
        });

        setListener();
        getLaunchMessage();
    }

    //设置监听
    private void setListener() {

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);

    }


    //发起pk
    public void getLaunchMessage() {


        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("page", "1");

        RewriteRequest launch = new RewriteRequest(UrlConfig.MYLAUNCHMESSAGE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());

                try {
                    JSONObject obj = new JSONObject(jsonObject.getString("obj"));
                    JSONArray array = new JSONArray(obj.getString("items"));
                    lists = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject json = (JSONObject) array.get(i);
                        LaunchMessage message = new LaunchMessage();
                        message.setId(json.getString("id"));
                        message.setTime(json.getString("time"));
                        message.setTimeStr(json.getString("timeStr"));
                        message.setState(json.getString("state"));
                        JSONObject j = new JSONObject(json.getString("dance"));
                        message.setDanceName(j.getString("danceName"));
                        message.setDanceHead(j.getString("danceHead"));
                        lists.add(message);

                    }

                    handler.sendEmptyMessage(1);

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

        launch.setTag("launch");
        MyApplication.getHttpQueues().add(launch);
        launch.setShouldCache(true);
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
