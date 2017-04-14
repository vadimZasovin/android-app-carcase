package com.imogene.android.carcase.view.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 13.04.2017.
 */

public class FABCollapseOnScrollBehavior extends FloatingActionButton.Behavior
        implements Runnable{

    private View mView;

    public FABCollapseOnScrollBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL && child.isEnabled();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed);
        if(dyConsumed > 0){
            hide(child);
        }else if(dyConsumed < 0){
            show(child);
        }
    }

    private void hide(final View view){
        mView = view;
        view.animate()
                .scaleX(0).scaleY(0)
                .alpha(.5f).alpha(.5f)
                .setDuration(200)
                .withEndAction(this);
    }

    @Override
    public void run() {
        if(mView != null){
            mView.setVisibility(View.INVISIBLE);
        }
    }

    private void show(View view){
        view.setVisibility(View.VISIBLE);
        view.animate()
                .scaleX(1).scaleY(1)
                .alpha(1).alpha(1)
                .setDuration(200);
    }
}