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

    private CoordinatorLayout parent;
    private final SlideOnScrollBehavior slideBehavior;

    public FABSlideOnScrollBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        slideBehavior = new SlideOnScrollBehavior(context, attributeSet);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes, int type) {
        parent = coordinatorLayout;
        return super.onStartNestedScroll(coordinatorLayout, child,
                directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    protected void hide(FloatingActionButton child, ViewPropertyAnimator animator) {
        slideBehavior.hide(child, parent, animator);
    }

    @Override
    protected void show(FloatingActionButton child, ViewPropertyAnimator animator) {
        slideBehavior.show(child, animator);
    }
}