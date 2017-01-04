package com.handstudio.android.hzgrapher;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Created by GrinWey on 10.12.2016.
 */

public class RandomTemperature {
    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(random(6)));
    }

    public static double[][] random(int countInterval) {
        double durationInterval = 24 / countInterval;
        double durationInterval1 = durationInterval;
        double[][] randomTime = new double[countInterval][2];
        for (int i = 0; i < countInterval; i++) {
            randomTime[i][0] = ThreadLocalRandom.current().nextDouble(durationInterval1 - durationInterval, durationInterval1);
            durationInterval1 += durationInterval;
            if (randomTime[i][0] < 12)
                randomTime[i][1] = 1;
            else randomTime[i][1] = -1;
        }
        return randomTime;
    }
}
