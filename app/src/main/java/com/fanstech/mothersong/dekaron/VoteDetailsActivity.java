package com.fanstech.mothersong.dekaron;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.VoteMessage;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 作者：胖胖祥
 * 时间：2016/7/4 0004 上午 10:36
 * 功能模块：投票详情
 */
public class VoteDetailsActivity extends BaseActivity {

    private Button vote_a, shar_a, vote_b, shar_b;
    private ImageView vote_dance_head_a, vote_dance_head_b, vote_dance_video_image_a, vote_dance_video_image_b;
    private TextView vote_dance_name_a, vote_dance_name_b;

    private VoteMessage messge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vote_details;
    }

    @Override
    protected void initViews() {

        messge = (VoteMessage) getIntent().getSerializableExtra("messge");

        vote_dance_head_a = (ImageView) findViewById(R.id.vote_dance_head_a);
        vote_dance_head_b = (ImageView) findViewById(R.id.vote_dance_head_b);
        vote_dance_video_image_a = (ImageView) findViewById(R.id.vote_dance_video_image_a);
        vote_dance_video_image_b = (ImageView) findViewById(R.id.vote_dance_video_image_b);

        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getF_danceHead(), vote_dance_head_a,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getF_cover(), vote_dance_video_image_a,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getJ_danceHead(), vote_dance_head_b,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getJ_cover(), vote_dance_video_image_b,
                ImageLoadOptions.getOptions());

        vote_dance_name_a = (TextView) findViewById(R.id.vote_dance_name_a);
        vote_dance_name_a.setText(messge.getF_danceName());
        vote_dance_name_b = (TextView) findViewById(R.id.vote_dance_name_b);
        vote_dance_name_b.setText(messge.getJ_danceName());

        vote_a = (Button) findViewById(R.id.vote_button1_a);
        vote_a.setOnClickListener(this);
        shar_a = (Button) findViewById(R.id.vote_button2_a);
        shar_a.setOnClickListener(this);
        vote_b = (Button) findViewById(R.id.vote_button1_b);
        vote_b.setOnClickListener(this);
        shar_b = (Button) findViewById(R.id.vote_button2_b);
        shar_b.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.vote_button1_a:

                if (MyApplication.getInstance().isLogin()) {//已登录
                    SubmitVote(messge.getId(), messge.getF_danceId());
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.vote_button2_a:
                showShare();
                break;
            case R.id.vote_button1_b:

                if (MyApplication.getInstance().isLogin()) {//已登录
                    SubmitVote(messge.getId(), messge.getJ_danceId());
                } else {
                    Toast.makeText(mActivity, "请先登录", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.vote_button2_b:
                showShare();
                break;

        }

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
        oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.fanstech.mothersong#opened");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在妈妈颂上进行舞队pk，请大家为我舞队投票");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://120.25.172.152/app/mms.png");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.fanstech.mothersong#opened");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.fanstech.mothersong#opened");
       // 启动分享GUI
        oks.show(this);

    }

    /**
     * 投票
     */
    public void SubmitVote(String dpId, String danceId) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("dpId", dpId);
        mMap.put("danceId", danceId);

        RewriteRequest vote = new RewriteRequest(UrlConfig.VOTEPIAO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

                try {

                    String obj = jsonObject.getString("obj");

                    Toast.makeText(mActivity, obj, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        vote.setTag("vote");
        MyApplication.getHttpQueues().add(vote);
        vote.setShouldCache(true);

    }

}
