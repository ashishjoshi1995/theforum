package com.theforum.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * @author DEEPANKAR
 * @since 09-01-2016.
 */
public class KeyboardListenerEditText extends EditText {

    private OnBackPressListener onBackPressListener;

    public KeyboardListenerEditText(Context context) {
        super(context);
    }

    public KeyboardListenerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardListenerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnBackPressListener(OnBackPressListener onBackPressListener){
        this.onBackPressListener = onBackPressListener;
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){

        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

            if(onBackPressListener!=null){
                return onBackPressListener.onBackPressed();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }


    public interface OnBackPressListener{

        /**
         * @return true if event is to be handled by user.
         */
        boolean onBackPressed();
    }
}
