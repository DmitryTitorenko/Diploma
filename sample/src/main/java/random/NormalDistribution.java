package random;

import static java.lang.Math.*;


/**
 * Created by Dmitry Titorenko on 13.03.2017.
 */

public class NormalDistribution {
    private static boolean checkAnswer = false;
    private static double outputRandom1;
    private static double outputRandom2;


    private static void math(double inputRandom1, double inputRandom2) {
        double v1 = -1 + 2 * inputRandom1;
        double v2 = -1 + 2 * inputRandom2;
        double S = sqrt(pow(v1, 2) + pow(v2, 2));
        if (S < 1) {
            setCheckAnswer(true);
            outputRandom1 = v1 * sqrt((-2 * log(S)) / S);
            outputRandom2 = v2 * sqrt((-2 * log(S)) / S);
        }
    }


    public static void setCheckAnswer(boolean checkAnswer) {
        NormalDistribution.checkAnswer = checkAnswer;
    }

    public static double getOutputRandom1() {
        return outputRandom1;
    }

    public static double getOutputRandom2() {
        return outputRandom2;
    }
}
