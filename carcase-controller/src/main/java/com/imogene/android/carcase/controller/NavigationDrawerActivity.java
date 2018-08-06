package com.imogene.android.carcase.controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
            drawerLayout = findViewById(R.id.drawerLayout);
            setupNavigationDrawerFragment();
        }
    }

    @Override
    protected int getLayoutRes() {
        if(isNavigable()){
            if(isTabletLand() && isNavigationDrawerPermanentOnTabletLand()){
                return R.layout.activity_navigation_drawer_permanent;
            } else {
                return R.layout.activity_navigation_drawer;
            }
        } else {
            return super.getLayoutRes();
        }
    }

    protected boolean isNavigationDrawerPermanentOnTabletLand(){ return true; }

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