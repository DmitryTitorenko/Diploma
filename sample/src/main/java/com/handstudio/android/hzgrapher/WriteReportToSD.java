package com.handstudio.android.hzgrapher;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Dmitry Titorenko on 30.01.2017.
 */

class WriteReportToSD {
    /**
     * The  method used for write Report to SD card.<br>
     */
    public static void writeFileSDFirst(Model model) {
        if (isExternalStorageWritable()) {
            // get path to SD
            File sdPath = Environment.getExternalStorageDirectory();
            // add new DIR
            String DIR_SD = "Report of modeling";
            sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
            // create DIR
            sdPath.mkdirs();
            String FILENAME_SD = "Report.txt";
            File sdFile = new File(sdPath, FILENAME_SD);
            try {
                // open stream to write
                BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
                bw.write("startModeling \n");
                bw.write(model.getStartModeling() + "\n");
                bw.write("endModeling \n");
                bw.write(model.getEndModeling() + "\n");
                bw.write("homeMaxT\n");
                bw.write(model.getRoomMaxT() + "\n");
                bw.write("homeMinT\n");
                bw.write(model.getRoomMinT() + "\n");
                bw.write("homeTimeChangeT\n");
                bw.write(model.getRoomTimeChangeT() + "\n");
                bw.write("homeValueChangeT\n");
                bw.write(model.getRoomValueChangeT() + "\n");
                bw.write("realHeatProductivityN\n");
                bw.write("" + model.getRealHeatProductivityN() + "\n");
                bw.write("realTime\n");
                bw.write("" + model.getRealTime() + "\n");
                bw.write("usingEnergy\n");
                bw.write(model.getUsingEnergy() + "\n");
                bw.write("calculationQHeatLoss\n");
                bw.write(model.getCalculationQHeatLoss() + "\n");
                bw.write("roomCurrentTemp\n");
                bw.write(model.getRoomCurrentTemp() + "\n");
                bw.write("timeByOneModelTime\n");
                bw.write(model.getTimeByOneModelTime() + "\n");

                bw.write("oneEventListTime\n");
                bw.write(model.getOneEventListTime() + "\n");

                bw.write("modelTimeArray\n");
                bw.write(model.getModelTimeArray() + "\n");
                bw.write("realTimeArray");
                bw.write("" + model.getRealTimeArray() + "\n");
                bw.write("eventList\n");
                bw.write(model.getEventList() + "\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The  method used for checks if external storage is available for read and write.<br>
     *
     * @return boolean available or not .
     */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "SD is not available: " + Environment.getExternalStorageState());
        return false;
    }
}
