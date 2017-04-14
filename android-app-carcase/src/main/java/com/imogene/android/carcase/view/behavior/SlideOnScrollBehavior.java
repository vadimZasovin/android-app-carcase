package com.imogene.android.carcase.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 13.04.2017.
 */

public class SlideOnScrollBehavior extends CoordinatorLayout.Behavior<View> implements Runnable{

    private View child;
    private boolean show;

    public SlideOnScrollBehavior(Context context, AttributeSet attributeSet){
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
        float translationY = child.getTranslationY();
        int childBottom = child.getBottom();
        int childHeight = child.getHeight();
        int targetBottom = target.getBottom();
        int distance = childHeight + targetBottom - childBottom;
        child.animate().translationYBy(distance - translationY).withEndAction(this);
    }

    private void show(View child){
        this.child = child;
        show = true;
        float translationY = child.getTranslationY();
        child.animate().translationYBy(-translationY).withStartAction(this);
    }

    @Override
    public void run() {
        if(show){
            child.setVisibility(View.VISIBLE);
        }else {
            child.setVisibility(View.INVISIBLE);
        }
    }
}