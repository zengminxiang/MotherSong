package com.fanstech.mothersong.main_fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.RankingList.RankingListActivity;
import com.fanstech.mothersong.adapter.VideoGridAdapter;
import com.fanstech.mothersong.adapter.VideoListAdapter;
import com.fanstech.mothersong.bean.Video;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.bean.WeatherMessage;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.dekaron.DekaronAcrivity;
import com.fanstech.mothersong.dekaron.VoteListActivity;
import com.fanstech.mothersong.dekaron.YuezhanActivity;
import com.fanstech.mothersong.public_class.BaseFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.ui.HuodongActivity;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.fanstech.mothersong.video.VideoListActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：胖胖祥
 * 时间：2016/6/12 0012 上午 11:26
 * 功能模块：首页
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {


    private TextView main_video,main_ranking,main_vote,main_activity;//视频模块
    private TextView temperature,week;//温度
    private ImageView weathers_image;//天气的图片
    private  WeatherMessage weathers;//天气的类
    private final int WEATHERS = 1;
    private ImageView map;

    //热门视频
    private List<VideoMessage> lists ;
    private GridView hot_gview;
    private VideoGridAdapter adapter;

    //定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private StringBuffer addre = new StringBuffer(256);//当前定位地址

    private boolean isFristLocation = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case WEATHERS:
                    temperature.setText(weathers.getTemperature());
                    ImageLoader.getInstance().displayImage(weathers.getDayPictureUrl(), weathers_image,
                            ImageLoadOptions.getOptions());
                    Date date=new Date();
                    SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
                    week.setText(dateFm.format(date));

                    break;

                case 3:
                    Log.e("jsonObject","视频数据："+lists.size());
                    adapter = new VideoGridAdapter(mActivity,lists);
                    hot_gview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;

            }

        }
    };

    @Override
    protected void initView() {

        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if(!isNetworkAvailable()){
            Toast.makeText(mActivity,"连接网络失败，请检查！",Toast.LENGTH_LONG).show();
        }

        main_video = (TextView) findViewById(R.id.main_video);
        main_video.setOnClickListener(this);
        main_ranking = (TextView) findViewById(R.id.main_ranking);
        main_ranking.setOnClickListener(this);
        main_vote = (TextView) findViewById(R.id.main_vote);
        main_vote.setOnClickListener(this);
        main_activity = (TextView) findViewById(R.id.main_activity);
        main_activity.setOnClickListener(this);
        temperature = (TextView) findViewById(R.id.temperature);
        weathers_image = (ImageView) findViewById(R.id.weathers_image);
        week = (TextView) findViewById(R.id.week);
        map = (ImageView) findViewById(R.id.map);
        map.setOnClickListener(this);
        hot_gview = (GridView) findViewById(R.id.hot_gview);
        getWeatherMessage();
        getVideoListMessage("","","");

        //定位
        mLocationClient = new LocationClient(mActivity.getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.main_video:
                Intent intent = new Intent(mActivity, VideoListActivity.class);
                mActivity.startActivity(intent);
                break;

            case R.id.map:
                startActivity(YuezhanActivity.class);
                break;
            case R.id.main_ranking:
                startActivity(RankingListActivity.class);
                break;

            case R.id.main_vote:
                startActivity(VoteListActivity.class);
                break;
            case R.id.main_activity:
                startActivity(HuodongActivity.class);
                break;

        }

    }

    /**
     * 获取天气预报信息
     * <p>
     * //http://api.map.baidu.com/telematics/v3/weather?location=%E5%B9%BF%E5%B7%9E&output=json&ak=FK9mkfdQsloEngodbFl4FeY3
     */
    public void getWeatherMessage() {

        JsonObjectRequest add = new JsonObjectRequest(Request.Method.GET, "http://api.map.baidu.com/telematics/v3/weather?location=%E5%B9%BF%E5%B7%9E&output=json&ak=FK9mkfdQsloEngodbFl4FeY3", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    Gson g = new Gson();
                    JSONArray array = new JSONArray(jsonObject.getString("results"));
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = (JSONObject) array.get(i);
                        JSONArray as = new JSONArray(obj.getString("weather_data"));

                        for (int j = 0; j < as.length(); j++) {

                            JSONObject weather = (JSONObject) as.get(0);
                            weathers = g.fromJson(weather.toString(), WeatherMessage.class);

                        }

                    }

                    handler.sendEmptyMessage(WEATHERS);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        add.setTag("add");
        MyApplication.getHttpQueues().add(add);
        add.setShouldCache(true); // 控制是否缓存

    }


    //定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            Log.e("addr", "addr");
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果


                addre.append(location.getProvince());//省份
                addre.append(location.getCity());//市
                addre.append(location.getDistrict());//区
                addre.append(location.getStreet());//街道
                addre.append(location.getStreetNumber());//街道号

                Log.e("addr", "addr" + addre.toString());


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                addre.append(location.getProvince());//省份
                addre.append(location.getCity());//市
                addre.append(location.getDistrict());//区
                addre.append(location.getStreet());//街道
                addre.append(location.getStreetNumber());//街道号

                Log.e("addr", "addr" + addre.toString());

            }

            //第一次定位
            if (isFristLocation) {

                isFristLocation = false;

            }

        }


    }

    /**
     * 获取视频列表
     * @param title 标题，用作搜索
     * @param sort 排序，1最新，2最热
     * @param page 当前页
     */

    public void getVideoListMessage(String title,String sort,String page){

        Map<String,String> mMap = new HashMap<>();
        mMap.put("title","");
        mMap.put("sort","2");
        mMap.put("page","1");

        RewriteRequest video = new RewriteRequest(UrlConfig.VIDEOLIST, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject","视频数据："+jsonObject);

                try {
                    if(jsonObject.getString("state").equals("200")){

                        Gson gson;
                        lists = new ArrayList<>();
                        JSONObject obj = new JSONObject(jsonObject.getString("obj"));
                        JSONArray videos = new JSONArray(obj.getString("items"));

                        for(int i = 0;i<videos.length();i++){

                            if (i>=2){
                                break;
                            };
                            gson = new Gson();
                            JSONObject j = videos.getJSONObject(i);
                            VideoMessage video = gson.fromJson(j.toString(),VideoMessage.class);
                            lists.add(video);


                        }

                        //获取到总页数
                        JSONObject p = new JSONObject(obj.getString("paginator"));
                        String pagecount = p.getString("totalCount");

                        handler.sendEmptyMessage(3);

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

        video.setTag("video");
        MyApplication.getHttpQueues().add(video);
        video.setShouldCache(true);

    }




}
