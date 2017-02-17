package com.handstudio.android.hzgrapher;

import java.io.Serializable;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {
    private static double sumAttainmentBefore;


    public static void init(Model model) {
        int tempSingleStart = model.getRoomCurrentTempSingle();
        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());
        //checkMinRoomT(model);
        mainAlgorithmBegin(model);
        model.setRealTime(0);
        model.setRoomCurrentTempSingle(tempSingleStart);

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
        checkMinRoomT(model);

        //find time to attainment required temp
        TimeForAttainment.mathTimeForAttainmentExpectancy(model, model.getHomeValueChangeT() - model.getRoomCurrentTempSingle());
        model.setTimeToAttainmentRequiredTempRoom(TimeForAttainment.getTimeByExpectancy());

        if (model.getHomeTimeChangeT() - model.getRealTime() < model.getTimeToAttainmentRequiredTempRoom()) {

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

                if (model.getHomeValueChangeT() == model.getRoomCurrentTempSingle()) {

                    //Support
                    if (sumAttainmentBefore == 0) {
                        model.setEventList(0, Model.eventType.START_SUPPORT.toString());
                    } else {
                        model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                    }

                } else {

                    //Support=>Attainment
                    if (sumAttainmentBefore == 0) {
                        model.setEventList(0, Model.eventType.START_SUPPORT.toString());
                        attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                        findEventsTimeInAttainmentAfterSupport(model, attainmentTemp);
                    } else {
                        model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                        attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                        findEventsTimeInAttainmentAfterSupport(model, attainmentTemp);
                    }
                }
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
            double sumEventsTimeInAttainmentForCheckMinRoom = findEventsTimeInAttainmentForCheckMinRoom(model, attainmentTemp);

            /*
            for (int i = 0; i < attainmentTemp; i++) {
                TimeForAttainment.mathTimeForAttainment(model);
                sumAttainmentBefore+=TimeForAttainment.getTimeByOneModelTme();
            }
            */
            //TimeForAttainment.mathTimeForAttainmentExpectancy(model, attainmentTemp);

            //sumAttainmentBefore=TimeForAttainment.getTimeByExpectancy();
            model.setRealTime(sumEventsTimeInAttainmentForCheckMinRoom);
            sumAttainmentBefore = sumEventsTimeInAttainmentForCheckMinRoom;
            //model.setRoomCurrentTempSingle(model.getHomeMinT());
        }
    }

    /**
     * The  method used for find events, his time & add them in eventList.<br>
     * =>Attainment
     */
    private static void findEventsTimeInAttainment(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        for (double i : arrayTimeByOneAttainmentExpectancy) {
            model.setEventList(i, Model.eventType.START_ATTAINMENT.toString());
        }
    }

    /**
     * The  method used for find events, his time & add them in eventList.<br>
     * when ure checkMinRoomT()
     *
     * @return sum of events time
     */
    private static double findEventsTimeInAttainmentForCheckMinRoom(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        for (double i : arrayTimeByOneAttainmentExpectancy) {
            model.setEventList(i - arrayTimeByOneAttainmentExpectancy[0], Model.eventType.START_ATTAINMENT.toString());
        }
        return arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
    }

    /**
     * The  method used for find events, his time & add them in eventList.<br>
     * Support=>Attainment
     */
    private static void findEventsTimeInAttainmentAfterSupport(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        double e1 = model.getEndModeling() - arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
        for (int i = 0; i < arrayTimeByOneAttainmentExpectancy.length; i++) {
            model.setEventList(i == 0 ? e1 : e1 + arrayTimeByOneAttainmentExpectancy[i - 1], Model.eventType.START_ATTAINMENT.toString());
        }
    }
}
