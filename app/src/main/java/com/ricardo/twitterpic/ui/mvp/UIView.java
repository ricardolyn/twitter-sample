
package com.ricardo.twitterpic.ui.mvp;

/**
 * Represents a view on the UI that shows data from a model
 * <p/>
 * Normally a custom view that atomically shows the current data on the model
 *
 * @param <M>
 */
public interface UIView<M> extends UIComponent {

    /**
     * Set the model to show on the UI View
     *
     * @param model
     */
    public void setModel(M model);
}
