package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

public class MainAlgorithm extends Activity {
    private String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        mainAlgorithmBegin();
    }

    public void mainAlgorithmBegin() {
        Bundle extras = getIntent().getExtras();
        int startModeling = Integer.valueOf(extras.getString("startModeling"));
        System.out.println(startModeling);
        Log.d(LOG_TAG, "startModeling " + startModeling);
    }
}
