package com.fanstech.mothersong.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseActivity;
/**
 *作者：胖胖祥
 *时间：2016/6/29 0029 上午 10:59
 *功能模块：意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    private Button submit_feedback;
    private EditText feedback_context;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initViews() {

        submit_feedback = (Button) findViewById(R.id.submit_feedback);
        submit_feedback.setOnClickListener(this);
        feedback_context = (EditText) findViewById(R.id.feedback_context);


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.submit_feedback:

                if(TextUtils.isEmpty(feedback_context.getText().toString())){
                    Toast.makeText(mActivity,"输入意见",Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(mActivity,"提交成功",Toast.LENGTH_LONG).show();
                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();
                break;

        }

    }
}
