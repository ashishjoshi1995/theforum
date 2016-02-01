package com.theforum.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.theforum.R;

/**
 * @author Ashish
 * @since 2/1/2016
 */
public class ProgresssDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.progress_dialog_layout);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
