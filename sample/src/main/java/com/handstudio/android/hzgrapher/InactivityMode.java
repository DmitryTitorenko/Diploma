package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 06.01.2017.
 */

class InactivityMode {
    private static double airMassQ;
    private static double calculationQHeatLoss;
    private static double timeByOneModelTme;

    public enum modelingOrExpectancy {
        MODELING, EXPECTANCY
    }

    /**
     * The  method used for progress modeling inactivity mode .<br>
     *
     * @param type check is we expectancy or modeling.
     */

    public static void inactivityStart(Model model, String type) {
        airMassQ = AirMassQ.mathAirMassQ(model, 1);
        calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
        timeByOneModelTme = airMassQ / calculationQHeatLoss;
        model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() - 1);
        if (type.equals(modelingOrExpectancy.MODELING.toString())) {
            model.stepModeling(timeByOneModelTme, 0, calculationQHeatLoss, 0);
        } else if (type.equals(modelingOrExpectancy.EXPECTANCY.toString())) {
            model.setRealTime(model.getRealTime() + timeByOneModelTme);
        }
    }

    public static double getTimeByOneModelTme() {
        return timeByOneModelTme;
    }
}
