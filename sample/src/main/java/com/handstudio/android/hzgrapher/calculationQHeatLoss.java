package com.handstudio.android.hzgrapher;


/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

class CalculationQHeatLoss {
    /**
     * The  method used for calculation heat loss.<br>
     *
     * @return double heat loss.
     */
    public static double calculationQHeatLoss(Model model) {
        return (model.getWideRoom() * model.getLengthRoom() *
                (model.getRoomCurrentTempSingle() - model.getStreetOriginT())
                * (1 + model.getHeatLossExtraB()) * model.getCoefficientN() / model.getR0()); //(2.2)
    }
}
