package com.fanstech.mothersong.RankingList.tablayou;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanstech.mothersong.RankingList.fragment.BigWinnerFragment;
import com.fanstech.mothersong.RankingList.fragment.DayslaterFragment;
import com.fanstech.mothersong.RankingList.fragment.PopularityFragment;

/**
 * Created by Administrator on 2016/1/26 0026.
 */
public class RankingFragmentAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private Context context;

    private PopularityFragment pf;
    private BigWinnerFragment bf;
    private DayslaterFragment df;


    public RankingFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        pf = new PopularityFragment();
        bf = new BigWinnerFragment();
        df = new DayslaterFragment();
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            return pf;

        }else if(position == 1){

            return bf;

        }else{
            return df;

        }



    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return null;

    }

}
