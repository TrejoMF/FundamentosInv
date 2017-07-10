package com.multimarca.tae.voceadorestae.Fragments.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class ProgressFragment extends DialogFragment {

    public String dialogTag;

    private String Title;
    private String Message;

    private static String ARG_PARAM1 = "Titulo";
    private static String ARG_PARAM2 = "Mensaje";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PARAM1, Title);
        outState.putString(ARG_PARAM2, Message);

    }

    public static ProgressFragment newInstance(String title, String message) {

        Bundle args = new Bundle();


        ProgressFragment fragment = new ProgressFragment();

        args.putString(ARG_PARAM1,title);
        args.putString(ARG_PARAM2,message);

        fragment.setArguments(args);
        return fragment;
    }


    public void dismiss(FragmentManager fragmentManager) {

        ProgressFragment progressFragment = (ProgressFragment)fragmentManager.findFragmentByTag(dialogTag);
        if(progressFragment != null){
            progressFragment.dismissAllowingStateLoss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());

            Title = savedInstanceState.getString(ARG_PARAM1);
            Message = savedInstanceState.getString(ARG_PARAM2);

            progressDialog.setTitle(Title);
            progressDialog.setMessage(Message);

            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            return progressDialog;
        }else{
            ProgressDialog progressDialog = new ProgressDialog(getActivity());

            Title = getArguments().getString(ARG_PARAM1);
            Message = getArguments().getString(ARG_PARAM2);

            progressDialog.setTitle(Title);
            progressDialog.setMessage(Message);

            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            return progressDialog;
        }

    }

    public void show(FragmentManager fragmentManager, String tag) {
        dialogTag = tag;
        super.show(fragmentManager, tag);
    }

    public ProgressFragment getFragment(FragmentManager fragmentManager){
        return (ProgressFragment)fragmentManager.findFragmentByTag(dialogTag);

    }
}
