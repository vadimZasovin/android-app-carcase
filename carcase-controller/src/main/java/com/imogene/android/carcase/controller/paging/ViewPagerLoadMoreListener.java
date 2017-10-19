package com.imogene.android.carcase.controller.paging;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by vadim on 5/21/17.
 */

public class ViewPagerLoadMoreListener extends ViewPager.SimpleOnPageChangeListener
        implements LoadMoreListener {

    private final float thresholdFraction;
    private final PagerAdapter adapter;
    private final OnThresholdReachedListener listener;
    private boolean shouldListen = true;

    public ViewPagerLoadMoreListener(float thresholdFraction, PagerAdapter adapter,
                                     OnThresholdReachedListener listener){
        if(thresholdFraction <= 0 || thresholdFraction > 1){
            throw new IllegalArgumentException("Illegal threshold fraction. It mast be > 0 and <= 1");
        }
        if(adapter == null){
            throw new IllegalArgumentException("The specified adapter is a null reference");
        }
        if(listener == null){
            throw new IllegalArgumentException("The specified listener is a null reference");
        }

        this.thresholdFraction = thresholdFraction;
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        int itemCount = adapter.getCount();
        if(shouldListen && position >= itemCount * thresholdFraction){
            reportThresholdReached(position);
            shouldListen = false;
        }
    }

    @Override
    public void reportThresholdReached(int position) {
        listener.onThresholdReached(position);
    }

    @Override
    public void notifyContinueListen() {
        shouldListen = true;
    }
}
