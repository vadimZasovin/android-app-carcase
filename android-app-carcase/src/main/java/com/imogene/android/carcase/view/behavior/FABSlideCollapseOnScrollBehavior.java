package com.imogene.android.carcase.view.behavior;

import android.content.Context;
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
    protected void hide(View view, ViewPropertyAnimator animator) {
        super.hide(view, animator);
        wrappedBehavior.hide(view, animator);
    }

    @Override
    protected void show(View view, ViewPropertyAnimator animator) {
        super.show(view, animator);
        wrappedBehavior.show(view, animator);
    }
}
