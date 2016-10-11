package com.fanstech.mothersong.RankingList;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.RankingList.tablayou.MyTabItem;
import com.fanstech.mothersong.RankingList.tablayou.RankingFragmentAdapter;
import com.fanstech.mothersong.public_class.BaseActivity;

/**
 *作者：胖胖祥
 *时间：2016/6/30 0030 下午 2:24
 *功能模块：排行榜
 */
public class RankingListActivity extends BaseActivity {

    //TabLayout
    private static final String POSITION = "position";
    private RankingFragmentAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    MyTabItem[] tabs;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    protected void initViews() {

        setTitle("排行榜");

        //左右滑动
        viewPager = (ViewPager) findViewById(R.id.ranking_viewpager);
        pagerAdapter = new RankingFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        //设置ViewPager预加载3个页面，解决第3个页面初始点击崩溃的Bug
        viewPager.setOffscreenPageLimit(3);
        tabLayout = (TabLayout) findViewById(R.id.ranking_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabs = new MyTabItem[tabLayout.getTabCount()];

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tabs[i] = new MyTabItem(this);
            tab.setCustomView(tabs[i].getTabView(i,"king"));

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //滑动end


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }


}
