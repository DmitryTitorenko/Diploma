package com.handstudio.android.hzgrapher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by GrinWey on 13.06.2015.
 */
public class EventHome extends Fragment {
    public final static String TAG = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_home, null);
    }
}