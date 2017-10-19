package com.imogene.android.carcase.controller.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by Admin on 13.04.2017.
 */

public class FABCollapseOnScrollBehavior extends FABReactOnScrollBehavior{

    public FABCollapseOnScrollBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    protected void hide(View view, ViewPropertyAnimator animator) {
        animator.scaleX(0).scaleY(0).alpha(.5f).alpha(.5f);
    }

    @Override
    protected void show(View view, ViewPropertyAnimator animator) {
        animator.scaleX(1).scaleY(1).alpha(1).alpha(1);
    }
}