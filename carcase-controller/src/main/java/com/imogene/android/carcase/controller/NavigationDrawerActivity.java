package com.imogene.android.carcase.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by Admin on 12.04.2017.
 */

public abstract class NavigationDrawerActivity extends NavigableActivity
        implements OnBackPressListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNavigable()){
            drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            setupNavigationDrawerFragment();
        }
    }

    @Override
    protected int getLayoutRes() {
        return isNavigable() ? R.layout.activity_navigation_drawer : super.getLayoutRes();
    }

    @Override
    protected boolean isNavigable(){
        return true;
    }

    @Override
    protected abstract int getNavigationDestinationId();

    private void setupNavigationDrawerFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.navigationDrawerFragmentContainer);
        if(fragment == null){
            fragment = createNavigationDrawerFragment();
            if(fragment != null){
                NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) fragment;
                drawerFragment.navigationDestinationId = getNavigationDestinationId();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.navigationDrawerFragmentContainer, fragment);
                transaction.commit();
            }
        }
    }

    protected abstract NavigationDrawerFragment createNavigationDrawerFragment();

    @Nullable
    public final NavigationDrawerFragment getNavigationDrawerFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.navigationDrawerFragmentContainer);
        if(fragment != null){
            return (NavigationDrawerFragment) fragment;
        }
        return null;
    }

    public final boolean hasNavigationDrawer(){
        return drawerLayout != null;
    }

    public final boolean isNavigationDrawerOpened(){
        return hasNavigationDrawer() && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public final void openNavigationDrawer(){
        if(hasNavigationDrawer() && !isNavigationDrawerOpened()){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public final void closeNavigationDrawer(){
        if(isNavigationDrawerOpened()){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onInterceptBackPressed() {
        if(isNavigationDrawerOpened()){
            closeNavigationDrawer();
            return true;
        } else {
            return false;
        }
    }
}