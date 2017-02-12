package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

class SupportMode {

    /**
     * calculation energy consumption witch use in support.<br>
     */

    /*
    public static void energySupport(Model model) {
        //находим продолжительность времени поддержки вычитая время начала будующего событие из текущего
        double duringSupportTime = model.getCurrentEventKey(model.getModelingTime() + 1) - model.getCurrentEventKey(model.getModelingTime());
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        double energyUsingInSupport = calculationQHeatLoss * duringSupportTime;
        double realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        model.stepModeling(duringSupportTime, energyUsingInSupport, calculationQHeatLoss, realHeatProductivityN);
    }*/

    public static void energySupport(Model model, double durationSupportTime) {
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        double energyUsingInSupport = calculationQHeatLoss * durationSupportTime;
        double realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        model.stepModeling(durationSupportTime, energyUsingInSupport, calculationQHeatLoss, realHeatProductivityN);
    }
}
