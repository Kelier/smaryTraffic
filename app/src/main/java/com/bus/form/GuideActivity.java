package com.bus.form;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bus.adapter.FragmentViewPager;
import com.bus.fragment.GuideFragment1;
import com.bus.fragment.GuideFragment2;
import com.bus.fragment.GuideFragment3;
import com.bus.tools.FirstLaunchTool;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initViewPager();
    }

    public void initViewPager(){
        List<Fragment> data=new ArrayList<>();
        data.add(new GuideFragment1());
        data.add(new GuideFragment2());
        data.add(new GuideFragment3());
        FragmentPagerAdapter adapter=new FragmentViewPager(getSupportFragmentManager(),data);
        ViewPager viewPager=(ViewPager) findViewById(R.id.guide_viewpager);
        viewPager.setAdapter(adapter);


    }
    protected void onDestroy(){
    super.onDestroy();
        FirstLaunchTool.saveOne(this);

    }

}
