package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 03.05.2017.
 */

public class FABSlideFadeOnScrollBehavior extends FABSlideOnScrollBehavior {

    public FABSlideFadeOnScrollBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void hide(FloatingActionButton child, ViewPropertyAnimator animator) {
        super.hide(child, animator);
        animator.alpha(.3f);
    }

    @Override
    protected void show(FloatingActionButton child, ViewPropertyAnimator animator) {
        super.show(child, animator);
        animator.alpha(1f);
    }
}
