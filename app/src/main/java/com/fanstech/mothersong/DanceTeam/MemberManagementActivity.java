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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.JoinDanceAdapter;
import com.fanstech.mothersong.adapter.MemberAdapter;
import com.fanstech.mothersong.bean.DanceUserMessage;
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
 *时间：2016/6/30 0030 上午 10:26
 *功能模块：成员列表
 */
public class MemberManagementActivity extends BaseActivity{

    private List<DanceUserMessage> lists;
    private MemberAdapter adapter;
    private ListView listView;
    private MaterialRefreshLayout refresh;
    private TextView no;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_management;
    }

    @Override
    protected void initViews() {

        listView = (ListView) findViewById(R.id.member_dance_lists);
        no = (TextView) findViewById(R.id.no);
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


        getMemberList();

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:

                    if(lists.size()<=0){
                        no.setVisibility(View.VISIBLE);
                    }

                    adapter = new MemberAdapter(mActivity, lists,1);
                    listView.setAdapter(adapter);
                    break;

            }
        }
    };

    /**
     * 队员列表
     */
    public void getMemberList(){

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("state", "1");

        RewriteRequest message = new RewriteRequest(UrlConfig.GETMEMBER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    lists = new ArrayList<>();
                    JSONObject objects = new JSONObject(jsonObject.getString("obj"));
                    JSONArray array = new JSONArray(objects.getString("items"));
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = (JSONObject) array.get(i);
                        DanceUserMessage danceMessage = gson.fromJson(object.toString(), DanceUserMessage.class);
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

        message.setTag("message");
        MyApplication.getHttpQueues().add(message);
        message.setShouldCache(true);

    }


}
