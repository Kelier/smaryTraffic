package com.bus.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bus.form.R;

/**
 * Created by John Yan on 1/31/2017.
 */

public class SettingFeed extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_feedback);
        initView();
    }

    private void initView() {
        TextView back_me= (TextView) findViewById(R.id.back_me);
        Button commit= (Button) findViewById(R.id.commit_tip);
        final EditText editplace= (EditText) findViewById(R.id.edit_place);
        back_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (editplace.getText().length()>0){
                    Toast.makeText(SettingFeed.this, "提交成功", Toast.LENGTH_LONG).show();
                    editplace.setText("");}
                else
                    Toast.makeText(SettingFeed.this, "提交失败", Toast.LENGTH_LONG).show();
            }
        });
    }


}
