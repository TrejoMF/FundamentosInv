package com.multimarca.tae.voceadorestae.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by erick on 1/16/16. Multimarca
 */
@SuppressLint("ValidFragment")
public class DialogTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private final ListenDataSet listenDataSet;

    @SuppressLint("ValidFragment")
    public DialogTimePicker(ListenDataSet listenDataSet) {
        //...
        this.listenDataSet = listenDataSet;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Called when the user is done setting a new time and the dialog has
     * closed.
     *
     * @param view      the view associated with this listener
     * @param hourOfDay the hour that was set
     * @param minute    the minute that was set
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listenDataSet.callback(view, hourOfDay, minute);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listenDataSet.cancel();
    }

    public interface ListenDataSet {
        void callback(View view, int Hour, int minute);
        void cancel();
    }
}
