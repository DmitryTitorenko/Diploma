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

    private int volume;
    private ArrayList<Integer> roomCurrentTemp = new ArrayList<>();
    private ArrayList<Double> usingEnergy = new ArrayList<>();
    private ArrayList<Double> calculationQHeatLoss = new ArrayList<>();
    private ArrayList<Double> timeByOneModelTme = new ArrayList<>(); //длительность одного модельного времени
    private ArrayList<Integer> modelTimeArray=new ArrayList<>();

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
        homeOriginTCheck=homeOriginT;
    }

    public void checkMinRoomT() {
        if (homeOriginTCheck < homeMinT) {
            event = eventType.STARTATTAINMENT.toString();
            roomCurrentTemp.clear();
            startAttainment(homeMinT);
        }
        //event = eventType.ENDMODELING.toString();
        Log.d(LOG_TAG, "Log " + "end checkMinRoomT");
    }

    public void mainAlgorithmBegin() {
        checkMinRoomT();
        SupportMode.energySupport(wideRoom, lengthRoom, roomCurrentTemp, streetOriginT, heatLossExtraB, coefficientN, r0, endModeling - startModeling);
        stepModeling(SupportMode.getTimeByOneModelTme(), roomCurrentTemp.get(roomCurrentTemp.size() - 1), SupportMode.getEnergyUsingInSupport(), SupportMode.getCalculationQHeatLoss());
        Log.d(LOG_TAG, "usingEnergy: " + usingEnergy.toString());

        if (event.equals(eventType.ENDMODELING)) {
            // write result to DB
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
        double q = mathQ(roomCurrentTemp.get(roomCurrentTemp.size() - 1));
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(wideRoom, lengthRoom, roomCurrentTemp, streetOriginT, heatLossExtraB, coefficientN, r0);
        realHeatProductivityN = heatProductivityN - calculationQHeatLoss;
        double timeByOneModelTme = q / realHeatProductivityN;
        double usingEnergy = timeByOneModelTme * realHeatProductivityN;
        realTime += timeByOneModelTme;
        stepModeling(timeByOneModelTme, roomCurrentTemp.get(roomCurrentTemp.size() - 1) + 1, usingEnergy, calculationQHeatLoss);
        Log.d(LOG_TAG, "timeByOneModelTme " + timeByOneModelTme);
    }

    private double mathQ(int originTemp) {
        double tKelvin = originTemp + 273.15;// перевод температуры в Кельвины
        double densityP = 0.473 * (atmospherePressureP / tKelvin);// плотность (2.4)
        volume = wideRoom * lengthRoom * heightRoom;//обьем комнаты                (2.3)
        double mass = densityP * volume; //масса воздуха              (2.5)
        double airMassQ = (mass * specificHeatC * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж (2.6)
        return airMassQ;
    }

    private void stepModeling(double timeByOneModelTme, int roomCurrentTemp, double usingEnergy, double calculationQHeatLoss) {
        this.timeByOneModelTme.add(timeByOneModelTme);
        this.roomCurrentTemp.add(roomCurrentTemp);
        this.usingEnergy.add(usingEnergy);
        this.calculationQHeatLoss.add(calculationQHeatLoss);
        modelingTime++;
        this.modelTimeArray.add(modelingTime);
    }
}
