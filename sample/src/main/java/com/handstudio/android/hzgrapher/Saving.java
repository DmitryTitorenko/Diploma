package com.handstudio.android.hzgrapher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener {

    private EditText etSleepMax;
    private EditText etSleepMin;
    private EditText etWakeMax;
    private EditText etWakeMin;
    private EditText etStartSleep;
    private EditText etEndSleep;
    private EditText etStreetMax;
    private EditText etStreetMin;
    private EditText etStartTariff;
    private EditText etEndTariff;
    private EditText etPriceTariff;
    private ListView m_listview;
    ArrayList<String> countTariff = new ArrayList<>();
    ArrayList<String> priceTariff = new ArrayList<>();
    ArrayList<String> startTariff = new ArrayList<>();
    ArrayList<String> endTariff = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        etSleepMax = (EditText) findViewById(R.id.etSleepMax);
        etSleepMin = (EditText) findViewById(R.id.etSleepMin);
        etWakeMax = (EditText) findViewById(R.id.etWakeMax);
        etWakeMin = (EditText) findViewById(R.id.etWakeMin);
        etStartSleep = (EditText) findViewById(R.id.etStartSleep);
        etStreetMin = (EditText) findViewById(R.id.etStreetMin);
        etStreetMax = (EditText) findViewById(R.id.etStreetMax);
        etEndSleep = (EditText) findViewById(R.id.etEndSleep);
        etStartTariff = (EditText) findViewById(R.id.etStartTariff);
        etEndTariff = (EditText) findViewById(R.id.etEndTariff);
        etPriceTariff = (EditText) findViewById(R.id.etPriceTariff);
        m_listview = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTariff:
                priceTariff.add(etPriceTariff.getText().toString());
                startTariff.add(etStartTariff.getText().toString());
                endTariff.add(etEndTariff.getText().toString());
                countTariff.add(etStartTariff.getText().toString() + " - " + etEndTariff.getText().toString() + " $ " + etPriceTariff.getText().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countTariff);
                m_listview.setAdapter(adapter);
                break;
            case R.id.btnDeleteTariff:
                countTariff.remove(countTariff.size() - 1);
                priceTariff.remove(priceTariff.size()-1);
                startTariff.remove(startTariff.size()-1);
                endTariff.remove(endTariff.size()-1);
                break;
        }
    }
}
