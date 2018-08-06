package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by Admin on 13.04.2017.
 */

public class SlideOnScrollBehavior extends ReactOnScrollBehavior{

    public SlideOnScrollBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    protected void hide(View child, CoordinatorLayout parent, ViewPropertyAnimator animator) {
        float translationY = child.getTranslationY();
        int childBottom = child.getBottom();
        int childHeight = child.getHeight();
        int targetBottom = parent.getBottom();
        int distance = childHeight + targetBottom - childBottom;
        animator.translationYBy(distance - translationY);
    }

    @Override
    protected void show(View child, ViewPropertyAnimator animator) {
        float translationY = child.getTranslationY();
        animator.translationYBy(-translationY);
    }
}