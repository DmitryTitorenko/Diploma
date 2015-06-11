package com.handstudio.android.hzgrapher;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by GrinWey on 05.04.2015.
 */
public class Model extends Observable {
    private List<Integer> myList;


    public Model() {
        myList=new ArrayList<Integer>(2);
        myList.add(0);
        myList.add(0);
    }
    public int GetValueOfIndex(final int the_index) throws IndexOutOfBoundsException{
        return myList.get(the_index);
    }
    public void SetVlueOfIndex(final  int the_index) throws IndexOutOfBoundsException{
        myList.set(the_index,myList.get(the_index)+1);
        setChanged();
        notifyObservers();
    }
}
