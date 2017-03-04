package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

class SupportMode {

    /**
     * The  method used for calculation energy consumption witch use in support.<br>
     *
     * @param model               entity witch contain all parameters
     * @param durationSupportTime time for work support mode in seconds
     */
    public static void energySupport(Model model, double durationSupportTime) {
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        double energyUsingInSupport = calculationQHeatLoss * durationSupportTime;
        double realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        model.stepModeling(durationSupportTime, energyUsingInSupport, calculationQHeatLoss, realHeatProductivityN);
    }
}
