package com.fanstech.mothersong;

import android.app.FragmentManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.main_fragment.CommunityFragment;
import com.fanstech.mothersong.main_fragment.MainFragment;
import com.fanstech.mothersong.main_fragment.MasterFragment;
import com.fanstech.mothersong.main_fragment.MyFragment;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.AllUtil;

/**
 * 作者：胖胖祥
 * 时间：2016/6/15 0015 下午 3:58
 * 功能模块：主界面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout main_main;//首页
    private RelativeLayout main_master;//达人
    private RelativeLayout main_community;//社区
    private RelativeLayout main_my;//我的

    //保留当前的显示的fragment的标签
    private String mLastFragmentTag;
    private final static String MAIN = "MAIN";
    private final static String MASTER = "MASTER";
    private final static String COMMUNITY = "COMMUNITY";
    private final static String MY = "MY";

    private MainFragment main_fragment;
    private MasterFragment master_fragment;
    private CommunityFragment community_fragment;
    private MyFragment my_fragment;

    private ImageView main_image, master_image, community_image, my_image;
    private TextView main_text, master_text, community_text, my_text;

    private RelativeLayout yindao;
    private LinearLayout ly_tab_bar;
    private FrameLayout ly_content;
    private ImageView next,skip;

    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    public void initView() {

        //判断是否登录了
        String token = SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.token, "");

        if (AllUtil.isNotNull(token)) {//已经登录
            MyApplication.getInstance().setLogin(true);
        } else {
            MyApplication.getInstance().setLogin(false);
        }
        yindao = (RelativeLayout) findViewById(R.id.yindao);
        ly_tab_bar = (LinearLayout) findViewById(R.id.ly_tab_bar);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);
        next = (ImageView) findViewById(R.id.next);
        next.setOnClickListener(this);
        skip = (ImageView) findViewById(R.id.skip);
        skip.setOnClickListener(this);

        if(SharePreferenceUtil.getInstance(this).getString(SharePreferenceUtil.loading,"").equals("")){
            yindao.setVisibility(View.VISIBLE);
            ly_tab_bar.setVisibility(View.GONE);
            ly_content.setVisibility(View.GONE);
        }else{
            yindao.setVisibility(View.GONE);
            ly_tab_bar.setVisibility(View.VISIBLE);
            ly_content.setVisibility(View.VISIBLE);
        }

        main_main = (RelativeLayout) findViewById(R.id.main_main);
        main_main.setOnClickListener(this);
        main_master = (RelativeLayout) findViewById(R.id.main_master);
        main_master.setOnClickListener(this);
        main_community = (RelativeLayout) findViewById(R.id.main_community);
        main_community.setOnClickListener(this);
        main_my = (RelativeLayout) findViewById(R.id.main_my);
        main_my.setOnClickListener(this);

        main_image = (ImageView) findViewById(R.id.main_image);
        master_image = (ImageView) findViewById(R.id.master_image);
        community_image = (ImageView) findViewById(R.id.community_image);
        my_image = (ImageView) findViewById(R.id.my_image);
        main_text = (TextView) findViewById(R.id.main_text);
        master_text = (TextView) findViewById(R.id.master_text);
        community_text = (TextView) findViewById(R.id.community_text);
        my_text = (TextView) findViewById(R.id.my_text);

        main_fragment = new MainFragment();
        master_fragment = new MasterFragment();
        community_fragment = new CommunityFragment();
        my_fragment = new MyFragment();

    }

    private void setTabClassification(int mClassificationFlag) {

        // 首页
        main_image.setBackgroundResource(mClassificationFlag == 0 ? R.mipmap.tab_channel_pressed : R.mipmap.tab_channel_normal);
//        main_text.setTextColor(getResources().getColor(mClassificationFlag == 0 ? R.color.text_yellow : R.color.bg_topbar));
        //达人
        master_image.setBackgroundResource(mClassificationFlag == 1 ? R.mipmap.tab_message_pressed : R.mipmap.tab_message_normal);
        //社区
        community_image.setBackgroundResource(mClassificationFlag == 2 ? R.mipmap.tab_better_pressed : R.mipmap.tab_better_normal);
        //我的
        my_image.setBackgroundResource(mClassificationFlag == 3 ? R.mipmap.tab_my_pressed : R.mipmap.tab_my_normal);

    }

    protected void initData() {
        change(MAIN);
        setTabClassification(0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            
            case R.id.main_main:
            case R.id.main_master:
            case R.id.main_community:
            case R.id.main_my:
                switchTabFragment(v.getId());
                break;

            case R.id.skip:

                SharePreferenceUtil.getInstance(this).saveKeyObjValue(SharePreferenceUtil.loading,"yes");
                yindao.setVisibility(View.GONE);
                ly_tab_bar.setVisibility(View.VISIBLE);
                ly_content.setVisibility(View.VISIBLE);

                break;

            case R.id.next:
                state++;
                switch (state){

                    case 1:

                        yindao.setBackgroundResource(R.mipmap.yindaob);

                        break;
                    case 2:

                        yindao.setBackgroundResource(R.mipmap.yindaoc);

                        break;
                    case 3:

                        yindao.setBackgroundResource(R.mipmap.yindaod);
                        skip.setVisibility(View.GONE);
                        next.setBackgroundResource(R.mipmap.ms_tiyan);

                        break;
                    
                    case 4:

                        SharePreferenceUtil.getInstance(this).saveKeyObjValue(SharePreferenceUtil.loading,"yes");
                        yindao.setVisibility(View.GONE);
                        ly_tab_bar.setVisibility(View.VISIBLE);
                        ly_content.setVisibility(View.VISIBLE);

                        break;

                }

                break;

            default:
                break;
        }

    }

    private void switchTabFragment(int checkedId) {

        switch (checkedId) {
            case R.id.main_main://首页
                change(MAIN);
                setTabClassification(0);
                break;
            case R.id.main_master://达人
                change(MASTER);
                setTabClassification(1);
                break;
            case R.id.main_community://社区
                change(COMMUNITY);
                setTabClassification(2);
                break;
            case R.id.main_my://我的
                change(MY);
                setTabClassification(3);
                break;

        }
    }

    //切换fragment
    private void change(String nowTag) {

        if (nowTag.equals(MAIN)) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ly_content, main_fragment, "MAIN").commit();

        } else if (nowTag.equals(MASTER)) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ly_content, master_fragment, "second").commit();

        } else if (nowTag.equals(COMMUNITY)) {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ly_content, community_fragment, "third").commit();


        } else if (nowTag.equals(MY)) {

            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ly_content, my_fragment, "four").commit();

        }
    }


    /* （再按一次退出程序） */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {

                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {

                this.finish();

            }
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }


}
