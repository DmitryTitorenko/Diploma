package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class Q_heat_loss extends Activity implements  View.OnClickListener{
    private List<View> add_b;//создание списка view кот. должны создаваться
    private int counter=0;

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_heat_loss);
        Button addButton = (Button) findViewById(R.id.button);
        add_b = new ArrayList<View>();// инициализация массива
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.button:
                counter++;
                final View view = getLayoutInflater().inflate(R.layout.add_b, null);
                Button deleteField = (Button) view.findViewById(R.id.btn_delete_fragment);
                deleteField.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ((LinearLayout) view.getParent()).removeView(view);
                            add_b.remove(view);
                        } catch (IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                EditText text = (EditText) view.findViewById(R.id.editText);
                text.setText("" + counter);
                //добавляем все что создаем в массив
                add_b.add(view);
                //добавляем елементы в linearlayout
                linear.addView(view);
            }}



        });


    }
    public void gotoR0(View v){
        Intent i=new Intent(this,V.class);
        startActivity(i);
    }
}



