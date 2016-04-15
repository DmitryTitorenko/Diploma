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
            case R.id.btnStartFirstMode:
                Intent intentModeFirst = new Intent(SelectMode.this, ModeFirst.class);
                startActivity(intentModeFirst);
                break;
            case R.id.btnStartSeconMode:
                Intent intentModeSecond = new Intent(SelectMode.this, ModeSecond.class);
                startActivity(intentModeSecond);
                break;
            case R.id.btnStartThirdMode:
                Intent intentModeThird = new Intent(SelectMode.this, ModeThird.class);
                startActivity(intentModeThird);
                break;
            case R.id.btnStartSaveMode:
                Intent intentSaveMode = new Intent(SelectMode.this, Saving.class);
                startActivity(intentSaveMode);
        }
    }
}
