package com.handstudio.android.hzgrapher;

import java.io.Serializable;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {

    public static void mainAlgorithmBegin(Model model) {
        checkMinRoomT(model);
        SupportMode.energySupport(model, model.getEndModeling() - model.getStartModeling());
        model.setEvent(Model.eventType.END_MODELING.toString());
        if (model.getEvent().equals(Model.eventType.END_MODELING.toString())) {
            // write result to SD
            WriteReportToSD.writeFileSDFirst(model);
        } else {
        }
    }

    /**
     * The  method used for check is current temp smaller then minimum value
     * if current temp lower then min, raise her .<br>
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getHomeMinT()) {
            model.setEvent(Model.eventType.START_ATTAINMENT.toString());
            AttainmentMode.startAttainment(model, model.getHomeMinT());
        }
    }
}
