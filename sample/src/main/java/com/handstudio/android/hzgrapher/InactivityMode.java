package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 06.01.2017.
 */

class InactivityMode {
    private static double timeByOneModelTme;

    public enum modelingOrExpectancy {
        MODELING, EXPECTANCY
    }

    /**
     * The  method used for progress modeling inactivity mode .<br>
     *
     * @param model entity witch contain all parameters
     * @param type  check are we expectancy or modeling.
     */
    public static void inactivityStart(Model model, String type) {
        double airMassQ = AirMassQ.mathAirMassQ(model);
        double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
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
