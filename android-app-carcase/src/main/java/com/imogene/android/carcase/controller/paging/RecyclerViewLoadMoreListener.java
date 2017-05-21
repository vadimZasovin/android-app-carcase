package com.imogene.android.carcase.controller.paging;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by vadim on 5/21/17.
 */

public class RecyclerViewLoadMoreListener extends RecyclerView.OnScrollListener
        implements LoadMoreListener {

    private final float thresholdFraction;
    private final int threshold;
    private final OnThresholdReachedListener listener;
    private boolean shouldListen = true;

    private RecyclerViewLoadMoreListener(float thresholdFraction, int threshold, boolean fraction,
                                         OnThresholdReachedListener listener){
        if(fraction){
            if(thresholdFraction <= 0 || thresholdFraction > 1){
                throw new IllegalArgumentException("Threshold fraction must be > 0 && <= 1");
            }
        }else {
            if(threshold <= 0){
                throw new IllegalArgumentException("Threshold must be > 0");
            }
        }

        if(listener == null){
            throw new IllegalArgumentException("Listener can not be null");
        }

        this.thresholdFraction = thresholdFraction;
        this.threshold = threshold;
        this.listener = listener;
    }

    public RecyclerViewLoadMoreListener(float thresholdFraction, OnThresholdReachedListener listener){
        this(thresholdFraction, -1, true, listener);
    }

    public RecyclerViewLoadMoreListener(int threshold, OnThresholdReachedListener listener){
        this(-1, threshold, false, listener);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager;
        try {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }catch (ClassCastException e){
            throw new IllegalStateException("This OnScrollListener supports only LinearLayoutManager.");
        }

        if(layoutManager == null){
            throw new IllegalStateException("LayoutManager is not found.");
        }

        if(layoutManager.getReverseLayout()){
            if(dy >= 0){
                return;
            }
        }else {
            if(dy <= 0){
                return;
            }
        }

        final int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        if(lastVisiblePosition == RecyclerView.NO_POSITION){
            return;
        }

        int itemCount = layoutManager.getItemCount();
        if(shouldListen && isThresholdReached(lastVisiblePosition, itemCount)){
            reportThresholdReached(lastVisiblePosition);
            shouldListen = false;
        }
    }

    private boolean isThresholdReached(int lastVisiblePosition, int itemCount){
        if(thresholdFraction > 0){
            return lastVisiblePosition >= itemCount * thresholdFraction;
        }else {
            return lastVisiblePosition >= itemCount - threshold;
        }
    }

    @Override
    public void reportThresholdReached(int position) {
        listener.onThresholdReached(position);
    }

    @Override
    public void notifyContinueListen(){
        shouldListen = true;
    }
}
