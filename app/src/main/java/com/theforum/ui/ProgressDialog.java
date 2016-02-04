package com.theforum.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.theforum.R;

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
        ((GradientDrawable)frogBody.getBackground()).setColor(Color.parseColor("#30ed17"));
    }



}
