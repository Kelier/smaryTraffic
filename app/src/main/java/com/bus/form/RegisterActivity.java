package com.bus.form;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity {
    private String regisname;
    private  String regispass;
    private String check_word;
    @InjectView(R.id.register_button)AppCompatButton regis_btn;
    @InjectView(R.id.check_btn)Button check_btn;
    @InjectView(R.id.register_phone)EditText regis_phone;
    @InjectView(R.id.register_password)EditText regis_pass;
    @InjectView(R.id.register_checkword)EditText regis_check;
    @InjectView(R.id.link_login)TextView link_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        initUi();
    }

    private void initUi() {
        //登录页面跳转
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                finish();
            }
        });


        //注册按钮监听
        regis_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(150);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(220);
                    regisname = regis_phone.getText().toString();
                    regispass = regis_pass.getText().toString();
                    check_word = regis_check.getText().toString();
                    if (check(regisname, regispass, check_word)) {
                        do_register(regisname, regispass,check_word);
                    }
                }
                return true;
            }

            private void do_register(String regisname, String regispass, String check_word) {
                //...做注册
            }

            private boolean check(String regisname, String regispass, String check_word) {
                //手机校验
                if(regisname.isEmpty()||!isMobileNum(regisname)){
                    regis_phone.setError("手机格式不正确");
                    return false;
                }
                //密码校验
                if(regispass.length()<6||regispass.length()>12){

                    regis_pass.setError("密码请设置在6-12位");
                    return false;
                }
                if(check_word.length()==0) {
                    regis_check.setError("验证码不能为空");
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

        });

    }
}
