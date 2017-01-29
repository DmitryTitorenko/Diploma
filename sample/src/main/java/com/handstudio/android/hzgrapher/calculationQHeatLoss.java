package com.handstudio.android.hzgrapher;

import java.util.ArrayList;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

public class CalculationQHeatLoss {
    /**
     * calculation heat loss.<br>
     *
     * @param wideRoom        width
     * @param lengthRoom      length;
     * @param roomCurrentTemp temperature in home;
     * @param streetT         temperature in street;
     * @param heatLossExtraB  extra heat loss ;
     * @param coefficientN    coefficient relatively flat outer surface of the outside air;
     * @param r0              heat resistance;
     * @return double heat loss.
     */
    public static double calculationQHeatLoss(int wideRoom, int lengthRoom, ArrayList<Integer> roomCurrentTemp, int streetT, double heatLossExtraB, double coefficientN, double r0) {
        double heatLoss = (wideRoom * lengthRoom * (roomCurrentTemp.get(roomCurrentTemp.size() - 1) - streetT) * (1 + heatLossExtraB) * coefficientN / r0);//(2.2)
        return heatLoss;
    }
}
