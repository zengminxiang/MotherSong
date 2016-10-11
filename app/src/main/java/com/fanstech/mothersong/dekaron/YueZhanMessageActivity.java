package com.fanstech.mothersong.dekaron;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.RankingList.tablayou.MyTabItem;
import com.fanstech.mothersong.RankingList.tablayou.RankingFragmentAdapter;
import com.fanstech.mothersong.dekaron.tablayout.LaunchFragmentAdapter;
import com.fanstech.mothersong.dekaron.tablayout.LaunchTabItme;
import com.fanstech.mothersong.public_class.BaseActivity;

/**
 *作者：胖胖祥
 *时间：2016/7/6 0006 上午 10:14
 *功能模块：约战消息列表
 */
public class YueZhanMessageActivity extends BaseActivity {

    private static final String POSITION = "position";
    private LaunchFragmentAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    LaunchTabItme[] tabs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_yue_zhan_message;
    }

    @Override
    protected void initViews() {

        //左右滑动
        viewPager = (ViewPager) findViewById(R.id.yuezhan_viewpager);
        pagerAdapter = new LaunchFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        //设置ViewPager预加载3个页面，解决第3个页面初始点击崩溃的Bug
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) findViewById(R.id.yuezhan_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabs = new LaunchTabItme[tabLayout.getTabCount()];

        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tabs[i] = new LaunchTabItme(this);
            tab.setCustomView(tabs[i].getTabView(i, "launch"));

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