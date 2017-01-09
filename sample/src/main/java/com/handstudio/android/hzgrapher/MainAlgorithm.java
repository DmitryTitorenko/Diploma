package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

public class MainAlgorithm extends Activity {
    private String LOG_TAG = "myLogs";
    private static int  event;
    private static final int eventEndModeling=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        mainAlgorithmBegin();
    }

    public void mainAlgorithmBegin() {
        Bundle extras = getIntent().getExtras();
        int startModeling = Integer.valueOf(extras.getString(Saving.startModeling));
        int endModeling = Integer.valueOf(extras.getString(Saving.endModeling));
        int homeOriginT = Integer.valueOf(extras.getString(Saving.homeOriginT));
        int homeMaxT = Integer.valueOf(extras.getString(Saving.homeMaxT));
        int homeMinT = Integer.valueOf(extras.getString(Saving.homeMinT));
        int wideRoom = Integer.valueOf(extras.getString(Saving.wideRoom));
        int lengthRoom = Integer.valueOf(extras.getString(Saving.lengthRoom));
        int heightRoom = Integer.valueOf(extras.getString(Saving.heightRoom));
        float atmospherePressureP = Integer.valueOf(extras.getString(Saving.atmospherePressureP));
        float specificHeatC = Float.valueOf(extras.getString(Saving.specificHeatC));
        float heatProductivityN = Float.valueOf(extras.getString(Saving.heatProductivityN));
        float coolingProductivityN = Float.valueOf(extras.getString(Saving.coolingProductivityN));
        float coefficientN = Float.valueOf(extras.getString(Saving.coefficientN));
        float r0 = Float.valueOf(extras.getString(Saving.r0));
        float heatLossExtraB = Float.valueOf(extras.getString(Saving.heatLossExtraB));
        int homeTimeChangeT = Integer.valueOf(extras.getString(Saving.homeTimeChangeT));
        int homeValueChangeT = Integer.valueOf(extras.getString(Saving.homeValueChangeT));
        Log.d(LOG_TAG, "startModeling " + startModeling);

        if (event==eventEndModeling){
            // write result to DB
        }
        else {
            if(homeOriginT<homeMinT){
                Attainment.startAttainment();
            }

        }
    }
}
