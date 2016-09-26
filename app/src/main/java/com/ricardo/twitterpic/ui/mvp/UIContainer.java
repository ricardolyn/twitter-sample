
package com.ricardo.twitterpic.ui.mvp;

/**
 * Represents a component on the UI that contains other UIComponents
 * <p/>
 * Normally a fragment, activity or group of views that use other UIs to present data on the screen
 */
public interface UIContainer extends UIComponent {

    /**
     * Show a loading indication to the user when is loading data
     *
     * @param enabled
     */
    void setLoading(boolean enabled);

}
