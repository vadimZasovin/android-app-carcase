package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * Created by Admin on 03.05.2017.
 */

public abstract class FABReactOnScrollBehavior
        extends FloatingActionButton.Behavior implements Runnable {

    private FloatingActionButton child;

    public FABReactOnScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionButton child, int layoutDirection) {
        this.child = child;
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                && child.isEnabled() && type == ViewCompat.TYPE_TOUCH;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if(dyConsumed > 0){
            hide();
        }else if(dyConsumed < 0){
            show();
        }
    }

    public final void hide(){
        if(child != null){
            hideInternal();
        }
    }

    private void hideInternal(){
        ViewPropertyAnimator animator = child.animate();
        hide(child, animator);
        animator.setDuration(200);
        animator.withEndAction(this);
    }

    protected abstract void hide(FloatingActionButton child, ViewPropertyAnimator animator);

    @Override
    public void run() {
        if(child != null){
            child.setVisibility(View.INVISIBLE);
        }
    }

    public final void show(){
        if(child != null){
            showInternal();
        }
    }

    private void showInternal(){
        child.setVisibility(View.VISIBLE);
        ViewPropertyAnimator animator = child.animate();
        show(child, animator);
        animator.setDuration(200);
    }

    protected abstract void show(FloatingActionButton child, ViewPropertyAnimator animator);
}