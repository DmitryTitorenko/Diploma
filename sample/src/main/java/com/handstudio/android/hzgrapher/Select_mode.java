package com.handstudio.android.hzgrapher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Select_mode extends Activity implements View.OnClickListener{
    final String LOG_TAG = "myLogs";
    int t_street;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        t_street=-1 + (int)(Math.random() * ((2) + 1));
        Log.e(LOG_TAG, "asdf " +t_street );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mode1:
                Intent intent=new Intent(Select_mode.this ,Mode_first.class);
                startActivity(intent);
                break;
            case R.id.btn_mode2:
                Intent i=new Intent(Select_mode.this, Mode_second.class);
                startActivity(i);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
