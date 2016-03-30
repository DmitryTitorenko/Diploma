package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Select_mode extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mode1:
                Intent intent = new Intent(Select_mode.this, Mode_first.class);
                startActivity(intent);
                break;
            case R.id.btn_mode2:
                Intent i = new Intent(Select_mode.this, Mode_second.class);
                startActivity(i);
                break;
            case R.id.btn_mode3:
                Intent ii = new Intent(Select_mode.this, Mode_third.class);
                startActivity(ii);
                break;
        }
    }
}
