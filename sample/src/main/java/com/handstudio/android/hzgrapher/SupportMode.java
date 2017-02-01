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
     * @return double energy using in support .
     */
    public static void energySupport(Model model, int duringSupportTime) {
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        energyUsingInSupport = calculationQHeatLoss * duringSupportTime;
        timeByOneModelTme = duringSupportTime;
        model.stepModeling(timeByOneModelTme, energyUsingInSupport, calculationQHeatLoss);
    }
}
