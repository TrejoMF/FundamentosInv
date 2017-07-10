package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.multimarca.tae.voceadorestae.adapters.FragmentRUserAdapter;
import com.multimarca.tae.voceadorestae.widgets.CustomViewPager;

import org.json.JSONException;
import org.json.JSONObject;

public class NewUser extends AppCompatActivity {

    Toolbar toolbar;
    CustomViewPager viewPager = null;
    JSONObject mJsonObjectUserFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        viewPager = (CustomViewPager) findViewById(R.id.ruser_viewpager);

        viewPager.setAdapter(new FragmentRUserAdapter(getSupportFragmentManager()));
        viewPager.setPagingEnabled(false);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar)findViewById(R.id.medium_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");

        medium_header_titulo.setText(titulo);
    }

    @Override
    public void onBackPressed() {
        try {
            if(mJsonObjectUserFragment != null && !mJsonObjectUserFragment.getString("stage").equals("3")) {
                Toast.makeText(this, "La creación del usuario estara incompleta si sales. Termina de crear el usuario antes de salir", Toast.LENGTH_LONG).show();
            }else {
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if(mJsonObjectUserFragment != null && !mJsonObjectUserFragment.getString("stage").equals("3")) {
                    Toast.makeText(getApplicationContext(), "La creación del usuario estara incompleta si sales. Termina de crear el usuario antes de salir", Toast.LENGTH_LONG).show();
                }else {
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }        }
    };

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setmJsonObjectUserFragment(JSONObject jsonObjectUserFragment) {
        mJsonObjectUserFragment = jsonObjectUserFragment;
    }

    public JSONObject getmJsonObjectUserFragment() {
        return mJsonObjectUserFragment;
    }
}
