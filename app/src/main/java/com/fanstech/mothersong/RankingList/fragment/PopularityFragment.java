package com.fanstech.mothersong.RankingList.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.public_class.BaseFragment;

/**
 *作者：胖胖祥
 *时间：2016/6/30 0030 下午 2:31
 *功能模块：人气排行榜
 */
public class PopularityFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_popularity, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected void initView() {

    }

}
