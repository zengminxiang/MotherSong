package com.fanstech.mothersong.dekaron.tablayout;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanstech.mothersong.RankingList.fragment.BigWinnerFragment;
import com.fanstech.mothersong.RankingList.fragment.DayslaterFragment;
import com.fanstech.mothersong.RankingList.fragment.PopularityFragment;
import com.fanstech.mothersong.dekaron.fragment.CoverLaunchMessageFragment;
import com.fanstech.mothersong.dekaron.fragment.LaunchMessageFragment;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class LaunchFragmentAdapter extends FragmentPagerAdapter{


    final int PAGE_COUNT = 2;
    private Context context;

    private LaunchMessageFragment lmf;
    private CoverLaunchMessageFragment clmf;


    public LaunchFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        lmf = new LaunchMessageFragment();
        clmf = new CoverLaunchMessageFragment();
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){

            return lmf;

        }else{

            return clmf;

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
