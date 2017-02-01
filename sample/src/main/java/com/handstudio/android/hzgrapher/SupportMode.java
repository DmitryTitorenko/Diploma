package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

class SupportMode {

    /**
     * calculation energy consumption witch use for support this mode.<br>
     */
    public static void energySupport(Model model, int duringSupportTime) {
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        double energyUsingInSupport = calculationQHeatLoss * duringSupportTime;
        double timeByOneModelTme = duringSupportTime;
        model.stepModeling(timeByOneModelTme, energyUsingInSupport, calculationQHeatLoss);
    }
}
