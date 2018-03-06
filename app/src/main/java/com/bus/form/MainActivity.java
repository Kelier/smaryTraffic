package com.bus.form;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bus.body.BodyActivity;

public class MainActivity extends BodyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
