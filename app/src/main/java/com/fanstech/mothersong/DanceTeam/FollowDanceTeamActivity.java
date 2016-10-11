package com.fanstech.mothersong.DanceTeam;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.FollowDanceAdapter;
import com.fanstech.mothersong.adapter.JoinDanceAdapter;
import com.fanstech.mothersong.bean.FollowDance;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *作者：胖胖祥
 *时间：2016/6/27 0027 上午 11:27
 *功能模块：关注的舞队
 */
public class FollowDanceTeamActivity extends BaseActivity {

    private ListView listView;
    private MaterialRefreshLayout refresh;
    private List<FollowDance> lists = new ArrayList<>();
    private FollowDanceAdapter adapter;

    private ProgressBar load;
    private TextView no;

    private int pagecount=1;//总页数
    private int page = 1;//当前页数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_dance_team;
    }

    @Override
    protected void initViews() {


        listView = (ListView) findViewById(R.id.follow_dance_list);
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

        getMyFollowDance();
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
                        break;

                    }
                    adapter = new FollowDanceAdapter(mActivity, lists);
                    listView.setAdapter(adapter);
                    break;

            }
        }
    };


    /**
     * 查询我的关注的舞队列表
     */
    public void getMyFollowDance(){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token,""));
        mMap.put("page", page+"");

        RewriteRequest follow = new RewriteRequest(UrlConfig.SELECTFOLLOW, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                Log.e("json","json"+jsonObject.toString());

                try {
                    JSONArray array = new JSONArray(jsonObject.getString("obj"));
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = (JSONObject) array.get(i);
                        FollowDance danceMessage = gson.fromJson(object.toString(), FollowDance.class);
                        lists.add(danceMessage);

                    }

                    //获取到总页数
//                    JSONObject p = new JSONObject(object.getString("paginator"));
//                    String count = p.getString("totalPages");
//                    pagecount = Integer.parseInt(count);
//                    Log.e("pagecount","pagecountn "+pagecount);
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

        follow.setTag("follow");
        MyApplication.getHttpQueues().add(follow);
        follow.setShouldCache(true);

    }

}
