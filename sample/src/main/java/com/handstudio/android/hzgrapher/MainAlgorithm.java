package com.handstudio.android.hzgrapher;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

public class MainAlgorithm implements Serializable {
    private int startModeling;
    private int endModeling;
    private int homeMaxT;
    private int homeMinT;
    private int streetOriginT;
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

    private double realHeatProductivityN;

    private String LOG_TAG = "myLogs";
    private static int modelingTime;
    private String event = "start";

    private enum eventType {
        ENDMODELING, STARTATTAINMENT
    }

    private double roomCurrentTempSingle;
    private ArrayList<Double> roomCurrentTemp = new ArrayList<>();
    private ArrayList<Double> usingEnergy = new ArrayList<>();
    private ArrayList<Double> calculationQHeatLoss = new ArrayList<>();
    private ArrayList<Double> timeByOneModelTme = new ArrayList<>(); //длительность одного модельного времени
    private ArrayList<Integer> modelTimeArray = new ArrayList<>();

    private double realTime;
    private double qHeatLoss;
    private static int homeOriginTCheck;

    public MainAlgorithm(int startModeling, int endModeling, int homeOriginT, int homeMaxT, int homeMinT, int streetOriginT,
                         int wideRoom, int lengthRoom, int heightRoom, double atmospherePressureP, double specificHeatC,
                         double heatProductivityN, double coolingProductivityN, double coefficientN, double r0,
                         double heatLossExtraB, int homeTimeChangeT, int homeValueChangeT) {
        this.startModeling = startModeling;
        this.endModeling = endModeling;
        this.homeMaxT = homeMaxT;
        this.homeMinT = homeMinT;
        this.streetOriginT = streetOriginT;
        this.wideRoom = wideRoom;
        this.lengthRoom = lengthRoom;
        this.heightRoom = heightRoom;
        this.atmospherePressureP = atmospherePressureP;
        this.specificHeatC = specificHeatC;
        this.heatProductivityN = heatProductivityN;
        this.coolingProductivityN = coolingProductivityN;
        this.coefficientN = coefficientN;
        this.r0 = r0;
        this.heatLossExtraB = heatLossExtraB;
        this.homeTimeChangeT = homeTimeChangeT;
        this.homeValueChangeT = homeValueChangeT;
        roomCurrentTempSingle = homeOriginT;
    }

    public void checkMinRoomT() {
        if (roomCurrentTempSingle < homeMinT) {
            event = eventType.STARTATTAINMENT.toString();
            startAttainment(homeMinT);
        }
        //event = eventType.ENDMODELING.toString();
        Log.d(LOG_TAG, "Log " + "end checkMinRoomT");
    }

    public void mainAlgorithmBegin() {
        checkMinRoomT();
        SupportMode.energySupport(wideRoom, lengthRoom, roomCurrentTempSingle, streetOriginT, heatLossExtraB, coefficientN, r0, endModeling - startModeling);
        stepModeling(SupportMode.getTimeByOneModelTme(), SupportMode.getEnergyUsingInSupport(), SupportMode.getCalculationQHeatLoss());
        Log.d(LOG_TAG, "usingEnergy: " + usingEnergy.toString());
        event = eventType.ENDMODELING.toString();
        Log.d(LOG_TAG, "event " + event);

        if (event.equals(eventType.ENDMODELING.toString())) {
            Log.d(LOG_TAG, "event 2 " + event);

            // write result to DB
            WriteReportToSD.writeFileSDFirst(startModeling, endModeling, homeMaxT, homeMinT, streetOriginT, homeTimeChangeT, homeValueChangeT, realHeatProductivityN,
                    modelingTime, realTime, roomCurrentTemp, usingEnergy, calculationQHeatLoss, timeByOneModelTme, modelTimeArray);
            Log.d(LOG_TAG, "Log " + "write to DB");
        } else {

        }
    }

    private void startAttainment(int attainmentTemp) {
        for (; homeOriginTCheck < attainmentTemp; ) {
            if (event.equals(eventType.STARTATTAINMENT.toString())) {
                mathTimeForAttainment();
                homeOriginTCheck++;
            } else {
            }
        }
    }

    private void mathTimeForAttainment() {
        double q = AirMassQ.airMassQ(roomCurrentTempSingle, atmospherePressureP, wideRoom, lengthRoom, heightRoom, specificHeatC);
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(wideRoom, lengthRoom, roomCurrentTempSingle, streetOriginT, heatLossExtraB, coefficientN, r0);
        realHeatProductivityN = heatProductivityN - calculationQHeatLoss;
        double timeByOneModelTme = q / realHeatProductivityN;
        double usingEnergy = timeByOneModelTme * realHeatProductivityN;
        roomCurrentTempSingle++;
        stepModeling(timeByOneModelTme, usingEnergy, calculationQHeatLoss);
        Log.d(LOG_TAG, "timeByOneModelTme " + timeByOneModelTme);
    }


    private void stepModeling(double timeByOneModelTme, double usingEnergy, double calculationQHeatLoss) {
        this.timeByOneModelTme.add(timeByOneModelTme);
        this.roomCurrentTemp.add(roomCurrentTempSingle);
        this.usingEnergy.add(usingEnergy);
        this.calculationQHeatLoss.add(calculationQHeatLoss);
        modelingTime++;
        this.modelTimeArray.add(modelingTime);
        realTime += modelingTime;
    }
}
