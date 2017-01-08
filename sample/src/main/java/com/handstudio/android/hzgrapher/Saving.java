package com.handstudio.android.hzgrapher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener {

    public final static String startModeling = "startModeling";
    public final static String endModeling = "endModeling";
    public final static String homeOriginT = "homeOriginT";
    public final static String homeMaxT = "homeMaxT";
    public final static String homeMinT = "homeMinT";
    public final static String wideRoom = "wideRoom";
    public final static String lengthRoom = "lengthRoom";
    public final static String heightRoom = "heightRoom";
    public final static String atmospherePressureP = "AtmospherePressureP";
    public final static String specificHeatC = "specificHeatC";
    public final static String heatProductivityN = "heatProductivityN";
    public final static String coolingProductivityN = "coolingProductivityN";
    public final static String coefficientN = "coefficientN";
    public final static String r0 = "r0";
    public final static String heatLossExtraB = "heatLossExtraB";
    public final static String homeTimeChangeT = "homeTimeChangeT";
    public final static String homeValueChangeT = "homeValueChangeT";


    private EditText etStartModeling; //время начала моделирования
    private EditText etEndModeling; // время окончания моделирования
    private EditText etHomeOriginT;//Origin T at home
    private EditText etHomeMaxT;
    private EditText etHomeMinT;

    private EditText etStreetOriginT; //Origin T at street
    private EditText etStreetMaxT;
    private EditText etStreetMinT;
    private EditText etCountStreetChange;

    private EditText etWideRoom;//ширина
    private EditText etLengthRoom;//длина
    private EditText etHeightRoom;//высота
    private EditText etAtmospherePressureP;//атмосферное давление
    private EditText etSpecificHeatC;//Удельная теплоёмкость
    private EditText etHeatProductivityN;//теплопроизводительность
    private EditText etCoolingProductivityN;//холодопроизовдительность
    private EditText etCoefficientN; //коэффицент
    private EditText etR0;//R0 коэффицент сопротивление теплопередачи
    private EditText etHeatLossExtraB;//теплопотери дополнительные

    //tariff
    private EditText etStartTariff;
    private EditText etEndTariff;
    private EditText etPriceTariff;
    private ListView m_listview;

    //change T at home
    private EditText etHomeTimeChangeT;
    private EditText etHomeValueChangeT;
    private ListView lvHomeChangeT;

    ArrayList<String> countTariff = new ArrayList<String>();
    ArrayList<String> priceTariff = new ArrayList<String>();
    ArrayList<String> startTariff = new ArrayList<String>();
    ArrayList<String> endTariff = new ArrayList<String>();

    ArrayList<String> countChangeTemp = new ArrayList<String>();
    ArrayList<String> timeHomeChangeT = new ArrayList<String>();
    ArrayList<String> valueHomeChange = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        etStartModeling = (EditText) findViewById(R.id.etStartModeling);
        etEndModeling = (EditText) findViewById(R.id.etEndModeling);
        etHomeOriginT = (EditText) findViewById(R.id.etHomeOriginT);
        etHomeMaxT = (EditText) findViewById(R.id.etHomeMaxT);
        etHomeMinT = (EditText) findViewById(R.id.etHomeMinT);

        etWideRoom = (EditText) findViewById(R.id.et_wideRoom);
        etLengthRoom = (EditText) findViewById(R.id.et_lengthRoom);
        etHeightRoom = (EditText) findViewById(R.id.et_heightRoom);
        etAtmospherePressureP = (EditText) findViewById(R.id.et_AtmospherePressureP);
        etSpecificHeatC = (EditText) findViewById(R.id.et_SpecificHeatC);
        etHeatProductivityN = (EditText) findViewById(R.id.et_HeatProductivityN);
        etCoolingProductivityN = (EditText) findViewById(R.id.et_CoolingProductivityN);
        etCoefficientN = (EditText) findViewById(R.id.et_CoefficientN);
        etR0 = (EditText) findViewById(R.id.et_R0);
        etHeatLossExtraB = (EditText) findViewById(R.id.et_HeatLossExtraB);

        /*
        etStartTariff = (EditText) findViewById(R.id.etStartTariff);
        etEndTariff = (EditText) findViewById(R.id.etEndTariff);
        etPriceTariff = (EditText) findViewById(R.id.etPriceTariff);
        m_listview = (ListView) findViewById(R.id.listview);
*/
        etHomeTimeChangeT = (EditText) findViewById(R.id.etHomeTimeChangeT);
        etHomeValueChangeT = (EditText) findViewById(R.id.etHomeValueChangeT);
        //lvHomeChangeT = (ListView) findViewById(R.id.lvHomeChangeT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTariff:
                priceTariff.add(etPriceTariff.getText().toString());
                startTariff.add(etStartTariff.getText().toString());
                endTariff.add(etEndTariff.getText().toString());
                countTariff.add(etStartTariff.getText().toString() + " - " + etEndTariff.getText().toString() + "$ " + etPriceTariff.getText().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countTariff);
                m_listview.setAdapter(adapter);
                break;
            case R.id.btnAddNewTemp:
                timeHomeChangeT.add(etHomeTimeChangeT.getText().toString());
                valueHomeChange.add(etHomeValueChangeT.getText().toString());
                countChangeTemp.add(etHomeTimeChangeT.getText().toString() + "°C " + etHomeValueChangeT.getText().toString() + "t");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countChangeTemp);
                lvHomeChangeT.setAdapter(adapter1);
                break;
            case R.id.btnStartSaving:
                //Toast.makeText(getApplicationContext(),"go",Toast.LENGTH_LONG);
                startActivity(MainAlgorithm.class);
        }
    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        i.putExtra(startModeling, etStartModeling.getText().toString());
        i.putExtra(endModeling, etEndModeling.getText().toString());
        i.putExtra(homeOriginT, etHomeOriginT.getText().toString());
        i.putExtra(homeMaxT, etHomeMaxT.getText().toString());
        i.putExtra(homeMinT, etHomeMinT.getText().toString());
        i.putExtra(wideRoom, etWideRoom.getText().toString());
        i.putExtra(lengthRoom, etLengthRoom.getText().toString());
        i.putExtra(heightRoom, etHeightRoom.getText().toString());
        i.putExtra(atmospherePressureP, etAtmospherePressureP.getText().toString());
        i.putExtra(specificHeatC, etSpecificHeatC.getText().toString());
        i.putExtra(heatProductivityN, etHeatProductivityN.getText().toString());
        i.putExtra(coolingProductivityN, etCoolingProductivityN.getText().toString());
        i.putExtra(coefficientN, etCoefficientN.getText().toString());
        i.putExtra(r0, etR0.getText().toString());
        i.putExtra(heatLossExtraB, etHeatLossExtraB.getText().toString());
        i.putExtra(homeTimeChangeT, etHomeTimeChangeT.getText().toString());
        i.putExtra(homeValueChangeT, etHomeValueChangeT.getText().toString());
        startActivity(i);
    }
}
