
package com.ricardo.twitterpic.ui.mvp;

/**
 * This is the abstraction for a feature that encapsulates a functionality of an activity, fragment or a view
 */
public interface Feature {

    /**
     * Every Feature MUST release its resources when not used
     */
    void release();
}
