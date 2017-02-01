package com.handstudio.android.hzgrapher;


/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

public class CalculationQHeatLoss {
    /**
     * The  method used for calculation heat loss.<br>
     *
     * @return double heat loss.
     */
    public static double calculationQHeatLoss(Model model) {
        double heatLoss = (model.getWideRoom() * model.getLengthRoom() *
                (model.getRoomCurrentTempSingle() - model.getStreetOriginT())
                * (1 + model.getHeatLossExtraB()) * model.getCoefficientN() / model.getR0());//(2.2)
        return heatLoss;
    }
}
