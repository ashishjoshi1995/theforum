package com.theforum.utils.listeners;

/**
 * @author DEEPANKAR
 * @since 08-01-2016.
 */

public interface OnHomeUiChangeListener {
    /**
     * @param position current position of view pager
     * @param setSearchEnabled if position is 1 or 2 search will be shown and at 3 shareOption will
     *                         be shown.
     */
    void onPageSelected(int position, boolean setSearchEnabled);
}
