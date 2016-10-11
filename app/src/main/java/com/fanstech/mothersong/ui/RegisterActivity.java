package com.fanstech.mothersong.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.AllUtil;
import com.fanstech.mothersong.utils.UrlConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 作者：胖胖祥
 * 时间：2016/6/13 0013 下午 6:46
 * 功能模块：注册界面
 */
public class RegisterActivity extends BaseActivity {


    private EditText phone, pwd, code;
    private Button getCode, reg_submit;
    private ProgressBar reg_load;
    private RadioButton radioButton;

    EventHandler eh;

    private String phonetext;//保存手机号码

    private final int SENDSUCCESS = 1;//验证码发送成功
    private final int BUTTONFALSE = 2;//修改button验证码
    private final int BUTTONTRUE = 3;//修改button验证码
    private final int SUCCESS = 4;//验证码验证成
    private final int CODESUCCESS = 5;//验证码验证成
    private final int ERROR = 0;// 错误

    private int time = 60;// 发送验证码的时间
    private boolean state = true;//停止线程

    int num = 5;// 限制的最大字数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {

        setTitle("注册");
        phone = (EditText) this.findViewById(R.id.register_phone);
        pwd = (EditText) this.findViewById(R.id.register_pwd);
        //密码框不能换行
        pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        code = (EditText) this.findViewById(R.id.register_code);
        //输入用户名框不能换行
//        register_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
//            }
//        });

        reg_submit = (Button) this.findViewById(R.id.reg_submit);
        reg_submit.setOnClickListener(this);
        getCode = (Button) this.findViewById(R.id.register_getcode);
        getCode.setOnClickListener(this);
        radioButton = (RadioButton) this.findViewById(R.id.radioButton);
        reg_load = (ProgressBar) this.findViewById(R.id.reg_load);

//        register_name.addTextChangedListener(new TextWatcher() {
//
//            private CharSequence temp;
//
//            private int selectionStart;
//
//            private int selectionEnd;
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                temp = s;
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//                int number = num - s.length();
//
//                selectionStart = register_name.getSelectionStart();
//
//                selectionEnd = register_name.getSelectionEnd();
//
//                if (temp.length() > num) {
//
//                    s.delete(selectionStart - 1, selectionEnd);
//
//                    int tempSelection = selectionEnd;
//
//                    register_name.setText(s);
//
//                    register_name.setSelection(tempSelection);// 设置光标在最后
//
//                }
//
//            }
//        });

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

                    reg_load.setVisibility(View.GONE);
                    phonetext = phone.getText().toString();
                    Toast.makeText(RegisterActivity.this, "成功发送", Toast.LENGTH_LONG).show();
                    reGetCode();

                    break;

                case BUTTONFALSE:

                    getCode.setBackgroundResource(R.color.white);
                    getCode.setClickable(false);
                    getCode.setText(time + "秒");

                    break;

                case BUTTONTRUE:

                    getCode.setText("获取验证码");
                    getCode.setClickable(true);
                    break;

                case SUCCESS:

                    //提交资料
                    register();
                    break;
                case CODESUCCESS:
                    reg_load.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_LONG).show();
                    break;

                case ERROR:
                    reg_load.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "连接错误", Toast.LENGTH_LONG).show();
                    break;

                case 6:
                    dialog();
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            //获取验证码
            case R.id.register_getcode:

                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(this, "输入手机号码", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!new AllUtil().checkPhone(phone.getText().toString())) {
                    Toast.makeText(this, "手机号码输入有误", Toast.LENGTH_LONG).show();
                    return;
                }

                reg_load.setVisibility(View.VISIBLE);
                isPhone(phone.getText().toString());
                break;

            //提交注册
            case R.id.reg_submit:

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
                if (!radioButton.isChecked()) {
                    Toast.makeText(this, "需要同意协议", Toast.LENGTH_LONG).show();
                    return;
                }

                JudgeCode();//先去验证验证码
                break;

        }

    }

    //注册
    public void register() {

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("account", phonetext);
        mMap.put("password", pwd.getText().toString());

        RewriteRequest cr = new RewriteRequest(UrlConfig.REGISTER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());

                try {

                    String success = jsonObject.getString("state");

                    if (success.equals("200")) {

                        //登录成功后获取用户的信息
                        handler.sendEmptyMessage(6);

                    } else {

                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                        RegisterActivity.this.finish();

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

        cr.setTag("register");
        MyApplication.getHttpQueues().add(cr);
        cr.setShouldCache(true); // 控制是否缓存

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

    //判断验证码
    public void JudgeCode() {

        //判断是否输入为空
        if (TextUtils.isEmpty(code.getText().toString())) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return;
        }

        reg_load.setVisibility(View.VISIBLE);
        SMSSDK.submitVerificationCode("86", phonetext, code.getText().toString());//验证验证码

    }

    //判断手机是否已经注册了
    public void isPhone(String number) {

        Log.e("进来了", "进来了");

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("account", number);

        RewriteRequest crphone = new RewriteRequest(UrlConfig.ISREGISTER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "" + jsonObject.toString());

                try {

                    String success = jsonObject.getString("state");//true表示可以注册，false表示已经注册

                    if (success.equals("400")) {

                        SMSSDK.getVerificationCode("86", phone.getText().toString());

                    } else {

                        Toast.makeText(RegisterActivity.this, "手机号码已经注册", Toast.LENGTH_LONG).show();
                        reg_load.setVisibility(View.GONE);

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

    @Override
    protected void onStop() {

        super.onStop();
        MyApplication.getHttpQueues().cancelAll("register");
        MyApplication.getHttpQueues().cancelAll("isphone");//关闭网络连接请求

    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("注册成功");

        builder.setTitle("邀请好友加入妈妈颂");
        builder.setPositiveButton("邀请", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showShare();
                RegisterActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.create().show();
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
    oks.setText("我正在使用妈妈颂app，挺好玩的，大家快来注册哦！");
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


}
