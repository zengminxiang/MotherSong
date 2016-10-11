package com.fanstech.mothersong.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.utils.UrlConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 作者：胖胖祥
 * 时间：2016/6/14 0014 上午 10:57
 * 功能模块：忘记密码
 */
public class FrgetPassword extends BaseActivity{

    private EditText phone, code, pwd;
    private Button button, getcode;
    private ProgressBar loading;

    private String phonetext;//保存手机号码
    private final int ERROR = 400;
    private final int SENDSUCCESS = 1;//验证码发送成功
    private final int BUTTONFALSE = 2;//修改button验证码
    private final int BUTTONTRUE = 3;//修改button验证码
    private final int SUCCESS = 4;//验证码验证成
    private final int CODESUCCESS = 5;//验证码验证成

    private String uid;//用户id

    private int time = 60;// 发送验证码的时间
    private boolean state = true;//停止线程
    EventHandler eh;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_frget_password;
    }

    @Override
    protected void initViews() {

        phone = (EditText) findViewById(R.id.frget_phone);
        pwd = (EditText) findViewById(R.id.frget_pwd);
        code = (EditText) findViewById(R.id.frget_code);
        loading = (ProgressBar) findViewById(R.id.frget_load);
        button = (Button) findViewById(R.id.frget_submit);
        button.setOnClickListener(this);
        getcode = (Button) findViewById(R.id.frget_getcode);
        getcode.setOnClickListener(this);

        //mob
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                //回调完成
                if (result == SMSSDK.RESULT_COMPLETE) {

                    //提交验证码成功,和验证成功返回
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        //提交资料注册
                        handler.sendEmptyMessage(SUCCESS);

                    }

                    //获取验证码成功
                    else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                        handler.sendEmptyMessage(SENDSUCCESS);

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                        //返回支持发送验证码的国家列表

                    }
                } else {

                    ((Throwable) data).printStackTrace();
                    handler.sendEmptyMessage(CODESUCCESS);

                }
            }
        };

        SMSSDK.registerEventHandler(eh); //注册短信回调

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SENDSUCCESS:

                    loading.setVisibility(View.GONE);
                    phonetext = phone.getText().toString();
                    Toast.makeText(FrgetPassword.this, "成功发送", Toast.LENGTH_LONG).show();
                    reGetCode();

                    break;

                case BUTTONFALSE:

                    getcode.setBackgroundResource(R.color.white);
                    getcode.setClickable(false);
                    getcode.setText(time + "秒");

                    break;

                case BUTTONTRUE:

                    getcode.setText("获取验证码");
                    getcode.setClickable(true);
                    break;

                case SUCCESS:

                    //提交资料
                    FrgetPaw(uid, pwd.getText().toString());
                    break;
                case CODESUCCESS:
                    loading.setVisibility(View.GONE);
                    Toast.makeText(FrgetPassword.this, "验证码错误", Toast.LENGTH_LONG).show();
                    break;

                case ERROR:
                    loading.setVisibility(View.GONE);
                    Toast.makeText(FrgetPassword.this, "连接错误", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);


        switch (v.getId()) {

            //提交
            case R.id.frget_submit:
                if (TextUtils.isEmpty(pwd.getText().toString())) {
                    Toast.makeText(this, "输入密码", Toast.LENGTH_LONG).show();
                    return;
                }

                if (pwd.getText().toString().length() < 6) {
                    Toast.makeText(this, "密码要大于6位数", Toast.LENGTH_LONG).show();
                    return;
                }
                //判断是否输入为空
                if (TextUtils.isEmpty(code.getText().toString())) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                JudgeCode();
                break;
            //获取验证码
            case R.id.frget_getcode:
                isPhone(phone.getText().toString());
                break;

        }

    }



    //等待60秒重新获取验证码
    public void reGetCode() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                while (state) {

                    try {

                        if (time == 1) {

                            handler.sendEmptyMessage(BUTTONTRUE);
                            time = 60;
                            break;

                        }

                        time--;
                        handler.sendEmptyMessage(BUTTONFALSE);

                        Thread.sleep(1000);

                    } catch (InterruptedException e) {

                        e.printStackTrace();

                    }

                }

            }
        }.start();

    }

    /**
     * 修改密码
     *
     * @param userid 用户id
     * @param pwd    用户密码
     */
    public void FrgetPaw(String userid, String pwd) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("userId", userid);
        mMap.put("password", pwd);

        RewriteRequest frget = new RewriteRequest(UrlConfig.FRGET, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);
        frget.setTag("frget");
        MyApplication.getHttpQueues().add(frget);
        frget.setShouldCache(true); // 控制是否缓存

    }

    //判断验证码
    public void JudgeCode() {

        //判断是否输入为空
        if (TextUtils.isEmpty(code.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return;
        }

        loading.setVisibility(View.VISIBLE);
        SMSSDK.submitVerificationCode("86", phonetext, code.getText().toString());//验证验证码

    }

    /**
     * 判断手机号是否已经注册
     *
     * @param number 手机号码
     */
    public void isPhone(String number) {

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("account", number);

        RewriteRequest crphone = new RewriteRequest(UrlConfig.ISREGISTER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "" + jsonObject.toString());

                try {

                    String success = jsonObject.getString("state");//true表示可以注册，false表示已经注册

                    if (!success.equals("400")) {

                        uid = jsonObject.getString("obj");
                        SMSSDK.getVerificationCode("86", phone.getText().toString());

                    } else {

                        Toast.makeText(FrgetPassword.this, "手机号码未注册", Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);

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

        crphone.setTag("isphone");
        MyApplication.getHttpQueues().add(crphone);
        crphone.setShouldCache(true); // 控制是否缓存

    }

}
