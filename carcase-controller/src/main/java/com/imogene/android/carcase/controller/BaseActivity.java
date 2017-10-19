package com.imogene.android.carcase.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.imogene.android.carcase.commons.util.AppUtils;

/**
 * Created by Admin on 11.04.2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        setupContentFragment();
    }

    @LayoutRes
    protected int getLayoutRes(){
        return R.layout.activity_abstract;
    }

    private void setupContentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
            Intent intent = getIntent();
            fragment = createFragment(intent);
            if(fragment != null){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.fragmentContainer, fragment);
                transaction.commit();
            }
        }
    }

    protected abstract Fragment createFragment(Intent intent);

    public final Fragment getContentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.fragmentContainer);
    }

    @IdRes
    public final int getContentFragmentId(){
        return R.id.fragmentContainer;
    }

    @Override
    public void onBackPressed() {
        boolean handled = false;
        OnBackPressListener listener;
        if(this instanceof OnBackPressListener){
            listener = (OnBackPressListener) this;
            handled = listener.onInterceptBackPressed();
        }
        if(!handled){
            Fragment fragment = getContentFragment();
            if(fragment != null && fragment instanceof OnBackPressListener){
                listener = (OnBackPressListener) fragment;
                handled = listener.onInterceptBackPressed();
            }
        }
        if(!handled){
            super.onBackPressed();
        }
    }

    public final void openSoftKeyboard(){
        AppUtils.Commons.openSoftKeyboard(this);
    }

    public final void closeSoftKeyboard(){
        AppUtils.Commons.closeSoftKeyboard(this);
    }

    public final boolean checkNetworkAvailability(){
        return AppUtils.Commons.checkNetworkAvailability(this);
    }

    public final boolean checkApiLevel(int requiredApiLevel){
        return AppUtils.Commons.checkApiLevel(requiredApiLevel);
    }

    public final int convertDpsInPixels(int dps){
        return AppUtils.Graphics.convertDpsInPixels(this, dps);
    }

    public final float convertDpsInPixels(float dps){
        return AppUtils.Graphics.convertDpsInPixels(this, dps);
    }

    public final void showToast(@StringRes int messageRes, boolean lengthLong){
        int length = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        showToast(messageRes, length);
    }

    public final void showToast(@StringRes int messageRes, int length){
        Toast.makeText(this, messageRes, length).show();
    }

    public final void showToast(String message, boolean lengthLong){
        int length = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        showToast(message, length);
    }

    public final void showToast(String message, int length){
        Toast.makeText(this, message, length).show();
    }

    public void replaceContentFragment(Fragment fragment){
        replaceContentFragment(fragment, R.anim.fade_in, R.anim.fade_out);
    }

    public void replaceContentFragment(Fragment fragment,
                                       @AnimRes int enterAnim,
                                       @AnimRes int exitAnim){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(getContentFragmentId(), fragment);
        transaction.commit();
    }

    public void replaceContentFragment(Fragment fragment,
                                       @AnimRes int enterAnim, @AnimRes int exitAnim,
                                       @AnimRes int popEnterAnim, @AnimRes int popExitAnim){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        transaction.replace(getContentFragmentId(), fragment);
        transaction.commit();
    }

    public final int requestPermission(String permission, boolean withExplanation, int requestCode){
        return AppUtils.Permissions.requestPermission(this, requestCode, withExplanation, permission);
    }
}