package com.fanstech.mothersong.RankingList.tablayou;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fanstech.mothersong.R;


/**
 * 舞林导航条
 */
public class MyTabItem {

    public View getTabView(int i, String code) {

        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        mTvTitle = (TextView) view.findViewById(R.id.textView);

        if (code.equals("king")) {
            if (i == 0) {

                mTvTitle.setText("人气舞队");

            } else if (i == 1) {

                mTvTitle.setText("大赢家");

            } else if (i == 2) {

                mTvTitle.setText("人气天后");
            }
        }

        return view;
    }

    public MyTabItem(Context context) {
        this.context = context;
    }

    public void setTabTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    private Context context;

    TextView mTvTitle;
}
