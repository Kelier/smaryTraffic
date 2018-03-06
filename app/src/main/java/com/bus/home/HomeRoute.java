package com.bus.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bus.form.R;

/**
 * Created by John Yan on 2/12/2017.
 */

public class HomeRoute extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_navi_view);
    }
}
