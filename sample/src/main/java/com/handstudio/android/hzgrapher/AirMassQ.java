package com.handstudio.android.hzgrapher;

/**
 * Created by Dmitry Titorenko on 30.01.2017.
 */

class AirMassQ {
    /**
     * The  method used for calculation air mass witch need for change temp in  1 °C .<br>
     *
     * @param model entity witch contain all parameters
     * @return double air mass.
     */
    public static double mathAirMassQ(Model model) {
        double tKelvin = model.getRoomCurrentTempSingle() + 273.15;                          // transfer to kelvin
        double densityP = 0.473 * (model.getAtmospherePressureP() / tKelvin);                // (2.4)
        int volumeRomm = model.getWideRoom() * model.getLengthRoom() * model.getHeightRoom();//(2.3)
        double mass = densityP * volumeRomm;                                                 // (2.5)
        return (mass * model.getSpecificHeatC() * 1000);//multiply in 1000 to transfer from kilojoule in joule. (2.6)
    }
}
