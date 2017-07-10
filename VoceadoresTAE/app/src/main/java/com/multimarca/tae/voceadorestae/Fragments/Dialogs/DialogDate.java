package com.multimarca.tae.voceadorestae.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by erick on 12/21/15. Multimarca
 */
@SuppressLint("ValidFragment")
public class DialogDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {



    private final ListenDataSet listenDataSet;

    public DialogDate(ListenDataSet listenDataSet) {
        this.listenDataSet = listenDataSet;
    }

    public static DialogDate newInstance() {

        Bundle args = new Bundle();

        DialogDate fragment = new DialogDate(new ListenDataSet() {
            @Override
            public void callback(View view, int year, int month, int day) {

            }

            @Override
            public void cancel() {

            }

        });
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();


        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        listenDataSet.callback(view, year, monthOfYear, dayOfMonth);
        setCancelable(false);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listenDataSet.cancel();

    }

    public interface ListenDataSet {
        public void callback(View view, int year, int month, int day);
        public void cancel();
    }
}
