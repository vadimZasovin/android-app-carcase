package com.imogene.android.carcase.controller;

/**
 * Created by Admin on 12.04.2017.
 */

public abstract class NavigationDrawerFragment extends BaseFragment{

    int navigationDestinationId;

    @Override
    public final int getNavigationDestinationId(){
        return navigationDestinationId;
    }
}