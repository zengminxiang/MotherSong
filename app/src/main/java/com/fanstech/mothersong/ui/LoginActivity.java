package com.fanstech.mothersong.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.nets.responses.PortraitUploadResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.SMSSDK;


/**
 * 作者：胖胖祥
 * 时间：2016/6/12 0012 下午 3:44
 * 功能模块：登录界面
 */
public class LoginActivity extends BaseActivity implements PlatformActionListener {

    private EditText login_phone, login_pwd;
    private Button login_button;
    private TextView login_register, login_forget;
    private ProgressBar login_load;

    private String nickname,id,head;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {

        setTitle("登录");
        SMSSDK.initSDK(this, "f419fc6ff1dd", "a203059bdd04b5b266f5d3311278678f", true);
        ShareSDK.initSDK(this);
        mActivity = this;
        login_pwd = (EditText) this.findViewById(R.id.login_pwd);//密码控件
        login_phone = (EditText) this.findViewById(R.id.login_phone);//手机号
        login_button = (Button) this.findViewById(R.id.login_button);//登录按钮
        login_button.setOnClickListener(this);
        login_register = (TextView) this.findViewById(R.id.login_register);//注册按钮
        login_register.setOnClickListener(this);
        login_forget = (TextView) this.findViewById(R.id.login_forget);//忘记密码
        login_forget.setOnClickListener(this);
        login_load = (ProgressBar) findViewById(R.id.login_load);//加载框

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    LoginUcoms(nickname, id, head);

                    break;
                case 2:

                    AlertDialog("昵称已存在，请重新修改");

                    break;

                case 3:

                    //授权成功
                    Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    Object[] objs = (Object[]) msg.obj;
                    Platform platform = (Platform) objs[0];
                    HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                    Log.e("用户信息", res.toString());
                    nickname = platform.getDb().getUserName();
                    id = platform.getDb().getUserId();
                    head = platform.getDb().getUserIcon();
                    LoginWeixin();

                    break;

                case 4:
                    //授权失败
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                    break;

                case 5:
                    Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
                    break;

                case 6:

                    final EditText inputServer = new EditText(mActivity);
                    inputServer.setHint("输入昵称");
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("昵称已被使用").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("修改昵称", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            if(TextUtils.isEmpty(inputServer.getText().toString())){

                                Toast.makeText(mActivity,"请输入昵称",Toast.LENGTH_LONG).show();
                                return;

                            }

                            nickname = inputServer.getText().toString();

                            LoginWeixin();

                        }
                    });
                    builder.show();

                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            //登录按钮
            case R.id.login_button:

                login_load.setVisibility(View.VISIBLE);
                Login(login_phone.getText().toString(), login_pwd.getText().toString());
                break;
            //注册按钮
            case R.id.login_register:

                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);

                break;
            //忘记密码
            case R.id.login_forget:
                Intent intent2 = new Intent(this, FrgetPassword.class);
                startActivity(intent2);
                break;

        }

    }

    /**
     * 请求登录
     *
     * @param phone 手机号码
     * @param pwd   密码
     */
    public void Login(String phone, String pwd) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("account", phone);
        mMap.put("password", pwd);

        RewriteRequest login = new RewriteRequest(UrlConfig.LOGIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);
                try {

                    String state = jsonObject.getString("state");

                    if (state.equals("200")) {

                        //登录成功后获取用户的信息
                        getUserMessage(jsonObject.getString("obj"));
                        getUserDanceAdmin(jsonObject.getString("obj"));

                    } else {

                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                        login_load.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                login_load.setVisibility(View.GONE);
            }
        }, mMap);
        login.setTag("login");
        MyApplication.getHttpQueues().add(login);
        login.setShouldCache(true); // 控制是否缓存
    }

    /**
     * qq登录
     *
     * @param v
     */
    public void onClickQQ(View v) {

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        authorize(qzone);

    }

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform plat) {

        plat.setPlatformActionListener(this);
        //关闭SSO授权
        plat.SSOSetting(false);
        Log.e("tan6458", "        plat.SSOSetting(true);");
        plat.showUser(null);
        Log.e("tan6458", "          plat.showUser(null);");

    }

    /**
     * 微信登录
     *
     * @param v
     */
    public void onClickweixin(View v) {

        Log.e("ss", "ss");
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        authorize(wechat);

    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        if (i == Platform.ACTION_USER_INFOR) {

            Log.e("用户信息", "ssss");
            Message msg = new Message();
            msg.what = 3;
            msg.obj = new Object[]{platform, hashMap};
            handler.sendMessage(msg);

        } else {

            Log.e("ss", "ss");

        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(4);
        } else {
            Log.e("授权失败", "取消授权");
        }
        throwable.printStackTrace();

    }

    @Override
    public void onCancel(Platform platform, int i) {

        if (i == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(5);
        } else {

            Log.e("取消授权", "取消授权");

        }

    }

    /**
     * 微信登录
     *
     * @param
     */
    public void LoginWeixin() {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("nickname", nickname);
        mMap.put("head", head);
        mMap.put("openId", id);

        RewriteRequest weixin = new RewriteRequest(UrlConfig.WEIXIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject);

                try {

                    String state = jsonObject.getString("state");

                    if (state.equals("200")) {

                        //登录成功后获取用户的信息
                        getUserMessage(jsonObject.getString("obj"));
                        getUserDanceAdmin(jsonObject.getString("obj"));

                    } else {

                        //跳转到填写用户名
                        handler.sendEmptyMessage(6);
                        login_load.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                login_load.setVisibility(View.GONE);

            }
        }, mMap);

        weixin.setTag("weixin");
        MyApplication.getHttpQueues().add(weixin);
        weixin.setShouldCache(true);

    }

    /**
     * 判断该用户是否是舞队管理员
     */
    public void getUserDanceAdmin(String token){

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", token);
        RewriteRequest admin = new RewriteRequest(UrlConfig.ADMIN, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "是否是管理员" + jsonObject);

                try {

                    String state = jsonObject.getString("obj");

                    //0不是管理员，1代表是管理员
                    if (state.equals("0")) {
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.adminDance, "0");
                    } else {
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.adminDance, "1");
                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                login_load.setVisibility(View.GONE);
            }
        }, mMap);

        admin.setTag("admin");
        MyApplication.getHttpQueues().add(admin);
        admin.setShouldCache(true);


    }

    /**
     * 获取用户的信息
     *
     * @param token
     */
    public void getUserMessage(final String token) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("token", token);

        RewriteRequest usermessage = new RewriteRequest(UrlConfig.USERMESSAGE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    if (jsonObject.getString("state").equals("200")) {

                        JSONObject user = new JSONObject(jsonObject.getString("obj"));
                        Log.e("jsonObject", "jsonObject" + jsonObject);
                        //保存用户资料
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.token, token);
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.id, user.getString("id"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.nickname, user.getString("nickname"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.account, user.getString("account"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.password, user.getString("password"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.introduce, user.getString("introduce"));

                        if (user.getString("head").equals("")) {
                            SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.head, "http://120.25.172.152/head/default.png");
                        } else {
                            SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.head, "http://120.25.172.152"+user.getString("head"));
                        }

                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.grade, user.getString("grade"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.province, user.getString("province"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.city, user.getString("city"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.openid, user.getString("openid"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.accountType, user.getString("accountType"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.state, user.getString("state"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.time, user.getString("time"));
                        SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.timeStr, user.getString("timeStr"));
                        MyApplication.getInstance().setLogin(true);
                        nickname = user.getString("nickname");
                        id = user.getString("id");
                        head = user.getString("head");
                        LoginUcoms(nickname, id, head);

                    } else {

                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                        login_load.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                login_load.setVisibility(View.GONE);

            }
        }, mMap);

        usermessage.setTag("usermessage");
        MyApplication.getHttpQueues().add(usermessage);
        usermessage.setShouldCache(true); // 控制是否缓存

    }

    /**
     * 登录友盟微社区
     */
    public void LoginUcoms(String name, String id, final String head) {

        final CommUser user = new CommUser();
        user.name = name;
        user.id = id;
        mCommSDK.loginToUmengServerBySelfAccount(mActivity, user, new LoginListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int stCode, CommUser commUser) {

                Log.e("登录","登录"+stCode);

                if (ErrorCode.NO_ERROR == stCode) {
                    //在此处可以跳转到任何一个你想要的activity
                    String s;
                    if (head.equals("")) {

                        s = "http://120.25.172.152/head/default.png";

                    } else {

                        s = "http://120.25.172.152"+head;

                    }
                    UpdateHead(s);

                }else if(ErrorCode.ERR_CODE_USER_NAME_ILLEGAL_CHAR == stCode){

                    AlertDialog("用户名存在非法字符");

                }

            }
        });

    }


    /**
     * 修改微社区用户头像
     */
    public void UpdateHead(String head) {

        mCommSDK.updateUserProtrait(head, new Listeners.SimpleFetchListener<PortraitUploadResponse>() {
            @Override
            public void onComplete(PortraitUploadResponse portraitUploadResponse) {

                SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.nickname, nickname);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                login_load.setVisibility(View.GONE);
                Intent data = new Intent();
                data.putExtra("res", true);
                setResult(RESULT_OK, data);
                finish();

            }
        });

    }


    public void AlertDialog(String text){

                final EditText inputServer = new EditText(this);
                AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
                builder.setTitle(text);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(inputServer);
                builder.setPositiveButton("修改昵称", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.isEmpty(inputServer.getText().toString())){

                            Toast.makeText(mActivity,"请输入昵称",Toast.LENGTH_LONG).show();
                            return;

                        }
                        nickname = inputServer.getText().toString();
                        AlterUsername(nickname);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog=builder.create();
                dialog.show();

    }

    /**
     * 修改昵称
     */
    public void AlterUsername(String nickname) {

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("nickname", nickname);

        RewriteRequest alter = new RewriteRequest(UrlConfig.ALTERNAME, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());
                try {

                    if(jsonObject.getString("state").equals("200")){

                        handler.sendEmptyMessage(1);

                    }else{
                        handler.sendEmptyMessage(2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        alter.setTag("alter");
        MyApplication.getHttpQueues().add(alter);
        alter.setShouldCache(true); // 控制是否缓存


    }



}

