package com.theforum.utils.views.customFont;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.theforum.R;


public class RadioButton extends android.widget.RadioButton{

    public RadioButton(Context context) {
        super(context);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontView);
            String fontName = a.getString(R.styleable.CustomFontView_fontName);
            if (fontName!=null) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName));
            }
            a.recycle();
        }
    }
}
