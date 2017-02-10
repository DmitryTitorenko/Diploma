package com.handstudio.android.hzgrapher;

import java.io.Serializable;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {

    public static void init(Model model) {
        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());
        mainAlgorithmBegin(model);
        for (int i = 0; model.getCurrentEventType(model.getCurrentEventKey(i)) != Model.eventType.END_MODELING.toString(); i++) {
            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_ATTAINMENT.toString())) {
                AttainmentMode.startAttainment(model);
            }
            //if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_SUPPORT.toString())) {
          //      SupportMode.energySupport(model);
            //}
            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_INACTIVITY.toString())) {
                InactivityMode.inactivityStart(model);
            }

        }


        // write result to SD
        WriteReportToSD.writeFileSDFirst(model);
    }


    public static void mainAlgorithmBegin(Model model) {
        checkMinRoomT(model);


        //find time to attainment required temp
        TimeForAttainment.mathTimeForAttainmentExpectancy(model, model.getHomeValueChangeT() - model.getRoomCurrentTempSingle());
        model.setTimeToAttainmentRequiredTempRoom(TimeForAttainment.getTimeByExpectancy());

        if (model.getHomeTimeChangeT() - model.getRealTime() < TimeForAttainment.getTimeByExpectancy()) {

            // =>Attainment and we probably can't attainment her to required temp
            //find double[] events time & add them in eventList
            double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, model.getHomeValueChangeT() - model.getRoomCurrentTempSingle());
            for (double timeByOneAttainmentExpectancy : arrayTimeByOneAttainmentExpectancy) {
                model.setEventList(timeByOneAttainmentExpectancy, Model.eventType.START_ATTAINMENT.toString());
            }

        }
        else {

            //find time, when we need start attainment
            double startForAttainment = model.getTimeToAttainmentRequiredTempRoom() + model.getHomeTimeChangeT();

            //model.setEventList(startForAttainment,Model.eventType.START_INACTIVITY.toString());

            if (model.getRoomCurrentTempSingle() > model.getHomeMinT()) {

                //Inactivity=>Attainment
                double[] arrayTimeToChangeOneTemp=InactivityMode.inactivityExpectancy(model,startForAttainment-model.getRealTime());
                for (double i: arrayTimeToChangeOneTemp){
                    model.setEventList(i,Model.eventType.START_INACTIVITY.toString());

                }

                //model.setEventList(model.getRealTime(), Model.eventType.START_INACTIVITY.toString());
                //model.setEventList(startForAttainment, Model.eventType.START_ATTAINMENT.toString());


                /*
                //find should we add start Support
                int countTempInInactivity = model.getRoomCurrentTempSingle() - model.getHomeMinT(); // count down temp in inactivity
                double endDuringInactivity = InactivityMode.inactivityExpectancy(model, countTempInInactivity);//max inactivity during
                if (endDuringInactivity < startForAttainment) {

                    //Inactivity=>Support=>Attainment
                    model.setEventList(endDuringInactivity, Model.eventType.START_SUPPORT.toString());
                }*/
            } else {

                //Support=>Attainment
                //model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                //model.setEventList(startForAttainment, Model.eventType.START_ATTAINMENT.toString());
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
     * if current temp lower then min, raise her - find time for every events and add to eventList.<br>
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getHomeMinT()) {
            double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, model.getHomeMinT() - model.getRoomCurrentTempSingle());
            for (double i : arrayTimeByOneAttainmentExpectancy) {
                model.setEventList(i, Model.eventType.START_ATTAINMENT.toString());
            }
        }
    }
}
