package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * Created by Admin on 03.05.2017.
 */

public abstract class ReactOnScrollBehavior extends CoordinatorLayout.Behavior<View>
        implements Runnable {

    private CoordinatorLayout parent;
    private View child;
    private boolean show;

    public ReactOnScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        this.parent = parent;
        this.child = child;
        return super.onLayoutChild(parent, child, layoutDirection);
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
            hide();
        }else if(dyConsumed < 0){
            show();
        }
    }

    public final void hide(){
        if(child != null && parent != null){
            hideInternal();
        }
    }

    private void hideInternal(){
        show = false;
        ViewPropertyAnimator animator = child.animate();
        hide(child, parent, animator);
        animator.withEndAction(this);
    }

    protected abstract void hide(View child, CoordinatorLayout parent, ViewPropertyAnimator animator);

    public final void show(){
        if(child != null){
            showInternal();
        }
    }

    private void showInternal(){
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
