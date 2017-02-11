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

            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_SUPPORT.toString())) {
                SupportMode.energySupport(model, model.getCurrentEventKey(model.getModelingTime() + 1) - model.getCurrentEventKey(model.getModelingTime()));
            }

            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_INACTIVITY.toString())) {
                InactivityMode.inactivityStart(model);
            }
        }


        // write result to SD
        WriteReportToSD.writeFileSDFirst(model);
    }


    public static void mainAlgorithmBegin(Model model) {
        int attainmentTemp;// count change temp in attainment
        //checkMinRoomT(model);

        //find time to attainment required temp
        TimeForAttainment.mathTimeForAttainmentExpectancy(model, model.getHomeValueChangeT() - model.getRoomCurrentTempSingle());
        model.setTimeToAttainmentRequiredTempRoom(TimeForAttainment.getTimeByExpectancy());

        if (model.getHomeTimeChangeT() - model.getRealTime() < TimeForAttainment.getTimeByExpectancy()) {

            // =>Attainment
            //and we probably won't attainment her to required time
            attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainment(model, attainmentTemp);

        } else {

            //find time, when we need start attainment
            double startForAttainment = model.getTimeToAttainmentRequiredTempRoom() + model.getHomeTimeChangeT();

            if (model.getRoomCurrentTempSingle() > model.getHomeMinT()) {
                //Check
                //Inactivity=>Attainment
                double[] arrayTimeToChangeOneTemp = InactivityMode.inactivityExpectancy(model, startForAttainment - model.getRealTime());
                for (double i : arrayTimeToChangeOneTemp) {
                    model.setEventList(i, Model.eventType.START_INACTIVITY.toString());
                }
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
                model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());

                attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                findEventsTimeInAttainment(model, attainmentTemp);
            }
        }
    }

    /**
     * The  method used for check is current temp smaller then minimum value
     * if current temp lower then min, raise her - find time for every events and add to eventList.<br>
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getHomeMinT()) {
            int attainmentTemp = model.getHomeMinT() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainment(model, attainmentTemp);
        }
    }

    //find events, his time & add them in eventList
    private static void findEventsTimeInAttainment(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        for (double i : arrayTimeByOneAttainmentExpectancy) {
            model.setEventList(i, Model.eventType.START_ATTAINMENT.toString());
        }
    }

    private static void findEventsTimeInAttainmentBeforeSupprot(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        for (double i : arrayTimeByOneAttainmentExpectancy) {
            model.setEventList(i, Model.eventType.START_ATTAINMENT.toString());
        }
    }
}
