package com.fanstech.mothersong.dekaron;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.JoinDanceAdapter;
import com.fanstech.mothersong.adapter.SpinnerAdapter;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.bean.FieldMessage;
import com.fanstech.mothersong.bean.WeatherMessage;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *作者：胖胖祥
 *时间：2016/7/4 0004 上午 9:07
 *功能模块：发起pk
 */
public class LaunchPKActivity extends BaseActivity {

    private Spinner spinner;
    private Button button;
    private String danceId;

    private List<FieldMessage> lists = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch_pk;
    }

    @Override
    protected void initViews() {

        danceId = getIntent().getStringExtra("danceId");
        button = (Button) findViewById(R.id.launck_button);
        button.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.homeCourt);

        FieldMessage message = new FieldMessage();
        message.setId("1");
        message.setName("1天");
        lists.add(message);

        FieldMessage message1 = new FieldMessage();
        message1.setId("3");
        message1.setName("3天");
        lists.add(message1);

        FieldMessage message2 = new FieldMessage();
        message2.setId("7");
        message2.setName("7天");
        lists.add(message2);

        spinner.setAdapter(new SpinnerAdapter(mActivity, lists));

//        String[] items = new String[] {"1天","3天","7天"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        switch (v.getId()){

            case R.id.launck_button:

                FieldMessage cu = (FieldMessage) spinner.getSelectedItem();
                Bundle bundle = new Bundle();
                bundle.putString("day",cu.getId());
                bundle.putString("danceId",danceId);
                startActivity(LaunchPKOneActivity.class,bundle);
                this.finish();

                break;

        }

    }

}
