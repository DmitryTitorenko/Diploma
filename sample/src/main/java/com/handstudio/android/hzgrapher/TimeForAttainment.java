package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

class TimeForAttainment {

    private static double timeByOneModelTme;
    private static double usingEnergy;
    private static double calculationQHeatLoss;

    /**
     * The  method used for calculation time for attainment .<br>
     */
    public static void mathTimeForAttainment(Model model) {
        double airMassQ = AirMassQ.mathAirMassQ(model);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        model.setHeatProductivityN(model.getHeatProductivityN() - calculationQHeatLoss);
        timeByOneModelTme = airMassQ / model.getRealHeatProductivityN();
        usingEnergy = timeByOneModelTme * model.getRealHeatProductivityN();
    }

    public static double getTimeByOneModelTme() {
        return timeByOneModelTme;
    }

    public static double getUsingEnergy() {
        return usingEnergy;
    }

    public static double getCalculationQHeatLoss() {
        return calculationQHeatLoss;
    }
}
