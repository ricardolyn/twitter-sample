
package com.ricardo.twitterpic.ui.mvp;

/**
 * Interface used by objects that want to provide logic for list adapters
 *
 * @param <K>
 */
public interface ListBinder<K extends UIComponent> {

    public void bindListItem(K item, int position);
    public int getListCount();
}
