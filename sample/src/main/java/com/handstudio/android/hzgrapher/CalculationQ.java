package com.handstudio.android.hzgrapher;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Dmitry Titorenko on 09.12.2016.
 */

public class CalculationQ {

    public static void main(String[] args) {
    }

    private void mathQ(float BD, int t1, int t2, double c, double N_heat_productivity,
                       int a, int b, int c_height, int t_street, double nn, double R0,
                       double B, double t_support, String modeType) {

        double tKelvin = t1 + 273.15;// перевод температуры в Кельвины
        int V = a * b * c_height;//обьем               (2.3)
        double p = 0.473 * (BD / tKelvin);// плотность (2.4)
        double m = p * V; //масса воздуха              (2.5)
        //Q.add(m * c * 1000);//домножаем на 1000 т.к. нужно перевести кДж в Дж (2.6)
//        time.add(Q.get(tm) / N_heat_productivity);

    }
}
