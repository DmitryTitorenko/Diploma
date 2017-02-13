package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 01.02.2017.
 */

class AttainmentMode {

    /**
     * The  method used for modeling attainment mode .<br>
     */
    public static void startAttainment(Model model) {
        TimeForAttainment.mathTimeForAttainment(model);
        model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() + 1);
        model.stepModeling(TimeForAttainment.getTimeByOneModelTme(), TimeForAttainment.getUsingEnergy(),
                TimeForAttainment.getCalculationQHeatLoss(), TimeForAttainment.getRealHeatProductivityN());
    }

    /**
     * The  method used for calculation every start event time in attainment mode .<br>
     *
     * @param attainmentTemp count of temp witch need find start time.
     * @return array witch contain every events start time.
     */
    public static double[] startAttainmentExpectancy(Model model, int attainmentTemp) {
        int startRoomCurrentTemp = model.getRoomCurrentTempSingle();
        int startRoomCurrentTemp1 = model.getRoomCurrentTempSingle();

        //store event time
        double[] arrayTimeByOneAttainmentExpectancy = new double[attainmentTemp];
        for (int i = 0; startRoomCurrentTemp1 < startRoomCurrentTemp + attainmentTemp; i++) {
            TimeForAttainment.mathTimeForAttainment(model);
            if (i == 0) {
                arrayTimeByOneAttainmentExpectancy[i] = TimeForAttainment.getTimeByOneModelTme();
            } else {
                arrayTimeByOneAttainmentExpectancy[i] = TimeForAttainment.getTimeByOneModelTme() + arrayTimeByOneAttainmentExpectancy[i - 1];
            }
            startRoomCurrentTemp1++;
            model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() + 1);
        }
        return arrayTimeByOneAttainmentExpectancy;
    }
}
