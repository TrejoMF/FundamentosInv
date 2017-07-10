package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.adapters.FragmentTraspasosAdapter;
import com.multimarca.tae.voceadorestae.Objects.Traspaso;

import java.util.ArrayList;

public class RTraspasos extends AppCompatActivity {

    ViewPager viewPager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtraspasos);
        viewPager    = (ViewPager)findViewById(R.id.rtraspasos_viewPager);
//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);
        toolbar = (Toolbar)findViewById(R.id.medium_toolbar);


        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");

        medium_header_titulo.setText(titulo);
//        backButton.setOnClickListener(onClickBack);

        FragmentTraspasosAdapter fragmentTraspasosAdapter = new FragmentTraspasosAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentTraspasosAdapter);

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_blue700);

    }
    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.tae_viewPager);
        }
        return viewPager;
    }


    ArrayList<Traspaso> _traspasos = new ArrayList<>();

    public void setTraspasos(ArrayList<Traspaso> traspasos) {
        _traspasos = traspasos;
    }

    public ArrayList<Traspaso> get_traspasos(){
        return _traspasos;
    }
    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
