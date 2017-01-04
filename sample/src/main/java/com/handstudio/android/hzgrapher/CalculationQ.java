package com.handstudio.android.hzgrapher;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by GrinWey on 09.12.2016.
 */

public class CalculationQ {

    public static void main(String[] args) {

        Random r=new Random();
        //System.out.println(r.nextGaussian()*15+60);//генерация СВ на диапазоне 60+-15
        //int rnd = 5 + (int)(Math.random() * ((10 - 5) + 1));//интервал [5,10]

        System.out.println(Arrays.deepToString(random(5)));
    }
    public static int [][] random(int countInterval){
        double durationInterval=24/countInterval;
        int [][] randomTime=new int[countInterval][2];
        System.out.println(Arrays.deepToString(randomTime));
        for (int i = 0; i < countInterval; i++) {
            randomTime[i][1]=5 + (int)(Math.random() * ((10 - 5) + 1));
        }
        return randomTime;

    }
    private void math(float BD, int t1, int t2, double c, double N_heat_productivity,
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
