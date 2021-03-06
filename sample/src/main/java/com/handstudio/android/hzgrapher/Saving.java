package com.handstudio.android.hzgrapher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener, Serializable {

    private final static int ACTION_EDIT_R0 = 101;//идентификатор запроса к ValueR0
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

    //tariff store
    private ArrayList<Integer> startTariff = new ArrayList<>();
    private ArrayList<Integer> endTariff = new ArrayList<>();
    private ArrayList<Double> priceTariff = new ArrayList<>();
    private ArrayList<String> countTariff = new ArrayList<>();
    private ArrayAdapter<String> adapterStoreTariff;


    //change tempRoom store
    private ArrayList<Integer> timeHomeChangeT = new ArrayList<>();
    private ArrayList<Integer> valueHomeChange = new ArrayList<>();
    private ArrayList<String> countChangeTemp = new ArrayList<>();
    private ArrayAdapter<String> adapterStoreNewTemp;


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
        etCoefficientN = (EditText) findViewById(R.id.et_CoefficientN);
        etR0 = (EditText) findViewById(R.id.et_R0);
        etHeatLossExtraB = (EditText) findViewById(R.id.et_HeatLossExtraB);


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
            case R.id.btn_add_R0:
                Intent i = new Intent(this, ValueR0.class);
                startActivityForResult(i, ACTION_EDIT_R0);
                break;
            case R.id.btnAddTariff:
                startTariff.add(Integer.valueOf(etStartTariff.getText().toString()));
                endTariff.add(Integer.valueOf(etEndTariff.getText().toString()));
                priceTariff.add(Double.valueOf(etPriceTariff.getText().toString()));
                countTariff.add(etStartTariff.getText().toString() + " - " + etEndTariff.getText().toString() + "хв " + etPriceTariff.getText().toString()+"коп");
                adapterStoreTariff = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countTariff);
                m_listview.setAdapter(adapterStoreTariff);

                //upd old layout
                int stepTariff = endTariff.get(0) - startTariff.get(0);
                etStartTariff.setText("" + endTariff.get(endTariff.size() - 1));
                etEndTariff.setText("" + (endTariff.get(endTariff.size() - 1) + stepTariff));
                break;

            case R.id.btnDelTariff:
                if (!adapterStoreTariff.isEmpty()) {
                    //upd old layout
                    stepTariff = endTariff.get(0) - startTariff.get(0);
                    etStartTariff.setText("" + (startTariff.size() == 1 ? 0 : (startTariff.get(startTariff.size() - 1) )));
                    etEndTariff.setText("" + (endTariff.size() == 1 ? stepTariff : (endTariff.get(endTariff.size() - 1) )));

                    adapterStoreTariff.remove(adapterStoreTariff.getItem(adapterStoreTariff.getCount() - 1));
                    startTariff.remove(startTariff.size() - 1);
                    endTariff.remove(endTariff.size() - 1);
                    priceTariff.remove(priceTariff.size() - 1);
                }
                break;

            case R.id.btnAddNewTemp:
                timeHomeChangeT.add(Integer.valueOf(etHomeTimeChangeT.getText().toString()));
                valueHomeChange.add(Integer.valueOf(etHomeValueChangeT.getText().toString()));
                countChangeTemp.add(etHomeTimeChangeT.getText().toString() + "хв " + etHomeValueChangeT.getText().toString() + "°C ");
                adapterStoreNewTemp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countChangeTemp);
                lvHomeChangeT.setAdapter(adapterStoreNewTemp);
                break;

            case R.id.btnDelTemp:
                if (!adapterStoreNewTemp.isEmpty()) {
                    adapterStoreNewTemp.remove(adapterStoreNewTemp.getItem(adapterStoreNewTemp.getCount() - 1));

                    timeHomeChangeT.remove(timeHomeChangeT.size()-1);
                    valueHomeChange.remove(valueHomeChange.size()-1);
                }
                break;

            case R.id.btnStartSaving:
                Model model = new Model(
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
                        Double.valueOf(etCoefficientN.getText().toString()),
                        Double.valueOf(etR0.getText().toString()),
                        Double.valueOf(etHeatLossExtraB.getText().toString()),

                        timeHomeChangeT,
                        valueHomeChange,
                        startTariff,
                        endTariff,
                        priceTariff);
                //model.mainAlgorithmBegin();
                MainAlgorithm.init(model);
                //myStartActivity(Graph.class, mainAlgorithm);
                break;
            default:
                break;
        }
    }

    private void myStartActivity(Class<?> cls, MainAlgorithm mainAlgorithm) {
        Intent i = new Intent(this, cls);
        i.putExtra("Data", mainAlgorithm);
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_EDIT_R0) {// может приходить ответ с разных activity
            if (resultCode == RESULT_OK) {
                String answerR0 = data.getStringExtra(ValueR0.ANSWER_R0);
                etR0.setText("" + answerR0);
            }
        }
    }
}
