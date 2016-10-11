package com.fanstech.mothersong.ui;

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
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.utils.L;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.listeners.Listeners;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 作者：胖胖祥
 * 时间：2016/7/14 0014 下午 3:42
 * 功能模块：修改昵称
 */
public class AlterUserActivity extends BaseActivity {

    private EditText editText;
    private Button submit;
    private ProgressBar load;


    private String code;
    private final int ONE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alter_user;
    }

    @Override
    protected void initViews() {

        load = (ProgressBar) this.findViewById(R.id.login_load);
        editText = (EditText) this.findViewById(R.id.alter_user_name);
        editText.setText(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname, ""));
        submit = (Button) this.findViewById(R.id.submitname);
        submit.setOnClickListener(this);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:

                    load.setVisibility(View.VISIBLE);

                    break;
                case 2:

                    Toast.makeText(mActivity,"修改成功",Toast.LENGTH_LONG).show();
                    SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.nickname,editText.getText().toString());
                    AlterUmeng();
                    mActivity.finish();

                    break;
                case 3:

                    Toast.makeText(mActivity,"昵称已存在，修改失败",Toast.LENGTH_LONG).show();
                    load.setVisibility(View.GONE);
                    break;

            }

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.submitname:

                if(TextUtils.isEmpty(editText.getText().toString())){

                    Toast.makeText(mActivity,"昵称不能为空",Toast.LENGTH_LONG).show();
                    return;

                }else if (SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname, "").equals(editText.getText().toString())) {

                    this.finish();

                }else{

                    handler.sendEmptyMessage(1);
                    AlterUsername();

                }

                break;

        }

    }

    /**
     * 修改昵称
     */
    public void AlterUsername() {

        HashMap<String, String> mMap = new HashMap<String, String>();
        mMap.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
        mMap.put("nickname", editText.getText().toString());

        RewriteRequest alter = new RewriteRequest(UrlConfig.ALTERNAME, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonObject" + jsonObject.toString());
                try {

                    if(jsonObject.getString("state").equals("200")){

                        handler.sendEmptyMessage(2);

                    }else{
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
        }, mMap);

        alter.setTag("alter");
        MyApplication.getHttpQueues().add(alter);
        alter.setShouldCache(true); // 控制是否缓存


    }

    /**
     * 修改友盟昵称
     */
    public void AlterUmeng(){

        CommUser user = CommConfig.getConfig().loginedUser;
        user.name = editText.getText().toString();
        mCommSDK.updateUserProfile(user, new Listeners.CommListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(com.umeng.comm.core.nets.Response response) {

            }
        });

    }

}
