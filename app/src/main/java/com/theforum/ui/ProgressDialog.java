package com.theforum.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.theforum.R;
import com.theforum.utils.CommonUtils;

/**
 * @author Ashish
 * @since 2/1/2016
 */
public class ProgressDialog extends android.app.ProgressDialog {

    public static ProgressDialog createDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setCancelable(false);
        return dialog;
    }

    private ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_layout);

        ImageView frogBody = (ImageView)findViewById(R.id.frog_body);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        GradientDrawable drawable = (GradientDrawable) frogBody.getBackground();
        drawable.setColor(Color.parseColor("#30ed17"));
        drawable.setStroke((int) CommonUtils.convertDpToPixel(3, getContext()), Color.BLACK);

        progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#1b91d5"), PorterDuff.Mode.MULTIPLY);
    }



}
