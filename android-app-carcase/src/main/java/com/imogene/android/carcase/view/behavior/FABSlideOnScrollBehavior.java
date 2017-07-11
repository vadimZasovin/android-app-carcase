package com.imogene.android.carcase.view.behavior;

import android.content.Context;
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
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return wrappedBehavior.onStartNestedScroll(
                coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed);
        wrappedBehavior.onNestedScroll(
                coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    protected void hide(View view, ViewPropertyAnimator animator) {}

    @Override
    protected void show(View view, ViewPropertyAnimator animator) {}
}