package com.handstudio.android.hzgrapher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Dmitry Titorenko on 09.01.2017.
 */

class Model implements Serializable {
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
    private int modelingTime;
    private double realTime;
    private int homeOriginTCheck;
    private int roomCurrentTempSingle;
    private double timeToAttainmentRequiredTempRoom;

    private ArrayList<Integer> roomCurrentTemp = new ArrayList<>();
    private ArrayList<Double> usingEnergy = new ArrayList<>();
    private ArrayList<Double> calculationQHeatLoss = new ArrayList<>();
    private ArrayList<Double> timeByOneModelTime = new ArrayList<>();
    private ArrayList<Integer> modelTimeArray = new ArrayList<>();
    private ArrayList<Double> realHeatProductivityN = new ArrayList<>();
    private Map<Double, String> eventList = new TreeMap<>();


    private String event;

    public enum eventType {
        END_MODELING, START_ATTAINMENT, START_SUPPORT, START_INACTIVITY
    }

    public Model(int startModeling, int endModeling, int homeOriginT, int homeMaxT, int homeMinT, int streetOriginT,
                 int wideRoom, int lengthRoom, int heightRoom, double atmospherePressureP, double specificHeatC,
                 double heatProductivityN, double coolingProductivityN, double coefficientN, double r0,
                 double heatLossExtraB, int homeTimeChangeT, int homeValueChangeT) {
        this.startModeling = startModeling * 60;//from minute to second
        this.endModeling = endModeling * 60;
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
        event = "start";
    }

    /**
     * The  method used for progress modeling time.<br>
     */
    public void stepModeling(double timeByOneModelTime, double usingEnergy, double calculationQHeatLoss, double realHeatProductivityN) {
        this.timeByOneModelTime.add(timeByOneModelTime);
        this.roomCurrentTemp.add(roomCurrentTempSingle);
        this.usingEnergy.add(usingEnergy);
        this.calculationQHeatLoss.add(calculationQHeatLoss);
        this.realHeatProductivityN.add(realHeatProductivityN);
        modelingTime++;
        this.modelTimeArray.add(modelingTime);
        realTime += timeByOneModelTime;
    }

    /**
     * The  method used for get current event.<br>
     *
     * @return String event type.
     */
    public  String getCurrentEventType(double i) {
        //return (int) eventList.keySet().toArray()[i];
        return eventList.get(i);
    }

    public  double getCurrentEventKey(int i) {
        return (double) eventList.keySet().toArray()[i];
    }




    public int getStartModeling() {
        return startModeling;
    }

    public void setStartModeling(int startModeling) {
        this.startModeling = startModeling;
    }

    public int getEndModeling() {
        return endModeling;
    }

    public void setEndModeling(int endModeling) {
        this.endModeling = endModeling;
    }

    public int getHomeMaxT() {
        return homeMaxT;
    }

    public void setHomeMaxT(int homeMaxT) {
        this.homeMaxT = homeMaxT;
    }

    public int getHomeMinT() {
        return homeMinT;
    }

    public void setHomeMinT(int homeMinT) {
        this.homeMinT = homeMinT;
    }

    public int getStreetOriginT() {
        return streetOriginT;
    }

    public void setStreetOriginT(int streetOriginT) {
        this.streetOriginT = streetOriginT;
    }

    public int getWideRoom() {
        return wideRoom;
    }

    public void setWideRoom(int wideRoom) {
        this.wideRoom = wideRoom;
    }

    public int getLengthRoom() {
        return lengthRoom;
    }

    public void setLengthRoom(int lengthRoom) {
        this.lengthRoom = lengthRoom;
    }

    public int getHeightRoom() {
        return heightRoom;
    }

    public void setHeightRoom(int heightRoom) {
        this.heightRoom = heightRoom;
    }

    public double getAtmospherePressureP() {
        return atmospherePressureP;
    }

    public void setAtmospherePressureP(double atmospherePressureP) {
        this.atmospherePressureP = atmospherePressureP;
    }

    public double getSpecificHeatC() {
        return specificHeatC;
    }

