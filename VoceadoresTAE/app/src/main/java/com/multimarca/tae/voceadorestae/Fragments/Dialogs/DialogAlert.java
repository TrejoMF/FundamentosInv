package com.multimarca.tae.voceadorestae.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.multimarca.tae.voceadorestae.R;


public class DialogAlert extends DialogFragment {

    private static String ARG_PARAM1 = "Message";
    private static String ARG_PARAM2 = "Title";
    private static String ARG_PARAM3 = "Icon";

    private String Title = "Titulo Default";
    private String Message = "";
    private int Icon = R.mipmap.ic_launcher;

    private String dialogTag;

    public DialogAlert() {
        setRetainInstance(false);
    }

    public void show(FragmentManager fragmentManager, String tag) {
        dialogTag = tag;
        super.show(fragmentManager, tag);
    }
    public void dismiss(FragmentManager fragmentManager) {

        ProgressFragment progressFragment = (ProgressFragment)fragmentManager.findFragmentByTag(dialogTag);
        if(progressFragment != null){
            progressFragment.dismissAllowingStateLoss();
        }
    }
    public static DialogAlert newInstance(String message, String title, int icon) {


        Bundle args = new Bundle();

        DialogAlert fragment = new DialogAlert();

        args.putString(ARG_PARAM1, message);
        args.putString(ARG_PARAM2, title);
        args.putInt(ARG_PARAM3, icon);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_PARAM1, Message);
        outState.putString(ARG_PARAM2, Title);
        outState.putInt(ARG_PARAM3, Icon);


    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if (getArguments() != null) {
                Message = savedInstanceState.getString(ARG_PARAM1);
                Title = savedInstanceState.getString(ARG_PARAM2);
                Icon = savedInstanceState.getInt(ARG_PARAM3);
            }

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage(Message)
                    .setTitle(Title)
                    .setCancelable(true)
                    .setIcon(Icon)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            return builder.create();


        }else {
            if (getArguments() != null) {
                Message = getArguments().getString(ARG_PARAM1);
                Title = getArguments().getString(ARG_PARAM2);
                Icon = getArguments().getInt(ARG_PARAM3);
            }
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage(Message)
                    .setTitle(Title)
                    .setCancelable(true)
                    .setIcon(Icon)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }
}
