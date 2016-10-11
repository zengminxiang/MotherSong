package com.fanstech.mothersong.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.VideoCommentAdapter;
import com.fanstech.mothersong.bean.VideoComment;
import com.fanstech.mothersong.bean.VideoCommentUser;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.custom_view.Utility;
import com.fanstech.mothersong.main_fragment.community_utils.DensityUtil;
import com.fanstech.mothersong.player_util.VideoPlayView;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.ResizeRelativeLayout;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 作者：胖胖祥
 * 时间：2016/6/22 0022 上午 10:14
 * 功能模块：视频详情
 */
public class VideoDetailsActivity extends Activity implements PopupWindow.OnDismissListener,View.OnClickListener{

    private View button1;
    private TextView desc;//视频简介

    private String path;//http://ocs.maiziedu.com/android_app_sde_1.mp4
    private Uri uri;
    boolean isPortrait=true;

    private FrameLayout frameLayout,full_screen;
    private VideoPlayView videoItemView;//播放的view
    private LinearLayout layout_zong;
    private RelativeLayout showview;
    private ImageView image_bg;

    private VideoMessage video;//视频的资料
    private boolean zan = false;//判断用户是否已经点赞了，默认是没有点赞
    private boolean coll = false;//判断用户是否已经收藏了，默认是没有收藏
    private ImageView dianzan, collection_image;


    //评论区
    private TextView common_number;
    private List<VideoComment> lists;
    private VideoCommentAdapter adapter;
    private ListView listView;
    private String totalCount = "0";//总条数

    //弹出评论框
    private PopupWindow mEditMenuWindow;
    private int mMenuOpenedHeight;//编辑菜单打开时的高度
    private boolean mIsKeyboardOpened;// 软键盘是否显示
    private EditText edit_comment;//评论的内容
    private Button button;//提交评论的按钮

