package com.fanstech.mothersong.dekaron.tablayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanstech.mothersong.R;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class LaunchTabItme {


    public View getTabView(int i, String code) {

        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        mTvTitle = (TextView) view.findViewById(R.id.textView);

        if (code.equals("launch")) {
            if (i == 0) {

                mTvTitle.setText("发起pk列表");

            } else if (i == 1) {

                mTvTitle.setText("被发起pk列表");

            }
        }

        return view;
    }

    public LaunchTabItme(Context context) {
        this.context = context;
    }

    public void setTabTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    private Context context;

    TextView mTvTitle;

}
