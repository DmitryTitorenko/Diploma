package com.handstudio.android.hzgrapher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Mode_second extends FragmentActivity implements View.OnClickListener{
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
    public final static String eventArray_="eventArray";

    private Event_fragment event_fragment;
    private Event_fragment_home event_fragment_home;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    Fragment fragment;

    EditText et_p;
    EditText et_t_support;//поддержка температуры на этом уровне
    EditText et_c;
    EditText et_n;//пеплопроизводительность
    EditText et_a;//ширина
    EditText et_b;//длина
    EditText et_c_height;//высота
    EditText et_n_loss;//холодопроизовдительность
    EditText et_t_street;//температура на улице
    EditText et_nn; //коэффицент
    EditText et_R0;//сопротивление теплопередачи
    EditText et_B;//теплопотери дополнительные
    Button btn_start;

    public ArrayList<Integer>event_mode=new ArrayList<>(); //список событий

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

        manager=getSupportFragmentManager();
    }

public void onClick_Event(View view) {
    switch (view.getId()) {
        case R.id.btn_event:
            event_mode.add(0);//street
            event_fragment=new Event_fragment();
            transaction = manager.beginTransaction();
            transaction.add(R.id.container, event_fragment, event_fragment.TAG);
            transaction.commit();
            break;

        case R.id.bnt_event2:
            event_mode.add(1);// home
            event_fragment_home=new Event_fragment_home();
            transaction=manager.beginTransaction();
            transaction.add(R.id.container, event_fragment_home, event_fragment_home.TAG);
            transaction.commit();
            break;

        case R.id.btn_delete_event:
            fragment=getSupportFragmentManager().findFragmentByTag("");
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            event_mode.remove(event_mode.size()-1);
            break;
    }
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
        i.putExtra(eventArray_, event_mode);
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ACTION_EDIT_V){
            if(resultCode == RESULT_OK) {
                String vvv=data.getStringExtra(V.ANSWER_V);
                et_R0.setText("" + vvv);
            }
        }
    }
}
