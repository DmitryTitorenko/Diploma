package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

class SupportMode {

    /**
     * calculation energy consumption witch use for support this mode.<br>
     */
    public static void energySupport(Model model, double startSupport, double duringSupportTime) {
        for (; model.getRealTime() < startSupport + duringSupportTime; ) {
            double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
            double energyUsingInSupport = calculationQHeatLoss * duringSupportTime;
            double realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
            model.stepModeling(duringSupportTime, energyUsingInSupport, calculationQHeatLoss, realHeatProductivityN);
        }
    }
}
