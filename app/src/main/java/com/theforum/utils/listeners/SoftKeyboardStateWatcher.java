package com.theforum.utils.listeners;

/**
 * @author DEEPANKAR
 * @since 09-01-2016.
 */

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.theforum.utils.CommonUtils;


public class SoftKeyboardStateWatcher implements ViewTreeObserver.OnGlobalLayoutListener {

    private SoftKeyboardStateListener listener;
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;
    private Context mContext;

    public SoftKeyboardStateWatcher(View activityRootView, Context context) {
        this.mContext = context;
        this.activityRootView     = activityRootView;
        this.isSoftKeyboardOpened = false;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);
        final int heightDiff = activityRootView.getHeight() - (r.bottom - r.top);

        if(Math.abs(heightDiff) > CommonUtils.convertDpToPixel(100, mContext)){
            if(!isSoftKeyboardOpened){
                if (listener != null) {
                    listener.onSoftKeyboardOpened(heightDiff);
                }
            }else {
                if (listener != null) {
                    listener.onSoftKeyboardClosed();
                }
            }
            isSoftKeyboardOpened = !isSoftKeyboardOpened;
        }
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        this.listener = listener;
    }

    /**
     * Default value is zero {@code 0}.
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public interface SoftKeyboardStateListener {
        /**
         *
         * @param keyboardHeight gives soft keyboard Height in pixels.
         */
        void onSoftKeyboardOpened(int keyboardHeight);

        /**
         * notifies when keyboard get closed.
         */
        void onSoftKeyboardClosed();
    }
}
