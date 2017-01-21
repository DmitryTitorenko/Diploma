package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

public class MainAlgorithm extends Activity {
    private int startModeling;
    private int endModeling;
    private int homeOriginT;
    private int homeMaxT;
    private int homeMinT;
    private int wideRoom;
    private int lengthRoom;
    private int heightRoom;
    private float atmospherePressureP;
    private float specificHeatC;
    private float heatProductivityN;
    private float coolingProductivityN;
    private float coefficientN;
    private float r0;
    private float heatLossExtraB;
    private int homeTimeChangeT;
    private int homeValueChangeT;
    private int streetOriginT;

    private String LOG_TAG = "myLogs";
    private static final int eventEndModeling = 1;
    private static int modelingTime;
    private String event;
    private enum eventType{
        EVENTENDMODELING,STARTATTAINMENT
    }

    private int volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        mainAlgorithmBegin();
    }

    public void mainAlgorithmBegin() {
        Bundle extras = getIntent().getExtras();
        startModeling = Integer.valueOf(extras.getString(Saving.startModeling));
        endModeling = Integer.valueOf(extras.getString(Saving.endModeling));
        homeOriginT = Integer.valueOf(extras.getString(Saving.homeOriginT));
        homeMaxT = Integer.valueOf(extras.getString(Saving.homeMaxT));
        homeMinT = Integer.valueOf(extras.getString(Saving.homeMinT));
        wideRoom = Integer.valueOf(extras.getString(Saving.wideRoom));
        lengthRoom = Integer.valueOf(extras.getString(Saving.lengthRoom));
        heightRoom = Integer.valueOf(extras.getString(Saving.heightRoom));
        atmospherePressureP = Integer.valueOf(extras.getString(Saving.atmospherePressureP));
        specificHeatC = Float.valueOf(extras.getString(Saving.specificHeatC));
        heatProductivityN = Float.valueOf(extras.getString(Saving.heatProductivityN));
        coolingProductivityN = Float.valueOf(extras.getString(Saving.coolingProductivityN));
        coefficientN = Float.valueOf(extras.getString(Saving.coefficientN));
        r0 = Float.valueOf(extras.getString(Saving.r0));
        heatLossExtraB = Float.valueOf(extras.getString(Saving.heatLossExtraB));
        homeTimeChangeT = Integer.valueOf(extras.getString(Saving.homeTimeChangeT));
        homeValueChangeT = Integer.valueOf(extras.getString(Saving.homeValueChangeT));
        streetOriginT = Integer.valueOf(extras.getString(Saving.streetOriginT));

        volume = wideRoom * lengthRoom * heightRoom;//обьем комнаты                (2.3)
        Log.d(LOG_TAG, "startModeling " + startModeling);

        if (event.equals(eventType.EVENTENDMODELING)) {
            // write result to DB
        } else {
            if (homeOriginT < homeMinT) {
                startAttainment(homeOriginT,homeMinT);
            }

        }
    }

    public void startAttainment(int originTemp, int attainmentTemp) {
        for (; attainmentTemp <originTemp; originTemp++) {
            mathQ(originTemp);
        }
    }
    private void mathQ(int originTemp){
        double tKelvin = originTemp + 273.15;// перевод температуры в Кельвины
        double densityP = 0.473 * (atmospherePressureP / tKelvin);// плотность (2.4)
        double mass = densityP * volume; //масса воздуха              (2.5)
        double Q=(mass * specificHeatC * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж (2.6)
    }
}
