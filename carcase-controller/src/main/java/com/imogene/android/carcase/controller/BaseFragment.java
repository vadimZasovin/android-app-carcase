package com.imogene.android.carcase.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imogene.android.carcase.commons.util.AppUtils;

/**
 * Created by Admin on 11.04.2017.
 */

public abstract class BaseFragment extends Fragment implements OnBackPressListener {

    protected BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        @LayoutRes int layoutRes = getLayoutRes();
        return inflater.inflate(layoutRes, container, false);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public boolean onInterceptBackPressed() {
        return false;
    }

    public final void openSoftKeyboard() {
        AppUtils.Commons.openSoftKeyboard(this);
    }

    public final void closeSoftKeyboard() {
        AppUtils.Commons.closeSoftKeyboard(this);
    }

    public final boolean checkNetworkAvailability() {
        return activity.checkNetworkAvailability();
    }

    public final boolean checkApiLevel(int requiredApiLevel) {
        return activity.checkApiLevel(requiredApiLevel);
    }

    public final int convertDpsInPixels(int dps) {
        return activity.convertDpsInPixels(dps);
    }

    public final float convertDpsInPixels(float dps) {
        return activity.convertDpsInPixels(dps);
    }

    public final void showToast(@StringRes int messageRes, boolean lengthLong) {
        activity.showToast(messageRes, lengthLong);
    }

    public final void showToast(@StringRes int messageRes, int length) {
        activity.showToast(messageRes, length);
    }

    public final void showToast(String message, boolean lengthLong) {
        activity.showToast(message, lengthLong);
    }

    public final void showToast(String message, int length) {
        activity.showToast(message, length);
    }

    public final boolean hasNavigationDrawer(){
        try {
            NavigationDrawerActivity drawerActivity = (NavigationDrawerActivity) activity;
            return drawerActivity.hasNavigationDrawer();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an NavigationDrawerActivity");
        }
    }

    public final boolean isNavigationDrawerOpened(){
        try {
            NavigationDrawerActivity drawerActivity = (NavigationDrawerActivity) activity;
            return drawerActivity.isNavigationDrawerOpened();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an NavigationDrawerActivity");
        }
    }

    public final void openNavigationDrawer(){
        try {
            NavigationDrawerActivity drawerActivity = (NavigationDrawerActivity) activity;
            drawerActivity.openNavigationDrawer();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an NavigationDrawerActivity");
        }
    }

    public final void closeNavigationDrawer(){
        try {
            NavigationDrawerActivity drawerActivity = (NavigationDrawerActivity) activity;
            drawerActivity.closeNavigationDrawer();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an NavigationDrawerActivity");
        }
    }

    public final BottomNavigationView getBottomNavigationView(){
        try {
            BottomNavigationActivity navigationActivity = (BottomNavigationActivity) activity;
            return navigationActivity.getNavigationView();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an BottomNavigationActivity");
        }
    }

    public final boolean hasBottomNavigationView(){
        try {
            BottomNavigationActivity navigationActivity = (BottomNavigationActivity) activity;
            return navigationActivity.hasNavigationView();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an BottomNavigationActivity");
        }
    }

    public int getNavigationDestinationId(){
        try {
            NavigableActivity navigableActivity = (NavigableActivity) activity;
            return navigableActivity.getNavigationDestinationId();
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an NavigableActivity");
        }
    }

    public final void setBottomNavigationDestination(int itemId){
        try {
            BottomNavigationActivity navigationActivity = (BottomNavigationActivity) activity;
            navigationActivity.setNavigationDestination(itemId);
        }catch (ClassCastException e){
            throw new ClassCastException(
                    "Activity that hosts this fragment is not an BottomNavigationActivity");
        }
    }

    public void replaceContentFragment(Fragment fragment){
        activity.replaceContentFragment(fragment);
    }

    public final int requestPermission(String permission, boolean withExplanation, int requestCode){
        return AppUtils.Permissions.requestPermission(this, requestCode, withExplanation, permission);
    }
}