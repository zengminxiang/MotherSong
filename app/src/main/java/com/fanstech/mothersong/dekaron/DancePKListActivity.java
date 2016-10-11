package com.fanstech.mothersong.dekaron;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.fanstech.mothersong.adapter.PKDanceListAdapter;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
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
 * 作者：胖胖祥
 * 时间：2016/7/1 0001 下午 12:00
 * 功能模块：pk列表
 */
public class DancePKListActivity extends BaseActivity {

    private ListView listView;
    private MaterialRefreshLayout refresh;
    private List<DanceMessage> lists = new ArrayList<>();
    private String id;//广场的id
    private String name;//广场名称
    private PKDanceListAdapter adapter;
    private ProgressBar load;
    private TextView gc_name, no;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_dance_pklist;

    }

    @Override
    protected void initViews() {

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        gc_name = (TextView) findViewById(R.id.gc_name);
        gc_name.setText(name);

        listView = (ListView) findViewById(R.id.pk_list);
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
                        lists.clear();
                        getDanceList(id);

                    }
                }, 500);


            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                //上拉刷新
                refresh.postDelayed(new Runnable() {

                    @Override
                    public void run() {


                    }
                }, 500);


            }

        });

        getDanceList(id);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:

                    load.setVisibility(View.GONE);
                    if (lists.size() <= 0) {
                        no.setVisibility(View.VISIBLE);
                    }

                    if (adapter == null) {

                        adapter = new PKDanceListAdapter(mActivity, lists);
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
     * 获取某个主场下的舞队列表
     *
     * @param id 主场id
     */
    public void getDanceList(String id) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("squareId", id);

        RewriteRequest dance = new RewriteRequest(UrlConfig.SELECTDANCE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    Log.e("jsonObject", "jsonObject" + jsonObject);

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

        dance.setTag("dance");
        MyApplication.getHttpQueues().add(dance);
        dance.setShouldCache(true);

    }

}
