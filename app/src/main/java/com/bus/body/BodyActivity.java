package com.bus.body;

import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bus.form.R;
import com.bus.fragment.BodyHomeFragment;
import com.bus.fragment.BodyLocFragment;
import com.bus.fragment.BodyMeFragment;




public class BodyActivity extends FootnavActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radiogroup;
    private FrameLayout bodyframe;

    private Context context;
    BodyHomeFragment homeFragment;
    BodyLocFragment locFragment;
    BodyMeFragment meFragment;
    FragmentManager fragmentManager=getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        initView();
    }

    private void initView() {
        radiogroup= (RadioGroup) findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(this);

        radiogroup.check(R.id.footer_navhome);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){

            case R.id.footer_navhome:

                FragmentTransaction transaction1=fragmentManager.beginTransaction();
                homeFragment=new BodyHomeFragment();

                transaction1.replace(R.id.body_frame,homeFragment );
                transaction1.commit();

                break;

            case  R.id.footer_navloc:

                FragmentTransaction transaction2=fragmentManager.beginTransaction();
                locFragment=new BodyLocFragment();
                transaction2.replace(R.id.body_frame,locFragment );
                transaction2.commit();

                break;
            case  R.id.footer_navme:

                FragmentTransaction transaction3=fragmentManager.beginTransaction();
                meFragment=new BodyMeFragment();
                transaction3.replace(R.id.body_frame,meFragment );
                transaction3.commit();
                break;
        }

    }
}
