package com.imogene.android.carcase.sample;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;

import com.imogene.android.carcase.controller.BottomNavigationActivity;

/**
 * Created by Admin on 14.04.2017.
 */

public class SampleBottomNavigationActivity extends BottomNavigationActivity {

    @Override
    protected Fragment createFragment(Intent intent) {
        return SampleFragment.newInstance();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPrepareBottomNavigationView(@NonNull BottomNavigationView navigationView) {
        super.onPrepareBottomNavigationView(navigationView);
        navigationView.inflateMenu(R.menu.bottom_navigation_menu);
        float elevation = convertDpsInPixels(8);
        ViewCompat.setElevation(navigationView, elevation);
        navigationView.setBackgroundColor(Color.WHITE);

        int[] disabledStateSet = new int[]{-android.R.attr.state_enabled};
        int[] checkedStateSet = new int[]{android.R.attr.state_checked};
        int[] emptyStateSet = new int[]{};
        int[][] stateSets = new int[][]{disabledStateSet, checkedStateSet, emptyStateSet};

        Resources resources = getResources();
        int colorGrayLight = resources.getColor(R.color.colorGrayLight);
        int colorAccent = resources.getColor(R.color.colorAccent);
        int colorGray = resources.getColor(R.color.colorGray);
        int[] colors = new int[]{colorGrayLight, colorAccent, colorGray};

        ColorStateList tintList = new ColorStateList(stateSets, colors);
        navigationView.setItemIconTintList(tintList);
        navigationView.setItemTextColor(tintList);
    }

    @Override
    protected int getMode() {
        return MODE_FLOATING;
    }
}