package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;

public class Macaddress extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macaddress);

        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");


        toolbar      = (Toolbar)findViewById(R.id.medium_toolbar);

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);

        medium_header_titulo.setText(titulo);

        TextView MacTextView = (TextView)findViewById(R.id.macaddress_text);
        MacTextView.setText(getMACAddress());
//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
//        backButton.setOnClickListener(onClickBack);
    }

    public String getMACAddress() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }
    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
