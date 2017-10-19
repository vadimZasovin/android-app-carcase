package com.imogene.android.carcase.controller.paging;

/**
 * This interface describes a contract for the
 * classes that wants to listen for such events
 * as when the user reached some point of an
 * scrollable view such as RecyclerView or
 * ViewPager.
 */

public interface LoadMoreListener {

    /**
     * Implementation must call
     * {@link OnThresholdReachedListener#onThresholdReached(int)}
     * method within this method.
     * @param position the position triggered
     *                 onThresholdReached(int) invocation.
     */
    void reportThresholdReached(int position);

    /**
     * Implementations must provide an mechanism that
     * allows to stop listening for the threshold reaching
     * while the new data is loading. Calling this method
     * must ends in resuming listening.
     */
    void notifyContinueListen();
}
