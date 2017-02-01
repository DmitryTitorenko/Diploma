package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

public class AttainmentMode {
    /**
     * The  method used for modeling attainment mode .<br>
     *
     * @return nothing.
     */
    public static void startAttainment(Model model, int attainmentTemp) {
        for (; model.getRoomCurrentTempSingle() < attainmentTemp; ) {
            if (model.getEvent().equals(Model.eventType.START_ATTAINMENT.toString())) {
                TimeForAttainment.mathTimeForAttainment(model);
                model.stepModeling(TimeForAttainment.getTimeByOneModelTme(), TimeForAttainment.getUsingEnergy(), TimeForAttainment.getCalculationQHeatLoss());
                model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() + 1);
            } else {
            }
        }
    }
}
