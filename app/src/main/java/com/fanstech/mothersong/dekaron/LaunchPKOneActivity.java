package com.fanstech.mothersong.dekaron;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.MyVideoAdapter;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
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
 * 时间：2016/7/5 0005 下午 5:00
 * 功能模块：发起挑战选择视频界面
 */
public class LaunchPKOneActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadListener{

    private ListView listView;
    private RefreshLayout swipeLayout;
    private List<VideoMessage> lists = new ArrayList<>();
    private MyVideoAdapter adapter;
    private String danceId;
    private String day;
    private String pkid=null;
    private TextView no;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_pkone;
    }

    @Override
    protected void initViews() {

        Bundle bundle = getIntent().getExtras();
        danceId = bundle.getString("danceId");
        day = bundle.getString("day");
        pkid = bundle.getString("pkid");

        setTitle("我的视频");
        swipeLayout = (RefreshLayout) findViewById(R.id.video_list_refresh);
        swipeLayout.setColorSchemeResources(R.color.swipe_color_1,
                R.color.swipe_color_2,
                R.color.swipe_color_3,
                R.color.swipe_color_4);
        listView = (ListView) findViewById(R.id.pk_my_video_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                lists.get(position).getTitle();
                Log.e("点击了", lists.get(position).getTitle() + "ss");
                dialog(lists.get(position).getId());

            }
        });
        no = (TextView) findViewById(R.id.no);
        setListener();


        getMyVideo();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    if(lists.size()<=0){
                        no.setVisibility(View.VISIBLE);
                    }else {

                        adapter = new MyVideoAdapter(mActivity, lists,0);
                        listView.setAdapter(adapter);
                    }

                    break;

            }

        }
    };

    protected void dialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(pkid != null){

            builder.setMessage("上传此视频？");

        }else{

            builder.setMessage("确认发起pk？");

        }

        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(pkid != null){
                    CoverLaunchPk(pkid,id);
                }else {
                    SubmitLaunchPk(id);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 查询我的视频
     */
    public void getMyVideo() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));

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


    /**
     * 发起pk
     * @param videoid
     */
    public void SubmitLaunchPk(String videoid){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token,""));
        mMap.put("danceId", danceId);
        mMap.put("videoId", videoid);
        mMap.put("cycle", day);

        RewriteRequest launch = new RewriteRequest(UrlConfig.SUBMITLAUNCH, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if(jsonObject.getString("state").equals("200")){
                        Toast.makeText(mActivity,jsonObject.getString("obj"),Toast.LENGTH_LONG).show();
                        mActivity.finish();
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

        launch.setTag("launch");
        MyApplication.getHttpQueues().add(launch);
        launch.setShouldCache(true);
    }

    /**
     * 接受pk
     * @param pkids
     */
    public void CoverLaunchPk(String pkids,String videoid){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token,""));
        mMap.put("videoId", videoid);
        mMap.put("pkId", pkids);

        RewriteRequest cover = new RewriteRequest(UrlConfig.COVERPK, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("接受pk","接受pk");

                try {
                    if(jsonObject.getString("state").equals("200")){
                        Toast.makeText(mActivity,jsonObject.getString("obj"),Toast.LENGTH_LONG).show();
                        mActivity.finish();
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

        cover.setTag("cover");
        MyApplication.getHttpQueues().add(cover);
        cover.setShouldCache(true);
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
