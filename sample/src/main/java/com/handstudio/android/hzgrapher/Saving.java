package com.handstudio.android.hzgrapher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener {


    private EditText etStartModeling; //время начала моделирования
    private EditText etEndModeling; // время окончания моделирования
    private EditText etHomeOriginT;//Origin T at home
    private EditText etHomeMaxT;
    private EditText etHomeMinT;
    private EditText etStreetOriginT; //Origin T at street
    private EditText etStreetMaxT;
    private EditText etStreetMinT;
    private EditText etCountStreetChange;
    private EditText etA;//ширина
    private EditText etB;//длина
    private EditText etCHeight;//высота
    private EditText etP;//атмосферное давление
    private EditText etC;//Удельная теплоёмкость
    private EditText etN;//пеплопроизводительность
    private EditText etNLoss;//холодопроизовдительность
    private EditText etNn; //коэффицент
    private EditText etR0;//R0 коэффицент сопротивление теплопередачи
    private EditText etBExtra;//теплопотери дополнительные

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
        etHomeMaxT = (EditText) findViewById(R.id.etHomeMaxT);
        etHomeMinT = (EditText) findViewById(R.id.etHomeMinT);
        etStreetMaxT = (EditText) findViewById(R.id.etStreetMaxT);
        etHomeMinT = (EditText) findViewById(R.id.etHomeMinT);
        etStartTariff = (EditText) findViewById(R.id.etStartTariff);
        etEndTariff = (EditText) findViewById(R.id.etEndTariff);
        etPriceTariff = (EditText) findViewById(R.id.etPriceTariff);
        m_listview = (ListView) findViewById(R.id.listview);

        etHomeTimeChangeT = (EditText) findViewById(R.id.etHomeTimeChangeT);
        etHomeValueChangeT = (EditText) findViewById(R.id.etHomeValueChangeT);
        lvHomeChangeT = (ListView) findViewById(R.id.lvHomeChangeT);
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
                countChangeTemp.add(etHomeTimeChangeT.getText().toString() + "°C " + etHomeValueChangeT.getText().toString()+"t");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countChangeTemp);
                lvHomeChangeT.setAdapter(adapter1);
                break;

            case R.id.btnStart:
                Intent intent = new Intent(this, CurveGraphActivity.class);
                intent.putExtra("etHomeMaxT", etHomeMaxT.getText().toString());
                startActivity(intent);
        }
    }
}
