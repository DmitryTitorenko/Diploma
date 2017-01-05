package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 05.01.2017.
 */

public class CalculationQHeatLoss {
    /**
     * calculation heat loss.<br>
     *
     * @param a       width
     * @param b       length;
     * @param homeT   temperature in home;
     * @param streetT temperature in street;
     * @param B       additional heat loss ;
     * @param nn      coefficient relatively flat outer surface of the outside air;
     * @param R0      heat resistance;
     * @return float heat loss.
     */
    public static float calculationQHeatLoss(int a, int b, int homeT, int streetT, int B, int nn, int R0) {
        float heatLoss = (a * b * (homeT - streetT) * (1 + B) * nn / R0);//(2.2)
        return heatLoss;
    }
}
