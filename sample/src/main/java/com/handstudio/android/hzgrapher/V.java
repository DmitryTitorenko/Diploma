package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class V extends Activity implements View.OnClickListener {
    private EditText et_Lb;
    private EditText et_Lh;
    private EditText et_R;
    private EditText et_L;
    private EditText et_J;
    public final static String ANSWER_V = "Answer for V";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r0);
        et_Lb = (EditText) findViewById(R.id.et_lb);
        et_Lh = (EditText) findViewById(R.id.et_Lh);
        et_R = (EditText) findViewById(R.id.et_R);
        et_L = (EditText) findViewById(R.id.et_L);
        et_J = (EditText) findViewById(R.id.et_J);
    }

    @Override
    public void onClick(View v) {
        if (et_Lb.getText().length() == 0
                || et_Lh.getText().length() == 0 || et_R.getText().length() == 0 || et_L.getText().length() == 0
                || et_J.getText().length() == 0)
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

        else {
            float Lb = Float.valueOf(et_Lb.getText().toString());
            float Lh = Float.valueOf(et_Lh.getText().toString());
            float RR = Float.valueOf(et_R.getText().toString());
            float L = Float.valueOf(et_L.getText().toString());
            float J = Float.valueOf(et_J.getText().toString());

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
                float R0 = 1 / Lb + J / L + 1 / Lh + RR;
                Intent i = new Intent();
                i.putExtra(ANSWER_V, "" + R0);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }
}
