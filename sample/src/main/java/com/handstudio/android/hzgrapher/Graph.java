package com.handstudio.android.hzgrapher;

import android.os.Bundle;
import android.app.Activity;

public class Graph extends Activity {
    private String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);
        Bundle extras = getIntent().getExtras();
        MainAlgorithm mainAlgorithm=(MainAlgorithm) extras.get("Data");
        //int a= mainAlgorithm.getStartModeling();
        //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
        //Log.d(LOG_TAG,"asdfasdf: "+a);

    }


}
