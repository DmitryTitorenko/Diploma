package com.handstudio.android.hzgrapher;

import java.util.*;

/**
 * Created by Dmitry Titorenko on 06.02.2017.
 */
    /*
    This class consists exclusively of static methods witch using for get current event from eventList.
     */
public class EventList {
    private static Map<Integer, String> eventList = new TreeMap<>();


    public static void main(String[] args) {
        //eventList.remove()
    }
    /**
     * The  method used for get current event.<br>
     *
     * @return double heat loss.
     */
    public static int getCurrentEvent(int i) {
        return (int) eventList.keySet().toArray()[i];
    }
}
