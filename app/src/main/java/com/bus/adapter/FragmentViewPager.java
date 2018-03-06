package com.bus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by John Yan on 1/3/2017.
 */

public class FragmentViewPager extends FragmentPagerAdapter {
    //需要数据源成员变量
private List<Fragment> data;

    public FragmentViewPager(FragmentManager fm,List<Fragment> data) {
        super(fm);
        this.data=data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
