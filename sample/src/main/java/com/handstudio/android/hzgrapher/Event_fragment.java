package com.handstudio.android.hzgrapher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GrinWey on 13.06.2015.
 */
public class Event_fragment extends Fragment {
    public final static String TAG="";
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // View view = inflater.inflate(R.layout.fragment_event_layout, container, false);
       // return view;
        return inflater.inflate(R.layout.fragment_event_layout, null);

    }
    public void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.textView_event_fragment);
        textView.setText(text);
    }
}