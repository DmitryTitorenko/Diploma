package com.handstudio.android.hzgrapher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * Created by Grinw on 15.04.2016.
 */
public class Saving extends FragmentActivity implements View.OnClickListener{
    private FragmentManager manager;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving);
        manager = getSupportFragmentManager();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddTariff:
                Tariff tariff=new Tariff();
                FragmentTransaction fragmentTransaction=manager.beginTransaction();
                fragmentTransaction.add(R.id.containerTariff,tariff);
                fragmentTransaction.commit();
                break;
            case R.id.btnDeleteTariff:
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();

        }

    }
}
