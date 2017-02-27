package com.handstudio.android.hzgrapher;

import com.handstudio.android.hzgrapher.Model;

import java.util.ArrayList;

/**
 * Created by Dmitry Titorenko on 27.02.2017.
 */

 class SwitchMinToSecond {
    /**
     * The  method used for switch minutes ot seconds .<br>
     * @param model entity witch contain all parameters
     */
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
}
