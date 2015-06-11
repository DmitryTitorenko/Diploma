package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer, View.OnClickListener



{

    public  final static String t="T";
    public final static String r="R";
    public final static String q="Q";
    private static final String TAG = "MyActivity";


    private Model model;


    EditText EditText_Q;
    EditText EditText_R;
    EditText EditText_T;
    Button btn1;
    Button btn2;

    String a;


    final String TESTSTRING = new String("Hello Android");


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText_Q=(EditText)findViewById(R.id.etQ);
        EditText_R=(EditText)findViewById(R.id.edR);
        EditText_T=(EditText)findViewById(R.id.edT);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        model=new Model();
        model.addObserver(this);


	}



	public void onClick(View v){
        switch (v.getId()){
            case R.id.btnCurveGraph:
                if (EditText_Q.getText().toString().length()==0 ) {
                    Toast.makeText(getApplicationContext(), "Null Q", Toast.LENGTH_SHORT).show();
                }
                    else if (EditText_R.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Null R", Toast.LENGTH_SHORT).show();
                }
                else if ( EditText_T.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(), "Null T", Toast.LENGTH_SHORT).show();
                }
                    else{
                    startActivity(CurveGraphActivity.class);
                }
                break;


            case R.id.btn1:
                model.SetVlueOfIndex(0);
                break;
            case R.id.btn2:
                model.SetVlueOfIndex(1);
                break;
        }
	}

	private void startActivity(Class<?> cls) {
		Intent i = new Intent(this, cls);
        i.putExtra(t, EditText_T.getText().toString());
        i.putExtra(r,EditText_R.getText().toString());
        i.putExtra(q, EditText_Q.getText().toString());
		startActivity(i);
	}

    @Override
    public void update(Observable observable, Object data) {
        btn1.setText("count: "+model.GetValueOfIndex(0));
        btn2.setText("count: "+model.GetValueOfIndex(1));
    }
}
