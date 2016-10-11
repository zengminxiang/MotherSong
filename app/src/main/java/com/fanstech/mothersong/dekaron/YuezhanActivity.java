package com.fanstech.mothersong.dekaron;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fanstech.mothersong.DanceTeam.JoinAddreNameActivity;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.video.UploadVideoActivity;

/**
 * 作者：胖胖祥
 * 时间：2016/7/5 0005 上午 10:10
 * 功能模块：约战界面
 */
public class YuezhanActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yuezhan;
    }

    @Override
    protected void initViews() {

    }

    public void onClickYuezhan(View v) {

        if (MyApplication.getInstance().isLogin()) {//已登录

            startActivity(DekaronAcrivity.class);

        } else {

            Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();

        }

    }

    public void onClickWuDui(View v) {

        if (!MyApplication.getInstance().isLogin()) {

            Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(JoinAddreNameActivity.class);

    }

    public void onClickZhouSai(View v) {

        Toast.makeText(mActivity, "周赛暂未开放", Toast.LENGTH_LONG).show();

    }

    public void onClickYouYiSai(View v) {

        Toast.makeText(mActivity, "友谊赛暂未开放", Toast.LENGTH_LONG).show();


    }

    public void onClickYuezhanMessage(View v) {

        if (!MyApplication.getInstance().isLogin()) {

            Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
            return;
        }

        if (SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.adminDance, "").equals("0")) {

            Toast.makeText(mActivity, "没有舞队管理员权限", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(YueZhanMessageActivity.class);

    }


}
