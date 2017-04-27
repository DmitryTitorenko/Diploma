package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ValueR0 extends Activity implements View.OnClickListener {
    private EditText etLb;
    private EditText etLh;
    private EditText etR;
    private EditText etL;
    private EditText etJ;
    public final static String ANSWER_R0 = "Answer for R0";
    private static double R0;
    private static double Lb;  //коефіцієнт теплосприймання приміщення
    private static double Lh;  //коефіцієнт тепловіддачі приміщення
    private static double RR;  //Замкнутий повітряний прошарок
    private static double L;   //Коефіцієнт теплопровідності для матеріалу
    private static double J;   //Товщина цього шару


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.value_r0);
        etLb = (EditText) findViewById(R.id.et_lb);
        etLh = (EditText) findViewById(R.id.et_Lh);
        etR = (EditText) findViewById(R.id.et_R);
        etL = (EditText) findViewById(R.id.et_L);
        etJ = (EditText) findViewById(R.id.et_J);
    }

    @Override
    public void onClick(View v) {
        if (!ValidationIsNull() && ValidationParam()) {
            mathR0();
            returnR0();
        }
    }

    /**
     * The  method used for check  is param == Null.<br>
     *
     * @return false  if param is't Null.
     */
    private boolean ValidationIsNull() {
        boolean checkIsNull = true;
        if (etLb.getText().length() == 0 || etLh.getText().length() == 0
                || etR.getText().length() == 0 || etL.getText().length() == 0
                || etJ.getText().length() == 0)
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        else {
            Lb = Float.valueOf(etLb.getText().toString());
            Lh = Float.valueOf(etLh.getText().toString());
            RR = Float.valueOf(etR.getText().toString());
            L = Float.valueOf(etL.getText().toString());
            J = Float.valueOf(etJ.getText().toString());
            checkIsNull = false;
        }
        return checkIsNull;
    }

    /**
     * The  method used for validation parameters R0 .<br>
     *
     * @return false if param don't correct and true if correct.
     */
    private boolean ValidationParam() {
        boolean checkIsCorrect = false;
        if (Lb < 5 || Lb > 20)
            Toast.makeText(getApplicationContext(), "5 < Коефіцієнт теплосприймання приміщення < 20", Toast.LENGTH_SHORT).show();

        else if (Lh < 5 || Lh > 50)
            Toast.makeText(getApplicationContext(), "5 < Коефіцієнт тепловіддачі приміщення < 50", Toast.LENGTH_SHORT).show();

        else if (RR < 0.1 || RR > 0.3)
            Toast.makeText(getApplicationContext(), "0.1 < Замкнутий повітряний прошарок < 0.3", Toast.LENGTH_SHORT).show();

        else if (L < 0.1 || L > 0.8)
            Toast.makeText(getApplicationContext(), "0.1 < Коефіцієнт теплопровідності для матеріалу < 0.8", Toast.LENGTH_SHORT).show();

        else if (J < 0.1 || J > 0.8)
            Toast.makeText(getApplicationContext(), "0.1 < Товщина цього шару < 0.8", Toast.LENGTH_SHORT).show();

        else {
            checkIsCorrect = true;
        }
        return checkIsCorrect;
    }


    /**
     * The  method used for calculation R0 .<br>
     */
    private static void mathR0() {
        R0 = 1 / Lb + J / L + 1 / Lh + RR;
    }

    /**
     * The  method used for return value R0 .<br>
     */
    private void returnR0() {
        Intent i = new Intent();
        i.putExtra(ANSWER_R0, "" + R0);
        setResult(RESULT_OK, i);
        finish();
    }
}
