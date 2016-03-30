package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SelectMode extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mode1:
                Intent intent = new Intent(SelectMode.this, ModeFirst.class);
                startActivity(intent);
                break;
            case R.id.btn_mode2:
                Intent i = new Intent(SelectMode.this, ModeSecond.class);
                startActivity(i);
                break;
            case R.id.btn_mode3:
                Intent ii = new Intent(SelectMode.this, ModeThird.class);
                startActivity(ii);
                break;
        }
    }
}
