package com.fanstech.mothersong.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UpdateManager;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.login.LoginListener;

/**
 *作者：胖胖祥
 *时间：2016/6/28 0028 下午 2:56
 *功能模块：设置
 */
public class SetUpActivity extends BaseActivity {

    private TextView set_about,set_common,set_ToUpdate;
    private Button out_login;
    private ProgressDialog progressDialog = null;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initViews() {

        // 判断版本联网
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                    .build());
        }

        setTitle("关于我们");
        set_about = (TextView) findViewById(R.id.set_about);
        set_about.setOnClickListener(this);
        out_login = (Button) findViewById(R.id.out_login);
        out_login.setOnClickListener(this);
        set_common = (TextView) findViewById(R.id.set_common);
        set_common.setOnClickListener(this);
        set_ToUpdate = (TextView) findViewById(R.id.set_ToUpdate);
        set_ToUpdate.setOnClickListener(this);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    UpdateManager manager = new UpdateManager(SetUpActivity.this,1);
                    // 检查软件更新
                    manager.checkUpdate();
                    progressDialog.dismiss();

                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.set_about:
            startActivity(AboutActivity.class);
            break;

            case R.id.out_login:
                OutLogin();
                SharePreferenceUtil.getInstance(mActivity).clear();
                MyApplication.getInstance().setLogin(false);
                Intent data = new Intent();
                data.putExtra("res", true);
                setResult(RESULT_OK, data);
                finish();

                break;

            case R.id.set_common:

                startActivity(CommonProblemActivity.class);
                break;

            //版本更新
            case R.id.set_ToUpdate:

                progressDialog = ProgressDialog.show(this, "", "正在检查更新", true);
                handler.sendEmptyMessage(1);

                break;

        }

    }

    /**
     * 退出登录
     */
    public void OutLogin(){

        mCommSDK.logout(mActivity, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int i, CommUser commUser) {

                Log.e("ss","sss"+i);

            }
        });

    }

}
