package com.handstudio.android.hzgrapher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.handstudio.android.hzgrapherlib.vo.*;
import com.handstudio.android.hzgrapherlib.vo.curvegraph.CurveGraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener, Serializable {

    private static EditText etStartModeling; //время начала моделирования
    private static EditText etEndModeling; // время окончания моделирования
    private static EditText etHomeOriginT;//Origin T at home
    private static EditText etHomeMaxT;
    private static EditText etHomeMinT;
    private static EditText etStreetOriginT; //Origin T at street
    private static EditText etStreetMaxT;
    private static EditText etStreetMinT;
    private static EditText etCountStreetChange;
    private static EditText etWideRoom;//ширина
    private static EditText etLengthRoom;//длина
    private static EditText etHeightRoom;//высота
    private static EditText etAtmospherePressureP;//атмосферное давление
    private static EditText etSpecificHeatC;//Удельная теплоёмкость
    private static EditText etHeatProductivityN;//теплопроизводительность
    private static EditText etCoolingProductivityN;//холодопроизовдительность
    private static EditText etCoefficientN; //коэффицент
    private static EditText etR0;//R0 коэффицент сопротивление теплопередачи
    private static EditText etHeatLossExtraB;//теплопотери дополнительные

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
        etStreetOriginT = (EditText) findViewById(R.id.etStreetOriginT);
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
                MainAlgorithm mainAlgorithm = new MainAlgorithm(
                        Integer.valueOf(etStartModeling.getText().toString()),
                        Integer.valueOf(etEndModeling.getText().toString()),
                        Integer.valueOf(etHomeOriginT.getText().toString()),
                        Integer.valueOf(etHomeMaxT.getText().toString()),
                        Integer.valueOf(etHomeMinT.getText().toString()),
                        Integer.valueOf(etStreetOriginT.getText().toString()),
                        Integer.valueOf(etWideRoom.getText().toString()),
                        Integer.valueOf(etLengthRoom.getText().toString()),
                        Integer.valueOf(etHeightRoom.getText().toString()),
                        Double.valueOf(etAtmospherePressureP.getText().toString()),
                        Double.valueOf(etSpecificHeatC.getText().toString()),
                        Double.valueOf(etHeatProductivityN.getText().toString()),
                        Double.valueOf(etCoolingProductivityN.getText().toString()),
                        Double.valueOf(etCoefficientN.getText().toString()),
                        Double.valueOf(etR0.getText().toString()),
                        Double.valueOf(etHeatLossExtraB.getText().toString()),
                        Integer.valueOf(etHomeTimeChangeT.getText().toString()),
                        Integer.valueOf(etHomeValueChangeT.getText().toString()));
                mainAlgorithm.mainAlgorithmBegin();
                //myStartActivity(Graph.class, mainAlgorithm);
        }
    }

    private void myStartActivity(Class<?> cls, MainAlgorithm mainAlgorithm) {
        Intent i = new Intent(this, cls);
        i.putExtra("Data", mainAlgorithm);
        startActivity(i);
    }
}
