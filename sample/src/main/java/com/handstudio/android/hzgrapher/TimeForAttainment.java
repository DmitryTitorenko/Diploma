package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

class TimeForAttainment {

    private static double timeByOneModelTme;
    private static double usingEnergy;
    private static double calculationQHeatLoss;
    private static double realHeatProductivityN;


    /**
     * The  method used for calculation time for attainment .<br>
     */
    public static void mathTimeForAttainment(Model model, int temp) {
        double airMassQ = AirMassQ.mathAirMassQ(model, temp);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        timeByOneModelTme = airMassQ / realHeatProductivityN;
        usingEnergy = timeByOneModelTme * realHeatProductivityN;
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

    public static double getRealHeatProductivityN() {
        return realHeatProductivityN;
    }

}
