package com.fanstech.mothersong.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedsResponse;

/**
 *作者：胖胖祥
 *时间：2016/6/30 0030 上午 9:08
 *功能模块：消息界面
 */
public class MessageActivity extends BaseActivity {

    private TextView message_comment,message_zan;
    
    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initViews() {

        message_comment = (TextView) findViewById(R.id.message_comment);
        message_comment.setOnClickListener(this);
        message_zan = (TextView) findViewById(R.id.message_zan);
        message_zan.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.message_comment:

                startActivity(CommentListActivity.class);

                break;

            case R.id.message_zan:

                startActivity(ZanMessageActivity.class);

                break;

        }

    }
}
