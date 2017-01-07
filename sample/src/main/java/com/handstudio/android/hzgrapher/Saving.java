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


    private EditText etStartModeling; //время начала моделирования
    private EditText etEndModeling; // время окончания моделирования
    private EditText etHomeOriginT;//Origin T at home
    private EditText etHomeMaxT;
    private EditText etHomeMinT;

    private EditText etStreetOriginT; //Origin T at street
    private EditText etStreetMaxT;
    private EditText etStreetMinT;
    private EditText etCountStreetChange;

    private EditText etWide;//ширина
    private EditText etLength;//длина
    private EditText etHeight;//высота
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
        etStartModeling = (EditText) findViewById(R.id.etStartModeling);
        etEndModeling = (EditText) findViewById(R.id.etEndModeling);
        etHomeOriginT = (EditText) findViewById(R.id.etHomeOriginT);
        etHomeMaxT = (EditText) findViewById(R.id.etHomeMaxT);
        etHomeMinT = (EditText) findViewById(R.id.etHomeMinT);

        etWide = (EditText) findViewById(R.id.et_wide);
        etLength = (EditText) findViewById(R.id.et_length);
        etHeight = (EditText) findViewById(R.id.et_height);
        etP = (EditText) findViewById(R.id.et_p);
        etC = (EditText) findViewById(R.id.et_c);
        etN = (EditText) findViewById(R.id.et_n);
        etNLoss = (EditText) findViewById(R.id.et_n_loss);
        etNn = (EditText) findViewById(R.id.et_nn);
        etR0 = (EditText) findViewById(R.id.et_R0);
        etBExtra = (EditText) findViewById(R.id.et_B);

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
        i.putExtra("startModeling", etStartModeling.getText().toString());
        i.putExtra("endModeling", etEndModeling.getText().toString());
        i.putExtra("homeOriginT", etHomeOriginT.getText().toString());
        i.putExtra("homeMaxT", etHomeMaxT.getText().toString());
        i.putExtra("homeMinT", etHomeMinT.getText().toString());
        i.putExtra("wide", etWide.getText().toString());
        i.putExtra("length", etLength.getText().toString());
        i.putExtra("height", etHeight.getText().toString());
        i.putExtra("p", etP.getText().toString());
        i.putExtra("c", etC.getText().toString());
        i.putExtra("n", etN.getText().toString());
        i.putExtra("nLoss", etNLoss.getText().toString());
        i.putExtra("nn", etNn.getText().toString());
        i.putExtra("r0", etR0.getText().toString());
        i.putExtra("nExtra", etBExtra.getText().toString());
        i.putExtra("homeTimeChangeT", etHomeTimeChangeT.getText().toString());
        i.putExtra("homeValueChangeT", etHomeValueChangeT.getText().toString());
        startActivity(i);
    }
}
