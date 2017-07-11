package com.imogene.android.carcase.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 03.05.2017.
 */

public abstract class ReactOnScrollBehavior extends CoordinatorLayout.Behavior<View>
        implements Runnable {

    private View child;
    private boolean show;

    public ReactOnScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       View child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && child.isEnabled();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed > 0){
            hide(child, target);
        }else if(dyConsumed < 0){
            show(child);
        }
    }

    private void hide(View child, View target){
        this.child = child;
        show = false;
        ViewPropertyAnimator animator = child.animate();
        hide(child, target, animator);
        animator.withEndAction(this);
    }

    public abstract void hide(View child, View target, ViewPropertyAnimator animator);

    private void show(View child){
        this.child = child;
        show = true;
        ViewPropertyAnimator animator = child.animate();
        show(child, animator);
        animator.withStartAction(this);
    }

    public abstract void show(View child, ViewPropertyAnimator animator);

    @Override
    public final void run() {
        if(show){
            child.setVisibility(View.VISIBLE);
        }else {
            child.setVisibility(View.INVISIBLE);
        }
    }
}
