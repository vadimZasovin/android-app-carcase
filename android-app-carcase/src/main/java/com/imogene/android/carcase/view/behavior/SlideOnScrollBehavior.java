package com.imogene.android.carcase.view.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 13.04.2017.
 */

public class SlideOnScrollBehavior extends ReactOnScrollBehavior{

    public SlideOnScrollBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    protected void hide(View child, View target, ViewPropertyAnimator animator) {
        float translationY = child.getTranslationY();
        int childBottom = child.getBottom();
        int childHeight = child.getHeight();
        int targetBottom = target.getBottom();
        int distance = childHeight + targetBottom - childBottom;
        animator.translationYBy(distance - translationY);
    }

    @Override
    protected void show(View child, ViewPropertyAnimator animator) {
        float translationY = child.getTranslationY();
        animator.translationYBy(-translationY);
    }
}