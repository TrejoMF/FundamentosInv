package com.multimarca.tae.voceadorestae.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Balance;
import com.multimarca.tae.voceadorestae.Depositos;
import com.multimarca.tae.voceadorestae.Macaddress;
import com.multimarca.tae.voceadorestae.Objects.MenuElement;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.RTraspasos;
import com.multimarca.tae.voceadorestae.RVentas;
import com.multimarca.tae.voceadorestae.Services;
import com.multimarca.tae.voceadorestae.Tae;
import com.multimarca.tae.voceadorestae.Traspaso;
import com.multimarca.tae.voceadorestae.adapters.MenuAdapter;
import com.multimarca.tae.voceadorestae.utils.Global;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Drawer_menu extends Fragment {


    RelativeLayout relativeLayout;

    public Drawer_menu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_drawer_menu, container, false);
        RecyclerView principalRecycler = (RecyclerView)view.findViewById(R.id.menu_aside);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.image_chida);
        TextView textUser = (TextView)view.findViewById(R.id.user_info);

        textUser.setText("Usuario: " + Global.NAME(getContext()));


        ArrayList<MenuElement> optionsArray = new ArrayList<>();

        optionsArray.add(new MenuElement("Recarga Eléctronica","Recarga Eléctronica", new Intent(getActivity().getApplicationContext(),Tae.class)));
        optionsArray.add(new MenuElement("Pago de Servicios","Pago de Servicios", new Intent(getActivity().getApplicationContext(),Services.class)));
        optionsArray.add(new MenuElement("Consulta de saldo","Consulta de Saldo", new Intent(getActivity().getApplicationContext(),Balance.class)));
        optionsArray.add(new MenuElement("Reporte de ventas","Reporte de Ventas", new Intent(getActivity().getApplicationContext(),RVentas.class)));
        optionsArray.add(new MenuElement("Reportar depositos","Reportar Depositos", new Intent(getActivity().getApplicationContext(),Depositos.class)));
        optionsArray.add(new MenuElement("Reporte de traspasos","Reporte de Traspasos", new Intent(getActivity().getApplicationContext(),RTraspasos.class)));
        optionsArray.add(new MenuElement("Traspaso","Traspaso", new Intent(getActivity().getApplicationContext(),Traspaso.class)));
        optionsArray.add(new MenuElement("MAC Address","MAC Address", new Intent(getActivity().getApplicationContext(),Macaddress.class)));
        optionsArray.add(new MenuElement("Salir","Salir", null));

        principalRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        MenuAdapter menuAdapter = new MenuAdapter(getActivity(), optionsArray);
        principalRecycler.setAdapter(menuAdapter);

        return view;
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar, int color) {

        relativeLayout.setBackgroundColor(Color.parseColor(getString(color)));
    }

}
