package com.handstudio.android.hzgrapher;

import java.io.Serializable;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {
    private static double[] arrayTimeByOneAttainmentExpectancy;

    public static void init(Model model) {

        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());

        checkMinRoomT(model);

        //mainAlgorithmBegin(model);
        for (int i = 0; i < arrayTimeByOneAttainmentExpectancy.length && model.getCurrentEventType(arrayTimeByOneAttainmentExpectancy[i]) != Model.eventType.END_MODELING.toString(); i++) {

            if (model.getCurrentEventType(arrayTimeByOneAttainmentExpectancy[i]).equals(Model.eventType.START_ATTAINMENT.toString())) {
                AttainmentMode.startAttainment(model);
            }
        }
        // write result to SD
        WriteReportToSD.writeFileSDFirst(model);


    }


    public static void mainAlgorithmBegin(Model model) {
        checkMinRoomT(model);


        //find time to attainment required temp
        TimeForAttainment.mathTimeForAttainmentExpectancy(model, model.getRoomCurrentTempSingle() - model.getHomeValueChangeT());
        model.setTimeToAttainmentRequiredTempRoom(TimeForAttainment.getTimeByExpectancy());

        if (model.getHomeTimeChangeT() - model.getRealTime() < model.getTimeToAttainmentRequiredTempRoom()) {

            // =>Attainment and we probably can't attainment her to required temp
            //find double[] events time & add them in eventList
            for (double arrayTimeByOneAttainmentExpectancy : AttainmentMode.startAttainmentExpectancy(model, model.getRoomCurrentTempSingle() - model.getHomeValueChangeT())) {
                model.setEventList(arrayTimeByOneAttainmentExpectancy, Model.eventType.START_ATTAINMENT.toString());
            }

        } else {

            //find time, when we need start attainment
            double startForAttainment = model.getTimeToAttainmentRequiredTempRoom() + model.getHomeTimeChangeT();
            if (model.getRoomCurrentTempSingle() > model.getHomeMinT()) {

                //Inactivity=>Attainment
                model.setEventList(model.getRealTime(), Model.eventType.START_INACTIVITY.toString());
                model.setEventList(startForAttainment, Model.eventType.START_ATTAINMENT.toString());

                //find should we add start Support
                int countTempInInactivity = model.getRoomCurrentTempSingle() - model.getHomeMinT(); // count down temp in inactivity
                double endDuringInactivity = InactivityMode.inactivityExpectancy(model, countTempInInactivity);//max inactivity during
                if (endDuringInactivity < startForAttainment) {

                    //Inactivity=>Support=>Attainment
                    model.setEventList(endDuringInactivity, Model.eventType.START_SUPPORT.toString());
                }
            } else {

                //Support=>Attainment
                model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                model.setEventList(startForAttainment, Model.eventType.START_ATTAINMENT.toString());
            }
        }

/*
        InactivityMode.inactivityStart(model);
        SupportMode.energySupport(model, model.getRealTime(), model.getEndModeling() - model.getRealTime());
        model.setEvent(Model.eventType.END_MODELING.toString());
        */


    }

    /**
     * The  method used for check is current temp smaller then minimum value
     * if current temp lower then min, raise her .<br>
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getHomeMinT()) {

            //model.setEvent(Model.eventType.START_ATTAINMENT.toString());
            //AttainmentMode.startAttainment(model, model.getHomeMinT());
            arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, model.getHomeMinT() - model.getRoomCurrentTempSingle());
            for (double i : arrayTimeByOneAttainmentExpectancy) {
                model.setEventList(i, Model.eventType.START_ATTAINMENT.toString());
            }
        }
    }
}
