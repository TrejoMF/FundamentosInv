package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.adapters.FragmentTaeAdapter;
import com.multimarca.tae.voceadorestae.databases.DbManager;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tae extends AppCompatActivity {

    static String ARG_TITLE = "TITLE";

    ViewPager viewPager;
    FragmentTaeAdapter taeAdapter;
    AppBarLayout appBarLayout;
    TextView big_header_title;
    Toolbar toolbar;

    JSONObject mJsonObject;

    String Title;
    String Number = "";
    String Carrier = "";
    String Folio = "";
    String Date = "";
    String Status = "";
    String mNotice = "";
    int mVersion=0;
    Double mBalance = 0.0;

    ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando información","Espere un momento.");
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tae);

        toolbar      = (Toolbar)findViewById(R.id.big_toolbar);
        viewPager    = (ViewPager)findViewById(R.id.tae_viewPager);
        appBarLayout = (AppBarLayout)findViewById(R.id.action_bar_big);
        dbManager    = DbManager.getInstance(getApplicationContext());
        big_header_title  = (TextView)findViewById(R.id.big_header_titulo);
        final TextView amountText = (TextView)findViewById(R.id.big_header_amount);

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);

        mVersion = Global.VCARRIERS(this);
        toolbar.setTitle("Recargas Electronicas");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    amountText.setVisibility(View.VISIBLE);
                } else {
                    amountText.setVisibility(View.GONE);

                }
            }
        });

        JSONObject jsonObject = new JSONObject();
        JSONObject dataToSend = new JSONObject();

        Bundle extras  = getIntent().getExtras();

        Title = extras.getString("Titulo");
        big_header_title.setText(Title);

        FragmentManager fm = getSupportFragmentManager();
        taeAdapter = new FragmentTaeAdapter(fm);

        try {

            jsonObject.put("User", Global.USUARIO(getApplicationContext()));
            jsonObject.put("Password", Global.PASS(getApplicationContext()));
            dataToSend.put("request", jsonObject.toString());
            dataToSend.put("ws", Global.WS);

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        if(Global.ACTUALIZAR(getApplicationContext())) {
            updateCarriers();

        }else {
            viewPager.setCurrentItem(1);
            viewPager.setAdapter(taeAdapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_carriers:
                updateCarriers();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    public void updateCarriers() {

        JSONObject jsonObject = new JSONObject();
        JSONObject dataToSend = new JSONObject();

        Bundle extras  = getIntent().getExtras();

        try {

            jsonObject.put("User", Global.USUARIO(getApplicationContext()));
            jsonObject.put("Password", Global.PASS(getApplicationContext()));
            dataToSend.put("request", jsonObject.toString());
            dataToSend.put("ws", Global.WS);

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        new RestApiClient(
                Global.URL,
                "carriers2_0",
                dataToSend,
                RestApiClient.METHOD.POST,
                new RestApiClient.RestInterface() {
                    @Override
                    public void onFinish(String Result) {
                        if (Result != null) {
                            try {
                                if (!isFinishing()) {
                                    progressFragment.dismiss(getSupportFragmentManager());
                                }
                                JSONObject jsonObject = new JSONObject(Result);
                                String jsonResponseString = jsonObject.getString("response");
                                JSONArray jsonResponse = new JSONArray(jsonResponseString);
                                dbManager.truncateCarriers();
                                for (int i = 0; i < jsonResponse.length(); i++) {
                                    JSONObject carrier = jsonResponse.getJSONObject(i);
                                    dbManager.InsertCarrier(carrier.getString("name"), carrier.getString("_id"), carrier.getString("prices"), carrier.getString("observation"), carrier.getInt("type"));
                                }
                                if(jsonResponse.length()>0)
                                    Global.EdVACTUALIZAR(getApplicationContext(), false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            DialogAlert dialogAlert = DialogAlert.newInstance("Ocurrio un problema al conectar con el servidor","Error en conexión", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                            dialogAlert.show(getSupportFragmentManager(),"error_conexion");
                        }
                    }
                    @Override
                    public void onBefore() {
                        if (!isFinishing()) {
                            progressFragment.show(getSupportFragmentManager(), "dialog_fragment");
                        }
                    }
                }).execute();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TITLE, Title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Title = savedInstanceState.getString(ARG_TITLE);
        big_header_title.setText(Title);

    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.tae_viewPager);
        }
        return viewPager;
    }


    public void setData(JSONObject jsonObject) {
        mJsonObject = jsonObject;
    }
    public JSONObject getData() {

        try {
            if(mJsonObject != null) {
                int version = mJsonObject.getInt("productVersion");
                if (version != Global.VCARRIERS(this)) {
                    updateCarriers();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mJsonObject;
    }
    public void setData(String status, String number, String carrier, String response, String folio, String date, String notice, int version, Double balance) {
        Number = number;
        Carrier = carrier;
        Folio = folio;
        Date = date;
        mNotice = notice;
        mVersion = version;
        mBalance = balance;
        Title = response;
        Status = status;
        big_header_title.setText(Title);
    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
