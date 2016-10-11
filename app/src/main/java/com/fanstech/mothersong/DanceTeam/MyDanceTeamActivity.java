package com.fanstech.mothersong.DanceTeam;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.MyDanceMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.fanstech.mothersong.video.UploadVideoActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：胖胖祥
 * 时间：2016/6/23 0023 上午 11:50
 * 功能模块：我的舞队
 */
public class MyDanceTeamActivity extends BaseActivity {

    private ProgressBar load;
    private LinearLayout yes, no;
    private Button establishButton, my_dance_join, dance_member, dance_upd;//创建舞队，加入舞队,舞队成员，上传视频
    private TextView function_text;//头部右边按钮

    private TextView name, squareName, createUserName, memberCount, introduce;//舞队名，主场名，管理员，人数，简介
    private ImageViewUtil dance_head;//舞队头像

    private MyDanceMessage dance;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_dance_team;
    }

    @Override
    protected void initViews() {

        setTitle("我的舞队");
        load = (ProgressBar) findViewById(R.id.load);
        dance_head = (ImageViewUtil) findViewById(R.id.dance_head);
        name = (TextView) findViewById(R.id.dance_name);
        squareName = (TextView) findViewById(R.id.dance_squareName);
        createUserName = (TextView) findViewById(R.id.dance_createUserName);
        memberCount = (TextView) findViewById(R.id.dance_memberCount);
        introduce = (TextView) findViewById(R.id.dance_introduce);

        function_text = (TextView) findViewById(R.id.function_text);

//        判断是否是管理员
        if (SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.adminDance, "").equals("0")) {

            function_text.setVisibility(View.GONE);

        } else {

            function_text.setVisibility(View.VISIBLE);

        }
        function_text.setOnClickListener(this);
        yes = (LinearLayout) findViewById(R.id.my_dance_yes);
        no = (LinearLayout) findViewById(R.id.my_dance_no);
        establishButton = (Button) findViewById(R.id.my_dance_establish);
        establishButton.setOnClickListener(this);
        my_dance_join = (Button) findViewById(R.id.my_dance_join);
        my_dance_join.setOnClickListener(this);
        dance_member = (Button) findViewById(R.id.dance_member);
        dance_member.setOnClickListener(this);
        dance_upd = (Button) findViewById(R.id.dance_upd);
        dance_upd.setOnClickListener(this);

//        WhetherDance();
        SelectUserEsDance();

    }

    private final int STATENO = 1;//未加入舞队
    private final int STATEYES = 2;//加入舞队
    private final int DANCEMESSAGE = 3;//舞队信息
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case STATEYES:

                    getDanceMessage();
                    yes.setVisibility(View.VISIBLE);
                    no.setVisibility(View.GONE);

                    break;

                case STATENO:
                    no.setVisibility(View.VISIBLE);
                    yes.setVisibility(View.GONE);
                    load.setVisibility(View.GONE);
                    break;

                case DANCEMESSAGE:

                    yes.setVisibility(View.VISIBLE);
                    no.setVisibility(View.GONE);
                    load.setVisibility(View.GONE);
                    name.setText(dance.getName());
                    squareName.setText(dance.getSquareName());
                    createUserName.setText(dance.getCreateUserName());
                    SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.danceID, dance.getId());
                    memberCount.setText(dance.getMemberCount() + "人");
                    introduce.setText(dance.getIntroduce());
                    ImageLoader.getInstance().displayImage(UrlConfig.URL + dance.getHead(), dance_head,
                            ImageLoadOptions.getOptions());

                    break;

            }

        }
    };


    /**
     * 查询用户是否创建舞队
     */
    public void SelectUserEsDance() {


        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        Log.e("token", "token  " + SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        RewriteRequest select = new RewriteRequest(UrlConfig.WHETHUSER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonO111111111bject" + jsonObject.toString());

                try {

                    if (jsonObject.getString("obj").equals("null") || jsonObject.getString("obj") == null) {

                        WhetherDance();

                    } else {

                        JSONObject object = new JSONObject(jsonObject.getString("obj"));
                        Gson gson = new Gson();
                        dance = gson.fromJson(object.toString(), MyDanceMessage.class);

                        handler.sendEmptyMessage(DANCEMESSAGE);

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

        select.setTag("select");
        MyApplication.getHttpQueues().add(select);
        select.setShouldCache(true);


    }

    /**
     * 判断是否加入舞队
     */
    public void WhetherDance() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        RewriteRequest dance = new RewriteRequest(UrlConfig.WHETHERDANCE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());

                try {

                    String obj = jsonObject.getString("obj");
                    if (obj.equals("1")) {

                        Log.e("已加入", "已加入");
                        handler.sendEmptyMessage(STATEYES);

                    } else {

                        Log.e("未加入", "未加入");
                        handler.sendEmptyMessage(STATENO);

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

        dance.setTag("dance");
        MyApplication.getHttpQueues().add(dance);
        dance.setShouldCache(true);

    }

    /**
     * 获取舞队信息
     */
    public void getDanceMessage() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        RewriteRequest getDance = new RewriteRequest(UrlConfig.GETMYDANCE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

                try {

                    JSONObject object = new JSONObject(jsonObject.getString("obj"));
                    Gson gson = new Gson();
                    dance = gson.fromJson(object.toString(), MyDanceMessage.class);
                    handler.sendEmptyMessage(DANCEMESSAGE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        getDance.setTag("getDance");
        MyApplication.getHttpQueues().add(getDance);
        getDance.setShouldCache(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = new Bundle();
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case 3:
                SelectUserEsDance();
                getUserDanceAdmin(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
                break;

        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {

            //加入舞队
            case R.id.my_dance_join:
                startActivity(JoinAddreNameActivity.class);
                break;

            //创建舞队
            case R.id.my_dance_establish:
                startActivity(EstablishDanceTeamActivity.class, bundle, 3);
                break;

            //舞队信息
            case R.id.function_text:
                startActivity(DanceMessageActivity.class);
                break;

            //队员管理
            case R.id.dance_member:
                startActivity(MemberManagementActivity.class);
                break;

            case R.id.dance_upd:
                startActivity(UploadVideoActivity.class);
                break;
        }

    }



    /**
     * 判断该用户是否是舞队管理员
     */
    public void getUserDanceAdmin(String token){

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", token);
        RewriteRequest admin = new RewriteRequest(UrlConfig.ADMIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "是否是管理员" + jsonObject);

                try {

                    String state = jsonObject.getString("obj");

                    //0不是管理员，1代表是管理员
                    if (state.equals("0")) {
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.adminDance, "0");
                    } else {
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.adminDance, "1");
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

        admin.setTag("admin");
        MyApplication.getHttpQueues().add(admin);
        admin.setShouldCache(true);

    }

}