    public void setSpecificHeatC(double specificHeatC) {
        this.specificHeatC = specificHeatC;
    }

    public double getHeatProductivityN() {
        return heatProductivityN;
    }

    public void setHeatProductivityN(double heatProductivityN) {
        this.heatProductivityN = heatProductivityN;
    }

    public double getCoolingProductivityN() {
        return coolingProductivityN;
    }

    public void setCoolingProductivityN(double coolingProductivityN) {
        this.coolingProductivityN = coolingProductivityN;
    }

    public double getCoefficientN() {
        return coefficientN;
    }

    public void setCoefficientN(double coefficientN) {
        this.coefficientN = coefficientN;
    }

    public double getR0() {
        return r0;
    }

    public void setR0(double r0) {
        this.r0 = r0;
    }

    public double getHeatLossExtraB() {
        return heatLossExtraB;
    }

    public void setHeatLossExtraB(double heatLossExtraB) {
        this.heatLossExtraB = heatLossExtraB;
    }

    public int getHomeTimeChangeT() {
        return homeTimeChangeT;
    }

    public void setHomeTimeChangeT(int homeTimeChangeT) {
        this.homeTimeChangeT = homeTimeChangeT;
    }

    public int getHomeValueChangeT() {
        return homeValueChangeT;
    }

    public void setHomeValueChangeT(int homeValueChangeT) {
        this.homeValueChangeT = homeValueChangeT;
    }

    public int getModelingTime() {
        return modelingTime;
    }

    public void setModelingTime(int modelingTime) {
        this.modelingTime = modelingTime;
    }

    public double getRealTime() {
        return realTime;
    }

    public void setRealTime(double realTime) {
        this.realTime = realTime;
    }

    public int getHomeOriginTCheck() {
        return homeOriginTCheck;
    }

    public void setHomeOriginTCheck(int homeOriginTCheck) {
        this.homeOriginTCheck = homeOriginTCheck;
    }

    public int getRoomCurrentTempSingle() {
        return roomCurrentTempSingle;
    }

    public void setRoomCurrentTempSingle(int roomCurrentTempSingle) {
        this.roomCurrentTempSingle = roomCurrentTempSingle;
    }

    public ArrayList<Integer> getRoomCurrentTemp() {
        return roomCurrentTemp;
    }

    public void setRoomCurrentTemp(ArrayList<Integer> roomCurrentTemp) {
        this.roomCurrentTemp = roomCurrentTemp;
    }

    public ArrayList<Double> getUsingEnergy() {
        return usingEnergy;
    }

    public void setUsingEnergy(ArrayList<Double> usingEnergy) {
        this.usingEnergy = usingEnergy;
    }

    public ArrayList<Double> getCalculationQHeatLoss() {
        return calculationQHeatLoss;
    }

    public void setCalculationQHeatLoss(ArrayList<Double> calculationQHeatLoss) {
        this.calculationQHeatLoss = calculationQHeatLoss;
    }

    public ArrayList<Double> getTimeByOneModelTime() {
        return timeByOneModelTime;
    }

    public void setTimeByOneModelTime(ArrayList<Double> timeByOneModelTime) {
        this.timeByOneModelTime = timeByOneModelTime;
    }

    public ArrayList<Integer> getModelTimeArray() {
        return modelTimeArray;
    }

    public void setModelTimeArray(ArrayList<Integer> modelTimeArray) {
        this.modelTimeArray = modelTimeArray;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public double getRealHeatProductivityN() {
        return realHeatProductivityN.get(realHeatProductivityN.size() - 1);
    }

    public double getTimeToAttainmentRequiredTempRoom() {
        return timeToAttainmentRequiredTempRoom;
    }

    public void setTimeToAttainmentRequiredTempRoom(double timeToAttainmentRequiredTempRoom) {
        this.timeToAttainmentRequiredTempRoom = timeToAttainmentRequiredTempRoom;
    }

    public Map<Double, String> getEventList() {
        return eventList;
    }

    public void setEventList(double time, String event) {
        this.eventList.put(time,event);
    }
}
