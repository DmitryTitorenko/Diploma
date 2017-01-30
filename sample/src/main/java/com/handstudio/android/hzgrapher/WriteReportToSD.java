package com.handstudio.android.hzgrapher;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dmitry Titorenko on 30.01.2017.
 */

public class WriteReportToSD {
    public static void writeFileSDFirst(int startModeling,
                                        int endModeling,
                                        int homeMaxT,
                                        int homeMinT,
                                        int streetOriginT,
                                        int homeTimeChangeT,
                                        int homeValueChangeT,
                                        double realHeatProductivityN,
                                        int modelingTime,
                                        double realTime,
                                        ArrayList<Double> roomCurrentTemp,
                                        ArrayList<Double> usingEnergy,
                                        ArrayList<Double> calculationQHeatLoss,
                                        ArrayList<Double> timeByOneModelTme,
                                        ArrayList<Integer> modelTimeArray) {
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
                bw.write(startModeling + "\n");
                bw.write("endModeling \n");
                bw.write(endModeling + "\n");
                bw.write("homeMaxT\n");
                bw.write(homeMaxT + "\n");
                bw.write("homeMinT\n");
                bw.write(homeMinT + "\n");
                bw.write("homeTimeChangeT\n");
                bw.write(homeTimeChangeT + "\n");
                bw.write("homeValueChangeT\n");
                bw.write(homeValueChangeT + "\n");
                bw.write("realHeatProductivityN\n");
                bw.write("" + realHeatProductivityN + "\n");
                bw.write("modelingTime\n");
                bw.write(modelingTime + "\n");
                bw.write("realTime\n");
                bw.write("" + realTime + "\n");
                bw.write("roomCurrentTemp\n");
                bw.write(roomCurrentTemp.toString() + "\n");
                bw.write("usingEnergy\n");
                bw.write(usingEnergy.toString() + "\n");
                bw.write("calculationQHeatLoss\n");
                bw.write(calculationQHeatLoss.toString() + "\n");
                bw.write("timeByOneModelTme\n");
                bw.write(timeByOneModelTme.toString() + "\n");
                bw.write("modelTimeArray\n");
                bw.write(modelTimeArray.toString() + "\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "SD is not available: " + Environment.getExternalStorageState());
        return false;
    }
}
