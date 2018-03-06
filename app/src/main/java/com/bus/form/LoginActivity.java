package com.bus.form;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 闫江南 on 2016/10/14.
 */
public class LoginActivity extends AppCompatActivity {
    private String uname;
    private String upwd;
    @InjectView(R.id.login_phone) EditText bus_phone;
    @InjectView(R.id.login_password)EditText bus_password;
    @InjectView(R.id.login_button)AppCompatButton bus_login;
    @InjectView(R.id.link_register)TextView regis_link;
    @InjectView(R.id.forget_password)TextView forget_pas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        initUi();
    }
private void initUi(){
    //注册页面跳转
    regis_link.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
          finish();

        }
    });

    //找回密码页面跳转
    forget_pas.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Intent(LoginActivity.this,findPasswordActivity.class);
            finish();
        }
    });

    //登录按钮监听
    bus_login.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setAlpha(150);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().setAlpha(220);
                uname = bus_phone.getText().toString();
                upwd = bus_password.getText().toString();
                if (check(uname, upwd)) {
                    do_login(uname, upwd);
                }
            }
            return true;
        }
    });


}
    private boolean check(String uname,String upwd){
        //手机校验
        if(uname.isEmpty()||!isMobileNum(uname)){
        bus_phone.setError("手机格式不正确");
            return false;
        }
        //密码校验
        if(upwd.length()<6||upwd.length()>12){

            bus_password.setError("密码请设置在6-12位");
            return false;
        }
        return true;

    }
    //手机正则校验
    private boolean isMobileNum(String uname){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(uname);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
    //作登录
    private  void do_login(String uname,String upwd){
//进行登录操作，成功进入MainActivity

    }

}
