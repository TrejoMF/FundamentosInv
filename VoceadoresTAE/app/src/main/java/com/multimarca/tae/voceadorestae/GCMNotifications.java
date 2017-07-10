package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.Objects.Message;
import com.multimarca.tae.voceadorestae.adapters.MessagesCGMAdapter;
import com.multimarca.tae.voceadorestae.databases.DbManager;

import java.util.ArrayList;

public class GCMNotifications extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcmnotifications);
        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);

        toolbar      = (Toolbar)findViewById(R.id.medium_toolbar);
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");
        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);
        medium_header_titulo.setText(titulo);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary);

        recyclerView = (RecyclerView)findViewById(R.id.meesages_recycler);
        DbManager dbManager = DbManager.getInstance(this);
        ArrayList<Message> messagesArray = dbManager.getMessages();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MessagesCGMAdapter menuAdapter = new MessagesCGMAdapter(this, messagesArray);
        recyclerView.setAdapter(menuAdapter);

    }


    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
