package com.bus.form;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bus.tools.FirstLaunchTool;

public class FlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断：如果是第一次启动，关闭当前Activity，启动GuideActivity，否则等待几秒进入MainActivity
        if(FirstLaunchTool.isOne(this)) {
            startActivity(new Intent(this , GuideActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_flash);
            // 几秒后自己关闭，跳转到主界面
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              startActivity(new Intent(FlashActivity.this, MainActivity.class));
                                              finish();
                                          }
                                      }
                    ,
                    3000
            );
        }
    }
}
