package com.imogene.android.carcase.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;

/**
 * Created by Admin on 12.04.2017.
 */

public abstract class BottomNavigationActivity extends NavigableActivity {

    private BottomNavigationView navigationView;

    protected static final int MODE_FIXED = 1;
    protected static final int MODE_FLOATING = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNavigable()){
            navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
            if(navigationView != null){
                onPrepareBottomNavigationView(navigationView);
            }
        }
    }

    protected void onPrepareBottomNavigationView(@NonNull BottomNavigationView navigationView){}

    protected int getMode(){
        return MODE_FIXED;
    }

    @Override
    protected int getLayoutRes() {
        if(isNavigable()){
            int mode = getMode();
            return mode == MODE_FIXED ?
                    R.layout.activity_bottom_navigation_fixed :
                    R.layout.activity_bottom_navigation_floating;
        }
        return super.getLayoutRes();
    }

    @Override
    protected boolean isNavigable() {
        return true;
    }

    public final BottomNavigationView getNavigationView(){
        return navigationView;
    }

    public final boolean hasNavigationView(){
        return navigationView != null;
    }

    @Override
    public final int getNavigationDestinationId() {
        if(hasNavigationView()){
            return navigationView.getSelectedItemId();
        }else {
            return -1;
        }
    }

    public final void setNavigationDestination(int itemId){
        if(hasNavigationView()){
            navigationView.setSelectedItemId(itemId);
        }
    }
}