
package com.ricardo.twitterpic.ui.mvp;

/**
 * Holds the logic to present data on a UIComponent
 *
 * @param <T>
 */
public interface Presenter<T extends UIComponent> {

    /**
     * Initizalization of this presenter with the view (view is created and ready)
     *
     * @param view
     */
    public void init(T view);
}
