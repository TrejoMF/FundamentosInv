package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.adapters.FragmentDepositosAdapter;

import org.json.JSONObject;

public class Depositos extends AppCompatActivity {

    ViewPager viewPager;
    Toolbar toolbar;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositos);

//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
//        backButton.setOnClickListener(onClickBack);
        viewPager    = (ViewPager)findViewById(R.id.rtraspasos_viewPager);
        toolbar = (Toolbar)findViewById(R.id.medium_toolbar);

        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");

        medium_header_titulo.setText(titulo);


        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        FragmentDepositosAdapter fragmentDepositosAdapter = new FragmentDepositosAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentDepositosAdapter);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_amber);
    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.tae_viewPager);
        }
        return viewPager;
    }

    public void setData(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getData() {
        return jsonObject;
    }

}
