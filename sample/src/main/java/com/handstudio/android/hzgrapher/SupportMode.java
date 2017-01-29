package com.handstudio.android.hzgrapher;

import java.util.ArrayList;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

public class SupportMode {

    private static double energyUsingInSupport;
    private static double calculationQHeatLoss;
    private static double timeByOneModelTme;

    /**
     * calculation energy consumption witch use for support this mode.<br>
     *
     * @param wideRoom          width
     * @param lengthRoom        length;
     * @param roomCurrentTemp   temperature in home;
     * @param streetT           temperature in street;
     * @param heatLossExtraB    additional heat loss ;
     * @param coefficientN      coefficient relatively flat outer surface of the outside air;
     * @param r0                heat resistance;
     * @param duringSupportTime during support mod;
     * @return double energy using in support .
     */
    public static double energySupport(int wideRoom, int lengthRoom, ArrayList<Integer> roomCurrentTemp, int streetT, double heatLossExtraB, double coefficientN, double r0, int duringSupportTime) {
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(wideRoom, lengthRoom, roomCurrentTemp, streetT, heatLossExtraB, coefficientN, r0);
        energyUsingInSupport = calculationQHeatLoss * duringSupportTime;
        timeByOneModelTme = duringSupportTime;
        return energyUsingInSupport;
    }

    public static double getEnergyUsingInSupport() {
        return energyUsingInSupport;
    }

    public static double getCalculationQHeatLoss() {
        return calculationQHeatLoss;
    }

    public static double getTimeByOneModelTme() {
        return timeByOneModelTme;
    }


}
