package com.handstudio.android.hzgrapher;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {

    public static void init(Model model) {
        int tempSingleStart = model.getRoomCurrentTempSingle();
        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());
        checkMinRoomT(model);
        mainAlgorithmBegin(model);
        model.setRealTime(0);
        model.setRoomCurrentTempSingle(tempSingleStart);

        for (int i = 0; !Objects.equals(model.getCurrentEventType(model.getCurrentEventKey(i)), Model.eventType.END_MODELING.toString()); i++) {

            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_ATTAINMENT.toString())) {
                AttainmentMode.startAttainment(model);
            }

            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_SUPPORT.toString())) {
                SupportMode.energySupport(model, model.getCurrentEventKey(model.getModelingTime() + 1) - model.getCurrentEventKey(model.getModelingTime()));
            }

            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_INACTIVITY.toString())) {
                InactivityMode.inactivityStart(model, InactivityMode.modelingOrExpectancy.MODELING.toString());
            }
        }

        // write result to SD
        WriteReportToSD.writeFileSDFirst(model);
    }

    private static void mainAlgorithmBegin(Model model) {
        double timeToAttainment = 0;
        if (model.getRoomCurrentTempSingle() < model.getHomeValueChangeT()) {
            timeToAttainment = findTimeToAttainmentRequireTemp(model);
        }

        if (model.getHomeTimeChangeT() - model.getRealTime() < timeToAttainment) {

            // =>Attainment
            //and we probably won't attainment her to required time
            int attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainment(model, attainmentTemp);

        } else {

            if (model.getRoomCurrentTempSingle() > model.getHomeMinT()) {

                //Inactivity
                for (String st = "Start"; model.getRoomCurrentTempSingle() > model.getHomeMinT() & st.equals("Start"); ) {

                    InactivityMode.inactivityStart(model, InactivityMode.modelingOrExpectancy.EXPECTANCY.toString());

                    if (model.getRoomCurrentTempSingle() <= model.getHomeValueChangeT()) {
                        if (model.getHomeTimeChangeT() < model.getRealTime() + findTimeToAttainmentRequireTemp(model)) {
                            //Inactivity=>Attainment
                            int attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                            findEventsTimeInAttainment(model, attainmentTemp);
                            st = "End";
                        } else {
                            if (model.getRoomCurrentTempSingle() == model.getHomeMinT()) {

                                //Inactivity=>Support
                                model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());

                                if (model.getHomeValueChangeT() != model.getRoomCurrentTempSingle()) {

                                    //Inactivity=>Support=>Attainment
                                    int attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                                    findEventsTimeInAttainmentAfterSupport(model, attainmentTemp);
                                }
                                st = "End";
                            }
                        }
                    }
                    model.setEventList(model.getRealTime() - InactivityMode.getTimeByOneModelTme(), Model.eventType.START_INACTIVITY.toString());
                }


            } else {

                //Support
                model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());

                if (model.getHomeValueChangeT() != model.getRoomCurrentTempSingle()) {

                    //Support=>Attainment
                    int attainmentTemp = model.getHomeValueChangeT() - model.getRoomCurrentTempSingle();
                    findEventsTimeInAttainmentAfterSupport(model, attainmentTemp);
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
            findEventsTimeInAttainmentForCheckMinRoom(model, attainmentTemp);
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
     * when use checkMinRoomT()
     */
    private static void findEventsTimeInAttainmentForCheckMinRoom(Model model, int attainmentTemp) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        for (double i : arrayTimeByOneAttainmentExpectancy) {
            model.setEventList(i - arrayTimeByOneAttainmentExpectancy[0], Model.eventType.START_ATTAINMENT.toString());
        }
        // set time witch need to attainment this temp
        model.setRealTime(arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1]);
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

    /**
     * The  method used for find time to attainment require temp.<br>
     *
     * @return require time
     */
    private static double findTimeToAttainmentRequireTemp(Model model) {
        int roomCurrentTempSingle = model.getRoomCurrentTempSingle();
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, model.getHomeValueChangeT() - model.getRoomCurrentTempSingle());
        model.setRoomCurrentTempSingle(roomCurrentTempSingle);
        return arrayTimeByOneAttainmentExpectancy.length == 0 ? 0 : arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
    }
}
