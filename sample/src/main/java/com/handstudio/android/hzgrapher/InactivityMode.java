package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 06.01.2017.
 */

class InactivityMode {
    /**
     * The  method used for progress modeling inactivity mode .<br>
     */
    public static void inactivityStart(Model model) {
        for (; model.getRoomCurrentTempSingle() > model.getHomeMinT(); ) {
            double airMassQ = AirMassQ.mathAirMassQ(model);
            double calculationQHeatLoss = CalculationQHeatLoss.calculationQHeatLoss(model);
            double timeByOneModelTme = airMassQ / calculationQHeatLoss;
            model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() - 1);
            model.stepModeling(timeByOneModelTme, 0, calculationQHeatLoss, 0);

            /*
            switch (model.getEvent()) {
                case "HomeChangeT":
                    break;
                case "StreetChangeT":
                    break;
                default:
                    break;
            }*/
        }
    }
}
