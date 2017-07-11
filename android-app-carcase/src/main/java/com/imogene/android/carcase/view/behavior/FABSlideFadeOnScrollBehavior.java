package com.imogene.android.carcase.view.behavior;

import android.content.Context;
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
    public void hide(View view, ViewPropertyAnimator animator) {
        super.hide(view, animator);
        animator.alpha(.3f);
    }

    @Override
    public void show(View view, ViewPropertyAnimator animator) {
        super.show(view, animator);
        animator.alpha(1f);
    }
}
