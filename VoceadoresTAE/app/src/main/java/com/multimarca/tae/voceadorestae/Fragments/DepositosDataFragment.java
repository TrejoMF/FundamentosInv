package com.multimarca.tae.voceadorestae.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multimarca.tae.voceadorestae.R;


public class DepositosDataFragment extends Fragment {

    public DepositosDataFragment() {
    }

    public static DepositosDataFragment newInstance() {
        DepositosDataFragment fragment = new DepositosDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_depositos_data, container, false);
    }

}
