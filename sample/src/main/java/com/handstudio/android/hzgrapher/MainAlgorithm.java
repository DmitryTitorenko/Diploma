package com.handstudio.android.hzgrapher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {

    public static void init(Model model) {

        switchMinToSecond(model);
        int tempSingleStart = model.getRoomCurrentTempSingle();
        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());

        checkMinRoomT(model);
        origin(model);
        model.setRealTime(0);
        model.setRoomCurrentTempSingle(tempSingleStart);

        fixInactivityAfterAttainment(model);

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

    // getRoomTimeChangeT() => getCurrentTimeChangeTempRoom()
    // getRoomValueChangeT=>tempEnd
    //getCurrentValueChangeTempRoom =>tempEnd
    //  getRoomValueChangeT()=>getCurrentValueChangeTempRoom()

    private static void mainAlgorithmBegin(Model model) {
        double timeToAttainment = 0;

        //check  should attainment temp in this stage
        if (model.getRoomCurrentTempSingle() < model.getCurrentValueChangeTempRoom()) {
            timeToAttainment = findTimeToAttainmentRequireTemp(model, model.getCurrentValueChangeTempRoom() - model.getRoomCurrentTempSingle());
        }

        //check  should start attainment now
        if (model.getCurrentTimeChangeTempRoom() - model.getRealTime() < timeToAttainment) {

            // =>Attainment
            //and we probably won't attainment her to required time
            int attainmentTemp = model.getCurrentValueChangeTempRoom() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainment(model, attainmentTemp);
        } else {/*

            if (model.getRoomCurrentTempSingle() > model.getRoomMinT()) {

                //Inactivity
                for (String st = "Start"; model.getRoomCurrentTempSingle() > model.getRoomMinT() & st.equals("Start") &
                        model.getRealTime() < model.getEndModeling(); ) {

                    InactivityMode.inactivityStart(model, InactivityMode.modelingOrExpectancy.EXPECTANCY.toString());

                    if (model.getRoomCurrentTempSingle() <= model.getRoomValueChangeT()) {

                        if (model.getRoomTimeChangeT() < model.getRealTime() + findTimeToAttainmentRequireTemp(model)) {

                            //Inactivity=>Attainment
                            findEventsTimeInAttainmentAfterSupport(model, model.getRoomValueChangeT() - model.getRoomCurrentTempSingle());
                            st = "End";
                        } else if (model.getRoomCurrentTempSingle() == model.getRoomMinT()) {

                            //Inactivity=>Support
                            model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                            checkAttainmentAfterSupport(model);
                            st = "End";
                        }

                    }
                    model.setEventList(model.getRealTime() - InactivityMode.getTimeByOneModelTme(), Model.eventType.START_INACTIVITY.toString());
                }

            } else {

                //Support
                model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                checkAttainmentAfterSupport(model);

            }*/
        }
    }


    private static void origin(Model model) {
        //if (!model.getRoomTimeChangeT().isEmpty()&&)
        if (!model.getRoomTimeChangeT().isEmpty() && model.getCurrentTimeChangeTempRoom() <= model.getCurrentEndTariff()) {

            //next event- change temp in this tariff
            mathBranch(model, model.getCurrentValueChangeTempRoom(), model.getCurrentTimeChangeTempRoom());
        } else {
            if (model.getNextPriсeTariff() > model.getCurrentPriсeTariff()) {

                // next tariff more expensive
                mathBranch(model, model.getRoomMaxT(), model.getCurrentEndTariff());

            } else {

                // next tariff less expensive
                //check should set temp in next tariff + start attainment in this tariff

                double startAttainmentInThisTariff = isAttainmentShouldStartInThisTariff(model);
                if(startAttainmentInThisTariff!=0){
                    model.setEventList(startAttainmentInThisTariff, Model.eventType.START_ATTAINMENT.toString());
                }


            }
        }
    }


    /**
     * The  method used for match branch .<br>
     *
     * @param tempEnd store temp in the end of iteration
     */


    // getRoomTimeChangeT() => getCurrentTimeChangeTempRoom()
    // getRoomValueChangeT=>tempEnd
    //getCurrentValueChangeTempRoom =>tempEnd
    //  getRoomValueChangeT()=>getCurrentValueChangeTempRoom()
    private static void mathBranch(Model model, int tempEnd, int timeEnd) {


        if (model.getRoomCurrentTempSingle() > model.getRoomMinT()) {

            //Inactivity
            for (String st = "Start"; model.getRoomCurrentTempSingle() > model.getRoomMinT() & st.equals("Start") &
                    model.getRealTime() < model.getEndModeling(); ) {

                InactivityMode.inactivityStart(model, InactivityMode.modelingOrExpectancy.EXPECTANCY.toString());

                if (model.getRoomCurrentTempSingle() <= tempEnd) {

                    if (timeEnd < model.getRealTime() + findTimeToAttainmentRequireTemp(model, model.getCurrentValueChangeTempRoom() - model.getRoomCurrentTempSingle())) {

                        //Inactivity=>Attainment
                        findEventsTimeInAttainmentAfterSupport(model, tempEnd - model.getRoomCurrentTempSingle(), timeEnd);
                        st = "End";
                    } else if (model.getRoomCurrentTempSingle() == model.getRoomMinT()) {

                        //Inactivity=>Support
                        model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
                        checkAttainmentAfterSupport(model, timeEnd);
                        st = "End";
                    }

                }
                model.setEventList(model.getRealTime() - InactivityMode.getTimeByOneModelTme(), Model.eventType.START_INACTIVITY.toString());
            }

        } else {

            //Support
            model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
            checkAttainmentAfterSupport(model, timeEnd);

        }
    }


    private static void checkAttainmentAfterSupport(Model model, int timeEnd) {
        if (!model.getRoomTimeChangeT().isEmpty() && model.getCurrentValueChangeTempRoom() != model.getRoomCurrentTempSingle()) {

            //Support=>Attainment
            int attainmentTemp = model.getCurrentValueChangeTempRoom() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainmentAfterSupport(model, attainmentTemp, timeEnd);
        }
    }


    /**
     * The  method used for check is current temp smaller then minimum value
     * if current temp lower then min, raise her - find time for every events and add to eventList.<br>
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getRoomMinT()) {
            int attainmentTemp = model.getRoomMinT() - model.getRoomCurrentTempSingle();
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
    private static void findEventsTimeInAttainmentAfterSupport(Model model, int attainmentTemp, int timeEnd) {
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        double e1 = timeEnd - arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
        for (int i = 0; i < arrayTimeByOneAttainmentExpectancy.length; i++) {
            model.setEventList(i == 0 ? e1 : e1 + arrayTimeByOneAttainmentExpectancy[i - 1], Model.eventType.START_ATTAINMENT.toString());
        }
    }

    /**
     * The  method used for find time to attainment require temp.<br>
     *
     * @return require time
     */
    //tempToAttainment= model.getCurrentValueChangeTempRoom() - model.getRoomCurrentTempSingle()
    private static double findTimeToAttainmentRequireTemp(Model model, int tempToAttainment) {
        int roomCurrentTempSingle = model.getRoomCurrentTempSingle();
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, tempToAttainment);
        model.setRoomCurrentTempSingle(roomCurrentTempSingle);
        return arrayTimeByOneAttainmentExpectancy.length == 0 ? 0 : arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
    }

    private static void fixInactivityAfterAttainment(Model model) {
        for (int i = 0; !Objects.equals(model.getCurrentEventType(model.getCurrentEventKey(i)), Model.eventType.END_MODELING.toString()); i++) {
            if (model.getCurrentEventType((model.getCurrentEventKey(i))).equals(Model.eventType.START_ATTAINMENT.toString()) &
                    model.getCurrentEventType((model.getCurrentEventKey(i + 1))).equals(Model.eventType.START_INACTIVITY.toString())) {
                model.delEvent(i + 1);
                model.delEvent(i);
                model.delEvent(i - 1);
            }
        }
    }


    public static void switchMinToSecond(Model model) {

        ArrayList<Integer> switchMinToSecond = new ArrayList<>();
        for (int i = 0; i < model.getRoomTimeChangeT().size(); i++) {
            switchMinToSecond.add(model.getRoomTimeChangeT().get(i) * 60);
        }
        model.setRoomTimeChangeT(switchMinToSecond);
        switchMinToSecond = new ArrayList<>();

        for (int i = 0; i < model.getEndTariff().size(); i++) {
            switchMinToSecond.add(model.getEndTariff().get(i) * 60);
        }
        model.setEndTariff(switchMinToSecond);
        switchMinToSecond = new ArrayList<>();// why if use switchMinToSecond.clear => delete all elements in endTariff?

        for (int i = 0; i < model.getStartTariff().size(); i++) {
            switchMinToSecond.add(model.getStartTariff().get(i) * 60);
        }
        model.setStartTariff(switchMinToSecond);
    }

    /**
     * The  method used for find should attainment start in this tariff to attainment change temp in next tariff.<br>
     *
     * @return require time to start attainment in this tariff
     */

    private static double isAttainmentShouldStartInThisTariff(Model model) {
        double timeToAttainmentChangeTempInNextTariff = 0;
        double timeToStartAttainmentInThisTariff = 0;
        if (model.isNextTariffAvailable()) {
            for (int a : model.getRoomTimeChangeT()) {
                if (a < model.getNextEndTariff() && a > model.getNextStartTariff()) { // find is change temp in next tariff
                    timeToAttainmentChangeTempInNextTariff = a + findTimeToAttainmentRequireTemp(model, model.getCurrentValueChangeTempRoom() - model.getRoomMinT());
                    if (timeToAttainmentChangeTempInNextTariff - model.getNextStartTariff() > 0) {
                        timeToStartAttainmentInThisTariff = timeToAttainmentChangeTempInNextTariff - model.getNextStartTariff();
                    }
                }
            }
        }
        return timeToStartAttainmentInThisTariff;
    }
}
