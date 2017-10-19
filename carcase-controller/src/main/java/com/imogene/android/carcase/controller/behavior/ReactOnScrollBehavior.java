package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
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
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                && child.isEnabled() && type == ViewCompat.TYPE_TOUCH;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if(dyConsumed > 0){
            hide(child, target);
        }else if(dyConsumed < 0){
            show(child);
        }
    }

    public final void hide(View child, View target){
        this.child = child;
        show = false;
        ViewPropertyAnimator animator = child.animate();
        hide(child, target, animator);
        animator.withEndAction(this);
    }

    protected abstract void hide(View child, View target, ViewPropertyAnimator animator);

    public final void show(View child){
        this.child = child;
        show = true;
        ViewPropertyAnimator animator = child.animate();
        show(child, animator);
        animator.withStartAction(this);
    }

    protected abstract void show(View child, ViewPropertyAnimator animator);

    @Override
    public final void run() {
        if(show){
            child.setVisibility(View.VISIBLE);
        }else {
            child.setVisibility(View.INVISIBLE);
        }
    }
}
