package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 13.04.2017.
 */

public class FABSlideOnScrollBehavior extends FABReactOnScrollBehavior{

    private final SlideOnScrollBehavior wrappedBehavior;

    public FABSlideOnScrollBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        wrappedBehavior = new SlideOnScrollBehavior(context, attributeSet);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild, @NonNull View target,
                                       int nestedScrollAxes, int type) {
        return wrappedBehavior.onStartNestedScroll(
                coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
        wrappedBehavior.onNestedScroll(
                coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    protected void hide(View view, ViewPropertyAnimator animator) {}

    @Override
    protected void show(View view, ViewPropertyAnimator animator) {}
}