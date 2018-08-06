package com.imogene.android.carcase.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by Admin on 12.04.2017.
 */

public abstract class NavigationDrawerFragment extends BaseFragment{

    private static final String STATE_NAVIGATION_DESTINATION_ID =
            "com.imogene.android.carcase.NavigationDrawerFragment.STATE_NAVIGATION_DESTINATION_ID";

    int navigationDestinationId;

    @Override
    public final int getNavigationDestinationId(){
        return navigationDestinationId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            navigationDestinationId = savedInstanceState.getInt(STATE_NAVIGATION_DESTINATION_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_NAVIGATION_DESTINATION_ID, navigationDestinationId);
    }
}