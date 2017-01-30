package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 30.01.2017.
 */

public class AirMassQ {
    public static double airMassQ(double roomCurrentTempSingle, double atmospherePressureP, int wideRoom, int lengthRoom, int heightRoom, double specificHeatC) {
        double tKelvin = roomCurrentTempSingle + 273.15;// перевод температуры в Кельвины
        double densityP = 0.473 * (atmospherePressureP / tKelvin);// плотность (2.4)
        int volume = wideRoom * lengthRoom * heightRoom;//обьем комнаты                (2.3)
        double mass = densityP * volume; //масса воздуха              (2.5)
        double airMassQ = (mass * specificHeatC * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж (2.6)
        return airMassQ;
    }
}
