package com.fanstech.mothersong.public_class;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.fanstech.mothersong.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.comm.core.CommunitySDK;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 *作者：胖胖祥
 *时间：2016/6/13 0013 下午 6:33
 *功能模块：初始化
 */
public class MyApplication extends Application{

    public static MyApplication mInstance;
    public static RequestQueue queue;//volley全局变量
    private boolean isLogin;//是否登录

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        CrashReport.initCrashReport(getApplicationContext(), "b6c9d92119", true);
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //初始化volley
        queue = Volley.newRequestQueue(getApplicationContext());
        //异步加载图片配置
        configImageLoader();
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.loadimage)
                .showImageOnFail(R.mipmap.loadimage)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }

    public static RequestQueue getHttpQueues(){
        return queue;
    }
    public static MyApplication getInstance() {
        return mInstance;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {

        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.loadimage) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.loadimage) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.loadimage) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

}

