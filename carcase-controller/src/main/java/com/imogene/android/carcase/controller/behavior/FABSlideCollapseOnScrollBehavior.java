package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

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
