package com.fanstech.mothersong.DanceTeam;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.JoinDanceAddreAdapter;
import com.fanstech.mothersong.bean.FieldMessage;
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
 * 时间：2016/6/28 0028 上午 10:35
 * 功能模块：广场名列表
 */
public class JoinAddreNameActivity extends BaseActivity {

    private MaterialRefreshLayout refresh;
    private ListView listView;
    private List<FieldMessage> lists = new ArrayList<>();
    private JoinDanceAddreAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_join_addre_name;
    }

    @Override
    protected void initViews() {

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

                        refresh.finishRefresh();//结束下拉

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
        listView = (ListView) findViewById(R.id.join_list);
        getAddreList("天河区");

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    adapter = new JoinDanceAddreAdapter(mActivity, lists);
                    listView.setAdapter(adapter);

                    break;

            }

        }
    };

    /**
     * 获取主场的信息
     * @param area
     */
    public void getAddreList(final String area) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("area", area);

        RewriteRequest addre = new RewriteRequest(UrlConfig.DANCEADDRE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    JSONArray array = new JSONArray(jsonObject.getString("obj"));
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = (JSONObject) array.get(i);
                        FieldMessage fields = gson.fromJson(object.toString(), FieldMessage.class);
                        lists.add(fields);

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


}