    private ProgressBar load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_details);// 主文件布局
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initViews();

    }

    protected void initViews() {

        video = (VideoMessage) getIntent().getSerializableExtra("video");
        Log.e("video", "video" + video.getId());
        path = "http://120.25.172.152"+video.getPath();
        desc = (TextView) findViewById(R.id.video_details_desc);
        desc.setText(video.getIntroduce());
        dianzan = (ImageView) findViewById(R.id.dianzan);
        layout_zong = (LinearLayout) findViewById(R.id.layout_zong);
        showview = (RelativeLayout) findViewById(R.id.showview);
        collection_image = (ImageView) findViewById(R.id.collection_image);
        listView = (ListView) findViewById(R.id.video_details_lists);
        common_number = (TextView) findViewById(R.id.common_number);
        button1 = (Button) findViewById(R.id.button1);
        load = (ProgressBar) findViewById(R.id.load);

        frameLayout = (FrameLayout) findViewById(R.id.layout_video);
        full_screen = (FrameLayout) findViewById(R.id.full_screen);
        videoItemView = new VideoPlayView(this);
        image_bg = (ImageView) findViewById(R.id.image_bg);
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+video.getCover(), image_bg,
                ImageLoadOptions.getOptions());

        getComment(video.getId(), "");

        if (MyApplication.getInstance().isLogin()) {//已登录

            WhetherZambia(video.getId());
            WhetherCollection(video.getId());

        }

        addBrowser(video.getId());//添加播放量

        if (getAPNType(this) != 1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("无可用WiFi,使用移动网络继续播放？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认播放", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    PlayVoid();
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
        }else{

            PlayVoid();

        }

        //弹出评论
        // 编辑窗口
        LayoutInflater inflater = getLayoutInflater();
        View menuLayout = inflater.inflate(R.layout.menu_window, null);
        mEditMenuWindow = new PopupWindow(menuLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mEditMenuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        mEditMenuWindow.setTouchable(true);
        mEditMenuWindow.setFocusable(true);
        mEditMenuWindow.setAnimationStyle(R.style.MenuAnimation);
        mEditMenuWindow.setOutsideTouchable(false);
        mEditMenuWindow.update();
        //监听菜单消失
        mEditMenuWindow.setOnDismissListener(this);
        edit_comment = (EditText) menuLayout.findViewById(R.id.menu_edit);
        button = (Button) menuLayout.findViewById(R.id.menu_send);
        button.setOnClickListener(this);
        // 监听主布局大小变化,监控输入法软键盘状态
        listenerKeyBoardState(menuLayout);


    }

    private final int ONE = 1;//获取评论完毕
    private final int ERROR = 400;//连接失败
    private final int LOVE = 2;//判断是否点赞
    private final int ZAN = 3;//点赞成功操作
    private final int COLL = 4;//收藏成功操作

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case ONE:

                    load.setVisibility(View.GONE);
                    common_number.setText("评论" + totalCount + "条");
                    adapter = new VideoCommentAdapter(VideoDetailsActivity.this, lists);
                    listView.setAdapter(adapter);
                    Utility.setListViewHeightBasedOnChildren(listView);

                    break;

                case ERROR:
                    Toast.makeText(VideoDetailsActivity.this, "连接失败", Toast.LENGTH_LONG).show();
                    break;

                case LOVE:

                    if (zan) {

                        dianzan.setBackgroundResource(R.mipmap.zana);

                    } else {

                        dianzan.setBackgroundResource(R.mipmap.znab);
                    }

                    if (coll) {

                        collection_image.setBackgroundResource(R.mipmap.conn);

                    } else {

                        collection_image.setBackgroundResource(R.mipmap.conns);

                    }

                    break;

                case ZAN:
                    dianzan.setBackgroundResource(R.mipmap.zana);
                    break;
                case COLL:
                    collection_image.setBackgroundResource(R.mipmap.conn);
                    break;

            }

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.menu_send:

                if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                    Toast.makeText(VideoDetailsActivity.this, "说点什么咯", Toast.LENGTH_LONG).show();
                    return;
                }

                addComment(video.getId(), edit_comment.getText().toString());
                closeButtomSend();

                break;

        }

    }


    public void PlayVoid(){

        frameLayout.removeAllViews();
        frameLayout.addView(videoItemView);
        videoItemView.start(path);
        //播放完监听
        videoItemView.setCompletionListener(new VideoPlayView.CompletionListener() {
            @Override
            public void completion(IMediaPlayer mp) {

                videoItemView.release();

                showview.setVisibility(View.VISIBLE);

            }
        });

    }

    //全屏设置
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (videoItemView != null) {

            videoItemView.onChanged(newConfig);

            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

                full_screen.setVisibility(View.GONE);
                full_screen.removeAllViews();
                frameLayout.removeAllViews();
                frameLayout.addView(videoItemView);
                layout_zong.setVisibility(View.VISIBLE);

                videoItemView.setContorllerVisiable();


            }else{

                ViewGroup viewGroup = (ViewGroup) videoItemView.getParent();
                if (viewGroup == null)
                    return;
                viewGroup.removeAllViews();
                full_screen.addView(videoItemView);
                layout_zong.setVisibility(View.GONE);
                full_screen.setVisibility(View.VISIBLE);

            }

        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;

            }
        }
        return super.onKeyUp(keyCode, event);

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (videoItemView != null) {
            videoItemView.stop();
        }
    }

    /**
     * 获取视频评论
     *
     * @param videoId 视频的id
     * @param page    当前页
     */
    public void getComment(String videoId, String page) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("videoId", videoId);
        mMap.put("page", "1");

        RewriteRequest comment = new RewriteRequest(UrlConfig.GETCOUMMENT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    String obj = jsonObject.getString("obj");

                    Log.e("obj", "obj" + obj);
                    lists = new ArrayList<>();
                    JSONObject object = new JSONObject(obj);
                    JSONObject coumt = new JSONObject(object.getString("paginator"));
                    totalCount = coumt.getString("totalCount");
                    JSONArray array = new JSONArray(object.getString("items"));
                    //第一层循环
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject json = (JSONObject) array.get(i);
                        Log.e("obj", "obj" + json.toString());
                        VideoComment comm = new VideoComment();
                        comm.setId(json.getString("id"));
                        comm.setVideoId(json.getString("videoId"));
                        comm.setHeUserId(json.getString("heUserId"));
                        comm.setMyUserId(json.getString("myUserId"));
                        comm.setContent(json.getString("content"));
                        comm.setTime(json.getString("time"));
                        comm.setTimeStr(json.getString("timeStr"));

                        //获取发表评论的用户
                        JSONObject jsonuser = new JSONObject(json.getString("user"));
                        VideoCommentUser user = new VideoCommentUser();
                        user.setHead(jsonuser.getString("head"));
                        user.setNickname(jsonuser.getString("nickname"));
                        comm.setUser(user);
                        Log.e("user", "user" + jsonuser.toString());
                        lists.add(comm);

                    }

                    handler.sendEmptyMessage(ONE);

                } catch (JSONException e) {
                    load.setVisibility(View.GONE);
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);

            }
        }, mMap);
        comment.setTag("comment");
        MyApplication.getHttpQueues().add(comment);
        comment.setShouldCache(true);

    }

    /**
     * 添加评论
     *
     * @param videoId 视频id
     * @param comment 内容
     */
    public void addComment(String videoId, String comment) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        mMap.put("comment", comment);

        RewriteRequest addComment = new RewriteRequest(UrlConfig.ADDCOUMMENT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    String state = jsonObject.getString("state");
                    if (state.equals("200")) {

                        //更新评论列表
                        getComment(video.getId(), "");

                    } else {

                        Log.e("评论失败", "评论失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        addComment.setTag("addcomment");
        MyApplication.getHttpQueues().add(addComment);
        addComment.setShouldCache(true);

    }

    /**
     * 判断用户是否已经收藏过了
     *
     * @param videoId
     */
    public void WhetherCollection(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        RewriteRequest collection = new RewriteRequest(UrlConfig.WHETHERCOLLECTION, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "判断是否收藏了" + jsonObject.toString());

                try {
                    String obj = jsonObject.getString("obj");

                    if (obj.equals("1")) {

                        coll = true;

                    }

                    handler.sendEmptyMessage(LOVE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        collection.setTag("collection");
        MyApplication.getHttpQueues().add(collection);
        collection.setShouldCache(true);

    }

    /**
     * 判断用户是否点赞了
     *
     * @param videoId
     */
    public void WhetherZambia(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        RewriteRequest zambia = new RewriteRequest(UrlConfig.WHETHERZAMBIA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "判断是否点赞了了" + jsonObject.toString());
                try {
                    String obj = jsonObject.getString("obj");

                    if (obj.equals("1")) {

                        zan = true;

                    }

                    handler.sendEmptyMessage(LOVE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        zambia.setTag("zambia");
        MyApplication.getHttpQueues().add(zambia);
        zambia.setShouldCache(true);

    }

    /**
     * 点赞
     *
     * @param videoId
     */
    public void AddZambia(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        RewriteRequest addzambia = new RewriteRequest(UrlConfig.ADDZAMBIA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {

                    String state = jsonObject.getString("state");

                    if (state.equals("200")) {

                        zan = true;
                        Toast.makeText(VideoDetailsActivity.this, "点赞成功", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(ZAN);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        addzambia.setTag("zambia");
        MyApplication.getHttpQueues().add(addzambia);
        addzambia.setShouldCache(true);

    }

    /**
     * 收藏
     *
     * @param videoId
     */
    public void AddColletion(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        RewriteRequest addColletion = new RewriteRequest(UrlConfig.ADDCOLLECTION, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonO111111111bject" + jsonObject.toString());

                try {

                    String state = jsonObject.getString("state");

                    if (state.equals("200")) {

                        coll = true;

                        Toast.makeText(VideoDetailsActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
                        handler.sendEmptyMessage(COLL);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        addColletion.setTag("addcolletion");
        MyApplication.getHttpQueues().add(addColletion);
        addColletion.setShouldCache(true);

    }

    public void addBrowser(String videoId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(VideoDetailsActivity.this).getString(SharePreferenceUtil.token, ""));
        mMap.put("videoId", videoId);
        RewriteRequest addBrowser = new RewriteRequest(UrlConfig.ADDBROWSER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonO111111111bject" + jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                handler.sendEmptyMessage(ERROR);
            }
        }, mMap);

        addBrowser.setTag("addBrowser");
        MyApplication.getHttpQueues().add(addBrowser);
        addBrowser.setShouldCache(true);

    }

    /**
     * 分享监听
     */
    public void onClickShare(View v){

        showShare();

    }

    /**
     * 点赞监听
     *
     * @param v
     */
    public void onClickZambia(View v) {

        if (!MyApplication.getInstance().isLogin()) {//已登录

            Toast.makeText(VideoDetailsActivity.this, "请登录", Toast.LENGTH_LONG).show();
            return;

        }

        if (zan) {

            Toast.makeText(VideoDetailsActivity.this, "已经点赞了", Toast.LENGTH_LONG).show();

        } else {

            AddZambia(video.getId());

        }

    }

    /**
     * 收藏监听
     *
     * @param v
     */
    public void onClickCollection(View v) {

        if (!MyApplication.getInstance().isLogin()) {//已登录

            Toast.makeText(VideoDetailsActivity.this, "请登录", Toast.LENGTH_LONG).show();
            return;

        }

        if (coll) {

            Toast.makeText(VideoDetailsActivity.this, "已经收藏了", Toast.LENGTH_LONG).show();

        } else {

            AddColletion(video.getId());

        }


    }

    /**
     * 编辑监听
     *
     * @param v
     */
    public void onClickEdit(View v) {

        if (!MyApplication.getInstance().isLogin()) {//已登录

            Toast.makeText(VideoDetailsActivity.this, "请登录", Toast.LENGTH_LONG).show();
            return;

        }

        clickTopSend();
    }


    /**
     * 监听主布局大小变化,监控输入法软键盘状态
     *
     * @param menuLayout
     */
    private void listenerKeyBoardState(View menuLayout) {
        ResizeRelativeLayout mMenuLayout = (ResizeRelativeLayout) menuLayout.findViewById(R.id.menu_layout);
        mMenuLayout.setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
            @Override
            public void OnResizeRelative(int w, int h, int oldw, int oldh) {
                mIsKeyboardOpened = false;
                Log.e("菜单高度", "h = " + h + ",oldh = " + oldh);

                //记录第一次打开输入法时的布局高度
                if (h < oldh && oldh > 0 && mMenuOpenedHeight == 0) {

                    mMenuOpenedHeight = h;

                }

                // 布局的高度小于之前的高度
                if (h < oldh) {
                    mIsKeyboardOpened = true;
                }
                //或者输入法打开情况下, 输入字符后再清除(三星输入法软键盘在输入后，软键盘高度增加一行，清除输入后，高度变小，但是软键盘仍是打开状态)
                else if ((h <= mMenuOpenedHeight) && (mMenuOpenedHeight != 0)) {
                    mIsKeyboardOpened = true;
                }

                Log.e("是否打开", "软键盘  = " + mIsKeyboardOpened);

            }
        });
    }

    //点击顶部发送按钮,打开/关闭编辑窗口
    private void clickTopSend() {
        if (mEditMenuWindow.isShowing()) {
            //先关闭窗口再隐藏软键盘
            mEditMenuWindow.dismiss();

            // 隐藏输入法软键盘
//            hideKeyBoard();
        } else {
            // 窗口显示前显示输入法软键盘
            showKeyBoard();

            // 显示输入窗口
            mEditMenuWindow.showAsDropDown(button1, 0, 0);
        }
    }

    // 窗口显示前显示输入法软键盘
    private void showKeyBoard() {
        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        mEditMenuWindow.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层
    }

    // 隐藏输入法软键盘
    private void hideKeyBoard() {
        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);// 输入法软键盘打开时关闭,关闭时打开
        mEditMenuWindow.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层

    }

    //点击底部发送按钮，关闭编辑窗口
    private void closeButtomSend() {
        edit_comment.setText("");
        mEditMenuWindow.dismiss();
    }

    //编辑窗口关闭时，隐藏输入法软键盘
    @Override
    public void onDismiss() {
        // 如果软键盘打开,隐藏输入法软键盘
        if (mIsKeyboardOpened) {
            hideKeyBoard();
        }
    }

    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param context
     * @return
     */
    protected static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }



    private void showShare() {

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("妈妈颂");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在妈妈颂上观看广场舞视频，有兴趣学广场舞的亲赶紧下载来玩哦！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://120.25.172.152/app/mms.png");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


}
