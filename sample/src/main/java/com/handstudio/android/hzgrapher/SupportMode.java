package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

public class SupportMode {
    /**
     * calculation energy consumption witch use for support this mode.<br>
     *
     * @param a       width
     * @param b       length;
     * @param homeT   temperature in home;
     * @param streetT temperature in street;
     * @param B       additional heat loss ;
     * @param nn      coefficient relatively flat outer surface of the outside air;
     * @param R0      heat resistance;
     * @param time    during support mod;
     * @return float energy consumption .
     */
    public static float energySupport(int a, int b, int homeT, int streetT, int B, int nn, int R0, int time) {
        float energy = CalculationQHeatLoss.calculationQHeatLoss(a, b, homeT, streetT, B, nn, R0) * time;
        return energy;
    }
}
