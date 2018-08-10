package com.imogene.android.carcase.sample;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.imogene.android.carcase.controller.BottomNavigationActivity;
import com.imogene.android.carcase.controller.behavior.FABReactOnScrollBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by Admin on 14.04.2017.
 */

public class SampleBottomNavigationActivity extends BottomNavigationActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fab = findViewById(R.id.floating_action_button);
        TextView textView = findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFABUsingBehavior();
            }
        });
    }

    private void showFABUsingBehavior(){
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FABReactOnScrollBehavior behavior = (FABReactOnScrollBehavior) lp.getBehavior();
        behavior.show();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sample_bottom_navigation;
    }

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
}