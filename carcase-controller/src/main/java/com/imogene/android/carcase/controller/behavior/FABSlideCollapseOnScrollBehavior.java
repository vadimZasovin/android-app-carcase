package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by Admin on 03.05.2017.
 */

public class FABSlideCollapseOnScrollBehavior extends FABSlideOnScrollBehavior {

    private FABReactOnScrollBehavior wrappedBehavior;

    public FABSlideCollapseOnScrollBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        wrappedBehavior = new FABCollapseOnScrollBehavior(context, attributeSet);
    }

    @Override
    protected void hide(FloatingActionButton child, ViewPropertyAnimator animator) {
        super.hide(child, animator);
        wrappedBehavior.hide(child, animator);
    }

    @Override
    protected void show(FloatingActionButton child, ViewPropertyAnimator animator) {
        super.show(child, animator);
        wrappedBehavior.show(child, animator);
    }
}
