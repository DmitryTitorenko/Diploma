package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

class AttainmentMode {
    /**
     * The  method used for modeling attainment mode .<br>
     */
    public static void startAttainment(Model model, int attainmentTemp) {
        for (; model.getRoomCurrentTempSingle() < attainmentTemp; ) {
            if (model.getEvent().equals(Model.eventType.START_ATTAINMENT.toString())) {
                TimeForAttainment.mathTimeForAttainment(model);
                model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() + 1);
                model.stepModeling(TimeForAttainment.getTimeByOneModelTme(), TimeForAttainment.getUsingEnergy(), TimeForAttainment.getCalculationQHeatLoss(), TimeForAttainment.getRealHeatProductivityN());
            } else {
            }
        }
    }
}
