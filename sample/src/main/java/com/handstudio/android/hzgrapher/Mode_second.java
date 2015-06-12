package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Mode_second extends Activity implements View.OnClickListener{
    private final static int ACTION_EDIT_V = 101;
    final String LOG_TAG = "myLogs";

    public final static String p="p";
    public final static String t_support="t_support";
    public final static String c="c";
    public final static String n="n";
    public final static String a="a";
    public final static String b="b";
    public final static String c_height="c_height";
    public final static String n_loss="n_loss";
    public final static String t_street="t_street";
    public final static String nn="nn";
    public final static String R0="R0";
    public final static String B="B";
    public final static String event_mode_="event_mode_";

    EditText et_p;
    EditText et_t_support;//��������� ����������� �� ���� ������
    EditText et_c;
    EditText et_n;//�����������������������
    EditText et_a;//������
    EditText et_b;//�����
    EditText et_c_height;//������
    EditText et_n_loss;//������������������������
    EditText et_t_street;//����������� �� �����
    EditText et_nn; //����������
    EditText et_R0;//������������� �������������
    EditText et_B;//����������� ��������������

    Button btn_start;


    private List<View> allEds;    //������� ������ ���� ������� ����� �����������
    public ArrayList<Integer>event_mode=new ArrayList<>(); //������ �������
    //������� ����� ������������ ��� ����������� ����������� edittext'ov
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_second);

        et_p=(EditText)findViewById(R.id.et_p);
        et_t_support=(EditText)findViewById(R.id.et_t1);
        et_c=(EditText)findViewById(R.id.et_c);
        et_n=(EditText)findViewById(R.id.et_n);
        et_a=(EditText)findViewById(R.id.et_a);
        et_b=(EditText)findViewById(R.id.et_b);
        et_c_height=(EditText)findViewById(R.id.et_c_height);
        et_n_loss=(EditText)findViewById(R.id.et_n_loss);
        et_t_street=(EditText)findViewById(R.id.et_t_street);
        et_nn=(EditText)findViewById(R.id.et_nn);
        et_R0=(EditText)findViewById(R.id.et_R0);
        et_B=(EditText)findViewById(R.id.et_B);

        btn_start=(Button)findViewById(R.id.btn_start);

        Button addButton = (Button) findViewById(R.id.button);
        Button addButton1 = (Button) findViewById(R.id.button1);
        /*

        //���������������� ��� ������
        allEds = new ArrayList<View>();
        //������� ��� linear ������� � ��� ��� ������� add edittext
        final LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // event_mode.add(1);//��������� ��������� ����������, ��� 1- id ��������� ����������� �� �����
                counter++;
                event_mode.add(counter);//��������� ��������� ����������, ��� 1- id ��������� ����������� �� �����

                //Log.e(LOG_TAG, "counter   " + event_mode.get(counter));
                for(int evement:event_mode){
                    Log.e(LOG_TAG, "counter "+ evement);
                }


                //����� ��� ��������� ������ ������� ����� ���� ��� ���� ������ � ���� ������, ������ ������ ������
                final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);


                Button deleteField = (Button) view.findViewById(R.id.button2);
                deleteField.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ((LinearLayout) view.getParent()).removeView(view);
                            allEds.remove(view);
                            //Iterator<Integer> iterator=event_mode.iterator();
                            event_mode.remove(event_mode.indexOf(counter));
                            event_mode.add(0);

                        } catch(IndexOutOfBoundsException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                //EditText text = (EditText) view.findViewById(R.id.editText);
                TextView text=(TextView)view.findViewById(R.id.textView7);
                text.setText("street_id" + counter);
                //��������� ��� ��� ������� � ������
                allEds.add(view);
                //��������� �������� � linearlayout
                linear.addView(view);
            }
        }

        );
        addButton1.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             event_mode.add(2);//��������� ��������� ����������, ��� 2- id ��������� ����������� �� � ����
                                             counter++;

                                             //����� ��� ��������� ������ ������� ����� ���� ��� ���� ������ � ���� ������, ������ ������ ������
                                             final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);


                                             Button deleteField = (Button) view.findViewById(R.id.button2);
                                             deleteField.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     try {
                                                         ((LinearLayout) view.getParent()).removeView(view);
                                                         allEds.remove(view);
                                                         event_mode.remove(counter);// ����� ���������� ��������

                                                     } catch(IndexOutOfBoundsException ex) {
                                                         ex.printStackTrace();
                                                     }
                                                 }
                                             });

                                             //EditText text = (EditText) view.findViewById(R.id.editText);
                                             TextView text=(TextView)view.findViewById(R.id.textView7);
                                             text.setText("home_id" + counter);
                                             //��������� ��� ��� ������� � ������
                                             allEds.add(view);
                                             //��������� �������� � linearlayout
                                             linear.addView(view);
                                         }
                                     }

        );
        */

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                if( et_p.getText().length()==0
                        || et_t_support.getText().length()==0 || et_t_support.getText().length()==0 || et_c.getText().length()==0
                        || et_n.getText().length()==0
                        || et_a.getText().length()==0 || et_b.getText().length()==0
                        || et_c_height.getText().length()==0|| et_n_loss.getText().length()==0
                        || et_t_street.getText().length()==0 || et_nn.getText().length()==0
                        || et_R0.getText().length()==0 || et_B.getText().length()==0)
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

                else if (Float.valueOf(et_p.getText().toString())<405 || Float.valueOf(et_p.getText().toString())>854.6)
                    Toast.makeText(getApplicationContext(),"405 < p < 854.6",Toast.LENGTH_SHORT).show();

                else if (Float.valueOf(et_c.getText().toString())<1.005 || Float.valueOf(et_c.getText().toString())>1.0301)
                    Toast.makeText(getApplicationContext(),"1.005 < c < 1.0301", Toast.LENGTH_SHORT).show();

                else
                    startActivity(CurveGraphActivity.class);
                break;

            case R.id.btn_add_R0:
                Intent ii=new Intent(this,V.class);
                startActivityForResult(ii, ACTION_EDIT_V);

                break;
        }
    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        i.putExtra(p, et_p.getText().toString());
        i.putExtra(t_support, et_t_support.getText().toString());
        i.putExtra(c, et_c.getText().toString());
        i.putExtra(n, et_n.getText().toString());
        i.putExtra(a, et_a.getText().toString());
        i.putExtra(b, et_b.getText().toString());
        i.putExtra(c_height ,et_c_height.getText().toString());
        i.putExtra(n_loss,et_n_loss.getText().toString());
        i.putExtra(t_street, et_t_street.getText().toString());
        i.putExtra(nn,et_nn.getText().toString());
        i.putExtra(R0,et_R0.getText().toString());
        i.putExtra(B, et_B.getText().toString());
        i.putExtra(event_mode_, event_mode);
        startActivity(i);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ACTION_EDIT_V){// ����� ��������� ����� � ������ activity
            if(resultCode == RESULT_OK) {
                String vvv=data.getStringExtra(V.ANSWER_V);
                et_R0.setText("" + vvv);
            }
        }
    }
}
