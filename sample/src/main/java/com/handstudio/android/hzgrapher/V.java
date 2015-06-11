package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class V extends Activity implements View.OnClickListener{
    EditText et_Lb;
    EditText et_Lh;
    EditText et_R;
    EditText et_L;
    EditText et_J;
    float Lb;
    float Lh;
    float RR;
    float L;
    float J;
    float R0;
    public final static String ANSWER_V = "Answer for V";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r0);
        et_Lb=(EditText)findViewById(R.id.et_lb);
        et_Lh=(EditText)findViewById(R.id.et_Lh);
        et_R=(EditText)findViewById(R.id.et_R);
        et_L=(EditText)findViewById(R.id.et_L);
        et_J=(EditText)findViewById(R.id.et_J);
    }

    @Override
    public void onClick(View v) {
        if( et_Lb.getText().length()==0
                || et_Lh.getText().length()==0 || et_R.getText().length()==0 || et_L.getText().length()==0
                || et_J.getText().length()==0)
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

        else if (Float.valueOf(et_Lb.getText().toString())<5 || Float.valueOf(et_Lb.getText().toString())>20)
            Toast.makeText(getApplicationContext(),"5 < A_b < 20",Toast.LENGTH_SHORT).show();

        else if (Float.valueOf(et_Lh.getText().toString())<5 || Float.valueOf(et_Lh.getText().toString())>50)
            Toast.makeText(getApplicationContext(),"5 < A_h < 50",Toast.LENGTH_SHORT).show();

        else if (Float.valueOf(et_R.getText().toString())<0.1 || Float.valueOf(et_R.getText().toString())>0.3)
            Toast.makeText(getApplicationContext(),"0.1 < Rb.h < 0.3",Toast.LENGTH_SHORT).show();

        else if (Float.valueOf(et_L.getText().toString())<0.1 || Float.valueOf(et_L.getText().toString())>0.8)
            Toast.makeText(getApplicationContext(),"0.1 < L < 0.8",Toast.LENGTH_SHORT).show();

        else if (Float.valueOf(et_J.getText().toString())<0.1 || Float.valueOf(et_J.getText().toString())>0.8)
            Toast.makeText(getApplicationContext(),"0.1 < J < 0.8",Toast.LENGTH_SHORT).show();

        else {
            Lb = Float.valueOf(et_Lb.getText().toString());
            Lh = Float.valueOf(et_Lh.getText().toString());
            RR = Float.valueOf(et_R.getText().toString());
            L = Float.valueOf(et_L.getText().toString());
            J = Float.valueOf(et_J.getText().toString());
            R0 = 1 / Lb + J / L + 1 / Lh + RR;

            Intent i = new Intent();
            i.putExtra(ANSWER_V, "" + R0);
            setResult(RESULT_OK, i);
            finish();
        }
    }

}
