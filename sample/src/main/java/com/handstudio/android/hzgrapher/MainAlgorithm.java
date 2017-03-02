package com.handstudio.android.hzgrapher;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Dmitry Titorenko on 07.01.2017.
 */

class MainAlgorithm implements Serializable {

    /**
     * The  method used for initialization algorithm.<br>
     *
     * @param model entity witch contain all parameters
     */

    public static void init(Model model) {

        SwitchMinToSecond.switchMinToSecond(model);
        int tempSingleStart = model.getRoomCurrentTempSingle();
        model.setEventList(model.getEndModeling(), Model.eventType.END_MODELING.toString());

        checkMinRoomT(model);
        checkShouldStartAttainmentNow(model, model.getCurrentValueChangeTempRoom(), model.getCurrentTimeChangeTempRoom());

        origin(model);
        model.setRealTime(0);
        model.setRoomCurrentTempSingle(tempSingleStart);

        //fixInactivityAfterAttainment(model);

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

    /**
     * The  method used for split modelling on iterations .<br>
     *
     * @param model entity witch contain all parameters
     */
    private static void origin(Model model) {
        for (; model.getRealTime() < model.getEndModeling(); ) {

            if (!model.getRoomTimeChangeT().isEmpty() && model.getCurrentTimeChangeTempRoom() <= model.getCurrentEndTariff()) {

                //next event - change temp in this tariff
                mathBranch(model, model.getCurrentValueChangeTempRoom(), model.getCurrentTimeChangeTempRoom());
                model.setRealTime(model.getCurrentTimeChangeTempRoom());
            } else {
                if (model.getNextPriсeTariff() > model.getCurrentPriсeTariff()) {

                    // next tariff more expensive
                    mathBranch(model, model.getRoomMaxT(), model.getCurrentEndTariff());
                    model.setRealTime(model.getCurrentEndTariff());
                    model.setNewIndexCurrentTariffForIteration();

                } else {

                    //next tariff <=expensive
                    //check should start attainment in this tariff if should isAttainmentShouldStartInThisTariff=true
                    if (isAttainmentShouldStartInThisTariff(model)) {
                        mathBranch(model, model.getCurrentValueChangeTempRoom(), model.getCurrentTimeChangeTempRoom());
                        model.setRealTime(model.getCurrentTimeChangeTempRoom());
                        model.setNewIndexCurrentChangeRoomForIteration();

                    } else {
                        mathBranch(model, model.getRoomMinT(), model.getCurrentEndTariff());
                        model.setRealTime(model.getCurrentEndTariff());
                        model.setNewIndexCurrentTariffForIteration();
                    }
                }
            }
        }
    }


    /**
     * The  method used for match branch for every iteration.<br>
     *
     * @param model   entity witch contain all parameters
     * @param tempEnd store temp witch would be in the end of iteration
     * @param timeEnd store time witch would be in the end of iteration
     */
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
                        checkAttainmentAfterSupport(model, tempEnd, timeEnd);
                        st = "End";
                    }
                }
                model.setEventList(model.getRealTime() - InactivityMode.getTimeByOneModelTme(), Model.eventType.START_INACTIVITY.toString());
            }

        } else {

            //Support
            model.setEventList(model.getRealTime(), Model.eventType.START_SUPPORT.toString());
            if (tempEnd != model.getRoomMinT()) {
                checkAttainmentAfterSupport(model, tempEnd, timeEnd);
            }
        }
    }

    /**
     * The  method used for switch should start attainment after support, if yes - start it use findEventsTimeInAttainmentAfterSupport().<br>
     *
     * @param model   entity witch contain all parameters
     * @param tempEnd store temp witch would be in the end of iteration
     * @param timeEnd store time witch would be in the end of iteration
     */
    private static void checkAttainmentAfterSupport(Model model, int tempEnd, int timeEnd) {
        if (!model.getRoomTimeChangeT().isEmpty() && model.getRoomCurrentTempSingle() != tempEnd) {

            //Support=>Attainment
            int attainmentTemp = tempEnd - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainmentAfterSupport(model, attainmentTemp, timeEnd);
        }
    }


    /**
     * The  method used for check is current temp smaller then minimum value
     * if current temp lower then min, raise her - find time for every events and add to eventList.<br>
     *
     * @param model entity witch contain all parameters
     */
    private static void checkMinRoomT(Model model) {
        if (model.getRoomCurrentTempSingle() < model.getRoomMinT()) {
            int attainmentTemp = model.getRoomMinT() - model.getRoomCurrentTempSingle();
            findEventsTimeInAttainmentForCheckMinRoom(model, attainmentTemp);
        }
    }

    /**
     * The  method used for find events, his time & add them in eventList.<br>
     *
     * @param model          entity witch contain all parameters
     * @param attainmentTemp count of temp witch need attainment.
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
     *
     * @param model          entity witch contain all parameters
     * @param attainmentTemp count of temp witch need attainment.
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
     *
     * @param model          entity witch contain all parameters
     * @param attainmentTemp count of temp witch need attainment.
     * @param timeEnd        store time witch would be in the end of iteration
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
     * @param model          entity witch contain all parameters
     * @param attainmentTemp count of temp witch need attainment.
     * @return require time
     */
    private static double findTimeToAttainmentRequireTemp(Model model, int attainmentTemp) {
        int roomCurrentTempSingle = model.getRoomCurrentTempSingle();
        double[] arrayTimeByOneAttainmentExpectancy = AttainmentMode.startAttainmentExpectancy(model, attainmentTemp);
        model.setRoomCurrentTempSingle(roomCurrentTempSingle);
        return arrayTimeByOneAttainmentExpectancy.length == 0 ? 0 : arrayTimeByOneAttainmentExpectancy[arrayTimeByOneAttainmentExpectancy.length - 1];
    }

    /**
     * The  method used fix inactivity after attainment.<br>
     *
     * @param model entity witch contain all parameters
     */

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


    /**
     * The  method used for find should attainment start in this tariff for attainment change temp in next tariff.<br>
     *
     * @param model entity witch contain all parameters
     * @return true if it should start attainment in this tariff
     */

    private static boolean isAttainmentShouldStartInThisTariff(Model model) {
        boolean check = false;
        if (model.isNextTariffAvailable()) {
            for (int a : model.getRoomTimeChangeT()) {
                if (a < model.getNextEndTariff() && a > model.getNextStartTariff()) { // find is change temp in next tariff
                    double timeToAttainmentChangeTempInNextTariff = a - findTimeToAttainmentRequireTemp(model, model.getCurrentValueChangeTempRoom() - model.getRoomMinT());
                    if (timeToAttainmentChangeTempInNextTariff < model.getNextStartTariff()) {
                        check = true;
                    }
                }
            }
        }
        return check;
    }

    /**
     * The  method used for check should we start attainment now if yes, start it .<br>
     *
     * @param model   entity witch contain all parameters
     * @param tempEnd store temp witch would be in the end of iteration
     * @param timeEnd store time witch would be in the end of iteration
     */
    private static void checkShouldStartAttainmentNow(Model model, int tempEnd, int timeEnd) {
        double timeToAttainment = 0;

        //check  should attainment temp in this stage
        if (model.getRoomCurrentTempSingle() < tempEnd) {
            timeToAttainment = findTimeToAttainmentRequireTemp(model, tempEnd - model.getRoomCurrentTempSingle());
        }

        //check  should start attainment now
        if (timeEnd - model.getRealTime() < timeToAttainment) {

            // =>Attainment
            //and we probably won't attainment her to required time
            findEventsTimeInAttainment(model, tempEnd - model.getRoomCurrentTempSingle());
        }
    }
}
