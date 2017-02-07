package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 06.01.2017.
 */

class InactivityMode {
    private static double airMassQ;
    private static double calculationQHeatLoss;
    private static double timeByOneModelTme;

    /**
     * The  method used for progress modeling inactivity mode .<br>
     */
    public static void inactivityStart(Model model) {
        for (; model.getRoomCurrentTempSingle() > model.getHomeMinT(); ) {
            airMassQ = AirMassQ.mathAirMassQ(model, 1);
            calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
            timeByOneModelTme = airMassQ / calculationQHeatLoss;
            model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() - 1);
            model.stepModeling(timeByOneModelTme, 0, calculationQHeatLoss, 0);
        }
    }

    public static double inactivityExpectancy(Model model, int countTempToInactivity) {
        airMassQ = AirMassQ.mathAirMassQ(model, countTempToInactivity);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        return airMassQ / calculationQHeatLoss;
    }
}
