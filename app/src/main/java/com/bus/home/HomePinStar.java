package com.bus.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bus.form.R;


/**
 * Created by John Yan on 3/23/2017.
 */

public class HomePinStar extends AppCompatActivity {
    ImageView back;
    RelativeLayout nav_my;
    RelativeLayout nav_go;
    EditText destination;
    RelativeLayout push;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_pin);
        initUI();
    }
    private void initUI() {
        back= (ImageView) findViewById(R.id.back_before);
        nav_my= (RelativeLayout) findViewById(R.id.nav_my);
        nav_go= (RelativeLayout) findViewById(R.id.nav_go);
        destination= (EditText) findViewById(R.id.search_destination);
        destination.setHint("输入地点");
        push=(RelativeLayout)findViewById(R.id.Push_Tip);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nav_my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //broadcast广播回传选定的地址
            }
        });
        nav_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePinStar.this,MapPin.class));

            }
        });
        destination.addTextChangedListener(new TextWatcher() {
            //监听文本，如果改变，渲染底层布局，推送站点
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
