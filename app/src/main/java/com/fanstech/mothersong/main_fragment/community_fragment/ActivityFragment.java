package com.fanstech.mothersong.main_fragment.community_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseFragment;

/**
 *作者：胖胖祥
 *时间：2016/7/8 0008 下午 2:34
 *功能模块：活动
 */
public class ActivityFragment extends BaseFragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.community_acticity, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    protected void initView() {

    }
}
