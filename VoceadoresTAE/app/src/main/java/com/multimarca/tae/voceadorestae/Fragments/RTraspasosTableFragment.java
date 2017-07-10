package com.multimarca.tae.voceadorestae.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multimarca.tae.voceadorestae.Objects.Traspaso;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.RTraspasos;
import com.multimarca.tae.voceadorestae.adapters.TraspasoAdapter;

import java.util.ArrayList;


public class RTraspasosTableFragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Traspaso> traspasoArrayList = new ArrayList<>();
    TraspasoAdapter traspasoAdapter;

    public static RTraspasosTableFragment newInstance() {
        RTraspasosTableFragment fragment = new RTraspasosTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public RTraspasosTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rtraspasos_table_fragment, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.rtraspasos_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        traspasoArrayList = new ArrayList<>();
        traspasoAdapter = new TraspasoAdapter(getContext(), traspasoArrayList);
        recyclerView.setAdapter(traspasoAdapter);

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser ) {
            traspasoArrayList = ((RTraspasos) getActivity()).get_traspasos();
            TraspasoAdapter traspasoAdapter = new TraspasoAdapter(getContext(), traspasoArrayList);
            recyclerView.setAdapter(traspasoAdapter);
        }
        else {

        }
    }
}
