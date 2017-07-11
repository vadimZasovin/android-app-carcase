package com.imogene.android.carcase.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 03.05.2017.
 */

public abstract class FABReactOnScrollBehavior extends FloatingActionButton.Behavior
        implements Runnable {

    private View view;

    public FABReactOnScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && child.isEnabled();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed > 0){
            hide(child);
        }else if(dyConsumed < 0){
            show(child);
        }
    }

    public final void hide(View view){
        this.view = view;
        ViewPropertyAnimator animator = view.animate();
        hide(view, animator);
        animator.setDuration(200);
        animator.withEndAction(this);
    }

    protected abstract void hide(View view, ViewPropertyAnimator animator);

    @Override
    public void run() {
        if(view != null){
            view.setVisibility(View.INVISIBLE);
        }
    }

    public final void show(View view){
        this.view = view;
        view.setVisibility(View.VISIBLE);
        ViewPropertyAnimator animator = view.animate();
        show(view, animator);
        animator.setDuration(200);
    }

    protected abstract void show(View view, ViewPropertyAnimator animator);
}