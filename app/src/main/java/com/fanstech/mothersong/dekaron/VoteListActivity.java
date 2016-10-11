package com.fanstech.mothersong.dekaron;

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
import com.fanstech.mothersong.adapter.PKDanceListAdapter;
import com.fanstech.mothersong.adapter.VoteListAdapter;
import com.fanstech.mothersong.bean.VoteMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *作者：胖胖祥
 *时间：2016/7/4 0004 上午 9:51
 *功能模块：投票列表
 */
public class VoteListActivity extends BaseActivity {

    private ListView listView;
    private List<VoteMessage> lists;
    private VoteListAdapter adapter;
    private ProgressBar load;
    private MaterialRefreshLayout refresh;
    private TextView no;

    private int pagecount;//总页数
    private int page = 1;//当前页数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote_list;
    }

    @Override
    protected void initViews() {

        listView = (ListView) findViewById(R.id.vote_list);
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
                        getVoteListmessage();

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

                            getVoteListmessage();

                        }


                    }
                }, 500);


            }

        });

        lists = new ArrayList<>();
        adapter = new VoteListAdapter(mActivity,lists);
        listView.setAdapter(adapter);

        getVoteListmessage();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:

                    Log.e("lists","lists"+lists.size());

                    if(lists.size()<=0){

                        no.setText("没有投票的舞队");
                        no.setVisibility(View.VISIBLE);

                    }

                    if(pagecount > 1){

                        refresh.setLoadMore(true);

                    }

                    load.setVisibility(View.GONE);
                    adapter.onDateChange(lists);
                    refresh.finishRefreshLoadMore();//结束上拉
                    refresh.finishRefresh();//结束下拉

                    break;

            }
        }
    };

    /**
     * 查询投票列表
     */
    public void getVoteListmessage(){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("page", page+"");
        mMap.put("pkId", "");

        RewriteRequest vote = new RewriteRequest(UrlConfig.VOTELIST, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject","jsonObject"+jsonObject);

                try {

                    JSONObject json1 = new JSONObject(jsonObject.getString("obj"));
                    JSONArray array  = new JSONArray(json1.getString("items"));
                    lists = new ArrayList<>();
                    for(int i=0;i<array.length();i++){

                        JSONObject o = (JSONObject) array.get(i);
                        VoteMessage vote = new VoteMessage();
                        vote.setId(o.getString("id"));
                        JSONObject ob = new JSONObject(o.getString("launchDance"));
                        vote.setF_danceName(ob.getString("danceName"));
                        vote.setF_danceId(ob.getString("danceId"));
                        vote.setF_danceHead(ob.getString("danceHead"));
                        JSONObject obj = new JSONObject(o.getString("launchVideo"));
                        vote.setF_cover(obj.getString("cover"));
                        vote.setF_path(obj.getString("path"));
                        vote.setF_title(obj.getString("title"));
                        vote.setF_launchVoteCount(o.getString("launchVoteCount"));
                        JSONObject obje = new JSONObject(o.getString("acceptDance"));
                        vote.setJ_danceId(obje.getString("danceId"));
                        vote.setJ_danceName(obje.getString("danceName"));
                        vote.setJ_danceHead(obje.getString("danceHead"));
                        JSONObject objec = new JSONObject(o.getString("acceptVideo"));
                        vote.setJ_cover(objec.getString("cover"));
                        vote.setJ_path(objec.getString("path"));
                        vote.setJ_title(objec.getString("title"));
                        vote.setJ_acceptVoteCount(o.getString("acceptVoteCount"));
                        vote.setTime(o.getString("time"));
                        vote.setTimeStr(o.getString("timeStr"));
                        lists.add(vote);

                    }

                    //获取到总页数
                    JSONObject p = new JSONObject(json1.getString("paginator"));
                    String count = p.getString("totalPages");
                    pagecount = Integer.parseInt(count);

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
        },mMap);

        vote.setTag("vote");
        MyApplication.getHttpQueues().add(vote);
        vote.setShouldCache(true);

    }

}
