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
import com.multimarca.tae.voceadorestae.adapters.FragmentServicesAdapter;
import com.multimarca.tae.voceadorestae.databases.DbManager;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Services extends AppCompatActivity {

    ViewPager viewPager;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando información","Espere un momento.");
    DbManager dbManager;
    FragmentServicesAdapter servicesAdapter;
    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        viewPager = (ViewPager)findViewById(R.id.tae_viewPager);
        appBarLayout = (AppBarLayout)findViewById(R.id.action_bar_big);
        dbManager    = DbManager.getInstance(getApplicationContext());


        toolbar = (Toolbar)findViewById(R.id.big_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Pago de Servicios");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        final TextView amountText = (TextView)findViewById(R.id.big_header_amount);
        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);

        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_blue);


        TextView big_header_titulo  = (TextView)findViewById(R.id.big_header_titulo);
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");

        big_header_titulo.setText(titulo);

        FragmentManager fm = getSupportFragmentManager();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0) {
                    amountText.setVisibility(View.VISIBLE);
                    //grandote
                }else{
                    amountText.setVisibility(View.GONE);
                    //chiquito

                }
            }
        });
        servicesAdapter = new FragmentServicesAdapter(fm);

        if(Global.ACTUALIZAR(getApplicationContext())) {
            updateCarriers();

        }else {
            viewPager.setCurrentItem(1);
            viewPager.setAdapter(servicesAdapter);
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
                                Global.EdTrueACTUALIZAR(getApplicationContext());
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

    JSONObject mJsonObject;

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

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.tae_viewPager);
        }
        return viewPager;
    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
