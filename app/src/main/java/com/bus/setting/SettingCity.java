package com.bus.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bus.form.R;

import java.lang.reflect.Field;

/**
 * Created by John Yan on 1/31/2017.
 */

public class SettingCity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_city);
        initView();
    }

    private void initView() {
        SearchView mSearch= (SearchView) findViewById(R.id.search_city);
        if(mSearch==null){
            return;
        }
        else{
//获取到TextView的ID
            int id = mSearch.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
//获取到TextView的控件
            TextView textView = (TextView) mSearch.findViewById(id);
//设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
//设置字体颜色
            textView.setTextColor(getResources().getColor(R.color.cardview_shadow_start_color));
//设置提示文字颜色
            textView.setHintTextColor(getResources().getColor(R.color.cardview_shadow_start_color));
            textView.setPadding(0,12,0,0);


            try {        //--拿到字节码
                Class<?> argClass = mSearch.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearch);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            ImageView back= (ImageView) findViewById(R.id.back_before);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }
}
