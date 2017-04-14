package com.imogene.android.carcase.controller;

/**
 * Created by Admin on 12.04.2017.
 */

public abstract class NavigableActivity extends BaseActivity {

    protected boolean isNavigable(){
        return false;
    }

    protected int getNavigationDestinationId(){
        return 0;
    }
}