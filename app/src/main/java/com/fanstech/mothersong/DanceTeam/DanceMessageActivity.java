package com.fanstech.mothersong.DanceTeam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;

/**
 *作者：胖胖祥
 *时间：2016/6/30 0030 上午 9:47
 *功能模块：舞队消息
 */
public class DanceMessageActivity extends BaseActivity {

    private TextView dance_sq_message;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dance_message;
    }

    @Override
    protected void initViews() {

        dance_sq_message = (TextView) findViewById(R.id.dance_sq_message);
        dance_sq_message.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.dance_sq_message:
                startActivity(DanceSqMessageActivity.class);
                break;

        }

    }
}
