package com.example;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 11/20/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlertUserDialog extends DialogFragment implements DialogInterface.OnClickListener{
    private String _displayMessage;
    private String _settingsActivityAction;

    public AlertUserDialog(String displayMessage, String settingsActivityAction) {
        _displayMessage = displayMessage != null ? displayMessage : "MESSAGE NOT SET";
        _settingsActivityAction = settingsActivityAction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(_displayMessage);
        builder.setPositiveButton("OK", this);

        Dialog theDialog = builder.create();
        theDialog.setCanceledOnTouchOutside(false);

        return theDialog;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        switch(i) {
            case Dialog.BUTTON_POSITIVE:
                // Perform desired action response to user clicking "OK"
                if(_settingsActivityAction != null)
                    startActivity(new Intent(_settingsActivityAction));
                break;
        }
    }
}
