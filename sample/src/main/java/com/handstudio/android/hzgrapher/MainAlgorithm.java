package com.handstudio.android.hzgrapher;

import android.app.Activity;
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
    private double atmospherePressureP;
    private double specificHeatC;
    private double heatProductivityN;
    private double coolingProductivityN;
    private double coefficientN;
    private double r0;
    private double heatLossExtraB;
    private int homeTimeChangeT;
    private int homeValueChangeT;
    private int streetOriginT;
    private double realHeatProductivityN;

    private String LOG_TAG = "myLogs";
    private static int modelingTime;
    private String event = "start";

    private enum eventType {
        ENDMODELING, STARTATTAINMENT, CHANGEROOMTEMP
    }

    private int volume;
    private int roomCurrentTemp;
    private double realTime;
    private double qHeatLoss;
    private double timeByOneModelTme; //длительность одного модельного времени

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
        specificHeatC = Double.valueOf(extras.getString(Saving.specificHeatC));
        heatProductivityN = Double.valueOf(extras.getString(Saving.heatProductivityN));
        coolingProductivityN = Double.valueOf(extras.getString(Saving.coolingProductivityN));
        coefficientN = Double.valueOf(extras.getString(Saving.coefficientN));
        r0 = Double.valueOf(extras.getString(Saving.r0));
        heatLossExtraB = Double.valueOf(extras.getString(Saving.heatLossExtraB));
        homeTimeChangeT = Integer.valueOf(extras.getString(Saving.homeTimeChangeT));
        homeValueChangeT = Integer.valueOf(extras.getString(Saving.homeValueChangeT));
        streetOriginT = Integer.valueOf(extras.getString(Saving.streetOriginT));
        heatProductivityN = Integer.valueOf(extras.getString(Saving.heatProductivityN));

        volume = wideRoom * lengthRoom * heightRoom;//обьем комнаты                (2.3)
        Log.d(LOG_TAG, "startModeling " + startModeling);
        if (event.equals(eventType.ENDMODELING)) {
            // write result to DB
        } else {
            Log.d(LOG_TAG, "Log " + event);

            if (homeOriginT < homeMinT) {
                event = eventType.CHANGEROOMTEMP.toString();
                Log.d(LOG_TAG, "Log " + 1);

                startAttainment(homeOriginT, homeMinT);
                Log.d(LOG_TAG, "Log " + 2);
            }
        }
    }

    private void startAttainment(int homeOriginTemp, int attainmentTemp) {
        for (; homeOriginTemp < attainmentTemp; ) {
            if (event.equals(eventType.CHANGEROOMTEMP.toString())) {
                Log.d(LOG_TAG, "Log " + 3);

                mathTimeForAttainment(homeOriginTemp);
                modelingTime++;
                homeOriginTemp++;
            } else {
                Log.d(LOG_TAG, "Log " + "end");
            }
        }
    }

    private void mathTimeForAttainment(int homeOriginTemp) {
        double q = mathQ(homeOriginTemp);
        qHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(wideRoom, lengthRoom, roomCurrentTemp, streetOriginT, heatLossExtraB, coefficientN, r0);
        realHeatProductivityN = heatProductivityN - qHeatLoss;
        timeByOneModelTme = q / realHeatProductivityN;
        realTime += timeByOneModelTme;
        Log.d(LOG_TAG, "Logeee " + timeByOneModelTme);

    }

    private double mathQ(int originTemp) {
        double tKelvin = originTemp + 273.15;// перевод температуры в Кельвины
        double densityP = 0.473 * (atmospherePressureP / tKelvin);// плотность (2.4)
        double mass = densityP * volume; //масса воздуха              (2.5)
        double airMassQ = (mass * specificHeatC * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж (2.6)
        return airMassQ;
    }
}
