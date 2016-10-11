package com.fanstech.mothersong.DanceTeam;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
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
 * 作者：胖胖祥
 * 时间：2016/6/30 0030 上午 9:54
 * 功能模块：申请列表
 */
public class DanceSqMessageActivity extends BaseActivity implements MemberAdapter.ToExamineListener {

    private List<DanceUserMessage> lists;
    private MemberAdapter adapter;
    private ListView listView;
    private RefreshLayout swipeLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dance_sq_message;
    }

    @Override
    protected void initViews() {
        swipeLayout = (RefreshLayout) findViewById(R.id.video_list_refresh);
        swipeLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        listView = (ListView) findViewById(R.id.dance_sq_lists);
        getMessageList();

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    adapter = new MemberAdapter(mActivity, lists, 0);
                    adapter.setToExamine(DanceSqMessageActivity.this);
                    listView.setAdapter(adapter);
                    break;

            }
        }
    };

    /**
     * 获取申请列表
     */
    public void getMessageList() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        Log.e("danceId", "danceId " + SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.danceID, ""));
        mMap.put("danceId", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.danceID, ""));
        mMap.put("state", "0");

        RewriteRequest message = new RewriteRequest(UrlConfig.GETMEMBER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());

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


    @Override
    public void addToExamine(String state, String userid,int position) {

        ToExamineUser(userid,state,position);

    }

    /**
     * 同意和拒绝某个用户加入舞队
     */
    public void ToExamineUser(String uid, String state, final int position){

        Map<String, String> mMap = new HashMap<>();
        Log.e("token",SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("userId", uid);
        mMap.put("state", state);

        RewriteRequest message = new RewriteRequest(UrlConfig.TOEXAMINE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject","jsonObject"+jsonObject.toString());
                try {
                    if(jsonObject.getString("state").equals("200")){

                        lists.remove(position);
                        adapter.notifyDataSetChanged();

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

        message.setTag("message");
        MyApplication.getHttpQueues().add(message);
        message.setShouldCache(true);

    }

}
