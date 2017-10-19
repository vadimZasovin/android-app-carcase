package com.imogene.android.carcase.controller.paging;

/**
 * Interface definition for the callback to
 * be invoked by any LoadMoreListener when
 * the specified threshold is reached.
 */
public interface OnThresholdReachedListener {
    /**
     * Called by any LoadMoreListener to notify
     * the application that the scrollable view
     * such as RecyclerView or ViewPager have reached
     * a threshold and the application must load the
     * next portion of data.
     * @param position reached position. When you use
     *                 {@link RecyclerViewLoadMoreListener} this is
     *                 a last visible position, and when
     *                 you use {@link ViewPagerLoadMoreListener}
     *                 this is the exact position that trigger
     *                 invocation of this callback.
     */
    void onThresholdReached(int position);
}
