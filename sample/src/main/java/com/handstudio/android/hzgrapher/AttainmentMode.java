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
        model.stepModeling(TimeForAttainment.getTimeByOneModelTme(), TimeForAttainment.getUsingEnergy(), TimeForAttainment.getCalculationQHeatLoss(), TimeForAttainment.getRealHeatProductivityN());


    }

    public static double[] startAttainmentExpectancy(Model model, int attainmentTemp) {

        int startRoomCurrentTemp=model.getRoomCurrentTempSingle();

        //store event time
        double[] arrayTimeByOneAttainmentExpectancy = new double[attainmentTemp];
        for (int i = 0; model.getRoomCurrentTempSingle() < startRoomCurrentTemp+attainmentTemp; i++) {
            TimeForAttainment.mathTimeForAttainment(model);
            arrayTimeByOneAttainmentExpectancy[i] = TimeForAttainment.getTimeByOneModelTme();
            model.setRoomCurrentTempSingle(model.getRoomCurrentTempSingle() + 1);
        }
        return arrayTimeByOneAttainmentExpectancy;
    }


}
