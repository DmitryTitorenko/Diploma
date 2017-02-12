package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

class TimeForAttainment {

    private static double timeByOneModelTme;
    private static double usingEnergy;
    private static double calculationQHeatLoss;
    private static double realHeatProductivityN;
    private static double airMassQ;
    private static double timeByExpectancy;

    /**
     * The  method used for calculation time for attainment .<br>
     */
    public static void mathTimeForAttainment(Model model) {
        airMassQ = AirMassQ.mathAirMassQ(model, 1);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        timeByOneModelTme = airMassQ / realHeatProductivityN;
        usingEnergy = timeByOneModelTme * realHeatProductivityN;
    }

    /**
     * The  method used for calculation how long will be work attainment mode.<br>
     *
     * @param temp count of temp witch need expectancy
     */
    public static void mathTimeForAttainmentExpectancy(Model model, int temp) {
        airMassQ = AirMassQ.mathAirMassQ(model, temp);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        realHeatProductivityN = model.getHeatProductivityN() - calculationQHeatLoss;
        timeByExpectancy = airMassQ / realHeatProductivityN;
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

    public static double getTimeByExpectancy() {
        return timeByExpectancy;
    }

    public static double getAirMassQ() {

        return airMassQ;
    }
}