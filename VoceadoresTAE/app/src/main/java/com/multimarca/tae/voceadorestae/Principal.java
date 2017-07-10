package com.multimarca.tae.voceadorestae;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.GCM.RegistrationService;
import com.multimarca.tae.voceadorestae.Objects.MenuElement;
import com.multimarca.tae.voceadorestae.adapters.MenuAdapter;
import com.multimarca.tae.voceadorestae.databases.DbManager;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The type Principal.
 */
public class Principal extends AppCompatActivity {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando información","Espere un momento.");
    DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ArrayList<MenuElement> optionsArray = new ArrayList<>();

        optionsArray.add(new MenuElement("Recarga Eléctronica","Recarga Eléctronica", new Intent(getApplicationContext(),Tae.class)));
        optionsArray.add(new MenuElement("Pago de Servicios","Pago de Servicios", new Intent(getApplicationContext(),Services.class)));
        optionsArray.add(new MenuElement("Consulta de saldo","Consulta de Saldo", new Intent(getApplicationContext(),Balance.class)));
        optionsArray.add(new MenuElement("Reporte de ventas","Reporte de Ventas", new Intent(getApplicationContext(),RVentas.class)));
        optionsArray.add(new MenuElement("Registrar Usuario","Registrar Usuario", new Intent(getApplicationContext(),NewUser.class)));
        optionsArray.add(new MenuElement("Reportar deposito","Reportar Depositos", new Intent(getApplicationContext(),Depositos.class)));
        optionsArray.add(new MenuElement("Reporte de traspasos","Reporte de Traspasos", new Intent(getApplicationContext(),RTraspasos.class)));
        optionsArray.add(new MenuElement("Traspaso","Traspaso", new Intent(getApplicationContext(),Traspaso.class)));
        optionsArray.add(new MenuElement("MAC Address","MAC Address", new Intent(getApplicationContext(),Macaddress.class)));
        optionsArray.add(new MenuElement("Notificaciones","Notificaciones", new Intent(getApplicationContext(),GCMNotifications.class)));
        optionsArray.add(new MenuElement("Salir","Salir", null));
        dbManager    = DbManager.getInstance(getApplicationContext());

        RecyclerView principalRecycler = (RecyclerView)findViewById(R.id.principal_recycler);
        principalRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MenuAdapter menuAdapter = new MenuAdapter(this, optionsArray);
//        principalRecycler.setHasFixedSize(true);
//        principalRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        principalRecycler.setAdapter(menuAdapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Global.SENT_TOKEN_TO_SERVER, false);
                if(sentToken) {
                }
            }
        };

        if(checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationService.class);
            startService(intent);
        }
        if(Global.ACTUALIZAR(getApplicationContext())) {
            updateCarriers();

        }
        registerReceiver();

    }


    /**
     * Update carriers.
     * Funcion para actualizar carriers
     */
    public void updateCarriers() {

        JSONObject jsonObject = new JSONObject();
        JSONObject dataToSend = new JSONObject();

        Bundle extras  = getIntent().getExtras();

        try {
            // request {"User":"UsuarioXXX", "Password":"PassXXX"}
            // WS nombre del webservice que se consultara

            jsonObject.put("User", Global.USUARIO(getApplicationContext()));
            jsonObject.put("Password", Global.PASS(getApplicationContext()));
            dataToSend.put("request", jsonObject.toString());
            dataToSend.put("ws", Global.WS);

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        //Peticion al webservice 
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
                                    Log.d("TAGGGGG",carrier.toString());
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

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Global.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("GCM", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
