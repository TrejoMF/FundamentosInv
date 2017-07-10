package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.utils.Functions;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Traspaso extends AppCompatActivity {


    ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando Datos", "Espera un momento.");

    Button btnTraspass;

    TextView initialBalanceText;
    TextView clientBalanceText;

    Spinner spinnerClients;
    EditText editAmount;
    EditText editObsv;
    JSONArray clientsArray = new JSONArray();
    ArrayList<String> clientsStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traspaso);

        String title = getIntent().getExtras().getString("Titulo");
        TextView textTitle = (TextView)findViewById(R.id.medium_titulo);
        textTitle.setText(title);

        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.put("ws", Global.WS_D);
            jsonToSend.put("request", Global.PAPI(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.medium_toolbar);
        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);
        clientBalanceText =  (TextView)findViewById(R.id.traspaso_value_clientBalance);
        initialBalanceText =  (TextView)findViewById(R.id.traspaso_value_currentBalance);

        setSupportActionBar(toolbar);
        drawer_menu.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_indi);
        setSupportActionBar(toolbar);

        spinnerClients = (Spinner)findViewById(R.id.clientCbox);
        editAmount = (EditText)findViewById(R.id.traspaso_amount);
        editObsv = (EditText)findViewById(R.id.traspaso_comment);
        btnTraspass = (Button) findViewById(R.id.traspaso_button_traspaso);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);

        btnTraspass.setOnClickListener(sendTrass);

        new RestApiClient(Global.URL, "clientsCbox", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(Result);

                    String jsonResponseString = jsonObject.getString("response");
                    JSONArray jsonResponse = new JSONArray(jsonResponseString);
                    clientsStrings = new ArrayList<>();

                    clientsStrings.add("Cliente");
                    for (int i = 1; i < jsonResponse.length(); i++) {
                        JSONObject Client = jsonResponse.getJSONObject(i);

                        if(Client.getString("Tipo_Perfil").equals("PDV")) {
                            clientsStrings.add(Client.getString("Nombre"));
                            clientsArray.put(Client);
                        }

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getApplicationContext(), R.layout.spinner_dropdown_item, clientsStrings
                    );
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinnerClients.setAdapter(adapter);
                    spinnerClients.setOnItemSelectedListener(selectedItem);
                    checkBalance();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore() {
                progressFragment.show(getSupportFragmentManager(), "progress_data");
            }
        }).execute();
    }
/*
{"User":"6566444444","Observation":"test","Value":"1","User_Update":"6561827062","Password":"Prueba.1$"}
 */

    public void checkBalance(){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonSend = new JSONObject();

        try {
            jsonObject.put("User", Global.USUARIO(getApplicationContext()));
            jsonObject.put("Password", Global.PASS(getApplicationContext()));
            jsonSend.put("ws", Global.WS);
            jsonSend.put("request", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RestApiClient(Global.URL, "balance", jsonSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                progressFragment.dismiss(getSupportFragmentManager());
                JSONObject jsonObject;
                JSONObject jsonOResponse;
                if (Result != null) {
                    try {
                        jsonObject = new JSONObject(Result);
                        String jsonResponseString = jsonObject.getString("response");
                        jsonOResponse = new JSONObject(jsonResponseString);
                        String balanceString = jsonOResponse.getString("balance");

                        initialBalanceText.setText(Functions.FormatMoney(balanceString));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema al conectar con el servicio web", "Problema de conexión",R.drawable.ic_network_wifi_blue_a700_18dp);
                    dialogAlert.show(getSupportFragmentManager(), "error_conect");
                }
            }
            @Override
            public void onBefore() {

            }
        }).execute();
    }
    AdapterView.OnItemSelectedListener selectedItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("POSITION", String.valueOf(position));
            if(position!=0) {
                try {
                    position -= 1;
                    JSONObject jsonToSend = new JSONObject();
                    if(position>=1){
                        JSONObject pdvObject = clientsArray.getJSONObject(position);
                        String pdv = pdvObject.getString("Id_Punto_Venta");
                        jsonToSend.put("request", pdv);
                    }else{
                        jsonToSend.put("request", Global.USUARIO(getApplicationContext()));
                    }

                    jsonToSend.put("ws", Global.WS);
                    new RestApiClient(Global.URL, "balance_pdv", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                        @Override
                        public void onFinish(String Result) {
                            progressFragment.dismiss(getSupportFragmentManager());
                            JSONObject jsonObject;
                            JSONObject jsonOResponse;
                            if (Result != null) {
                                try {
                                    Log.d("TAG",Result);
                                    jsonObject = new JSONObject(Result);
                                    String jsonResponseString = jsonObject.getString("response");
                                    jsonOResponse = new JSONObject(jsonResponseString);
                                    String saldo = jsonOResponse.getString("saldo_disponible");
                                    clientBalanceText.setText(Functions.FormatMoney(saldo));


                                } catch (JSONException e) {
                                    clientBalanceText.setText("0.00 E!");

                                    e.printStackTrace();
                                }
                            }else {
                                DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema al conectar con el servicio web", "Problema de conexión",R.drawable.ic_network_wifi_blue_a700_18dp);
                                dialogAlert.show(getSupportFragmentManager(), "error_conect");
                            }
                        }
                        @Override
                        public void onBefore() {
                            progressFragment.show(getSupportFragmentManager(), "balance_progess");

                        }
                    }).execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.d("TAG", "Estas en el titulo");
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener sendTrass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            JSONObject jsonParams = new JSONObject();
            JSONObject jsonToSend = new JSONObject();

            if(editAmount.getText().toString().length() < 1 || editAmount.getText().toString().equals("0")) {
                DialogAlert dialogAlert = DialogAlert.newInstance("Es necesario asignar un monto","Error en formulario",R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getSupportFragmentManager(),"error_deposit_dialog");
                return;
            }

            if (spinnerClients.getSelectedItemPosition() == 0) {
                DialogAlert dialogAlert = DialogAlert.newInstance("Es necesario seleccionar un cliente antes de continuar","Error en formulario",R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getSupportFragmentManager(),"error_deposit_dialog");
            } else {

                try {
                    JSONObject clientObject = clientsArray.getJSONObject(spinnerClients.getSelectedItemPosition()-1);
                    String clientUpdate;
                    if(spinnerClients.getSelectedItemPosition() == 1) {
                        clientUpdate = Global.USUARIO(getApplicationContext());
                    }else{
                        clientUpdate = clientObject.getString("Id_Punto_Venta");
                    }
                    jsonParams.put("User_Update", Global.USUARIO(getApplicationContext()));
                    jsonParams.put("Observation", editObsv.getText().toString());
                    jsonParams.put("Value", editAmount.getText().toString());
                    jsonParams.put("User", clientUpdate);
                    jsonParams.put("Password", Global.PASS(getApplicationContext()));

                    jsonToSend.put("ws", Global.WS);
                    jsonToSend.put("request", jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final ProgressFragment progressFragmentInside = ProgressFragment.newInstance("Realizando Transferencia", "Espera un momento.");
                new RestApiClient(Global.URL, "passMoney", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                    @Override
                    public void onFinish(String Result) {
                        progressFragmentInside.dismiss(getSupportFragmentManager());
                        try {
                            JSONObject jsonResponse = new JSONObject(Result);
                            String status = jsonResponse.getString("status");
                            if(status.equals("1")) {

                                String response = jsonResponse.getString("response");
                                JSONArray jsonArrayResponse = new JSONArray(response);
                                String stringResponse = jsonArrayResponse.getString(0);
                                JSONObject jsonObjectResponse = new JSONObject(stringResponse);

                                String ResponseTransfer = jsonObjectResponse.getString("Response");
                                String ResponseCtd = Functions.FormatMoney(jsonObjectResponse.getString("Saldo_Apply"));
                                spinnerClients.setSelection(0);
                                editObsv.setText("");
                                editAmount.setText("");
                                DialogAlert dialogAlert;
                                switch (ResponseTransfer) {
                                    case "1":
                                        dialogAlert = DialogAlert.newInstance("Transferencia Exitosa! Se transfirieron: "+ResponseCtd+" con exito.","Estatus Transferencia",R.drawable.ic_call_made_indigo_700_18dp);
                                        dialogAlert.show(getSupportFragmentManager(),"correct_transfer_dialog");
                                        break;
                                    case "2":
                                        dialogAlert = DialogAlert.newInstance("Saldo Insuficiente!","Estatus Transferencia",R.drawable.ic_call_made_indigo_700_18dp);
                                        dialogAlert.show(getSupportFragmentManager(),"nomoneynofunny_transfer_dialog");
                                        break;
                                    case "3":
                                        dialogAlert = DialogAlert.newInstance("El Padre no corresponde.","Estatus Transferencia",R.drawable.ic_call_made_indigo_700_18dp);
                                        dialogAlert.show(getSupportFragmentManager(),"nelson_transfer_dialog");
                                        break;
                                    case "4":
                                        dialogAlert = DialogAlert.newInstance("Punto de venta no encontrado.","Estatus Transferencia",R.drawable.ic_call_made_indigo_700_18dp);
                                        dialogAlert.show(getSupportFragmentManager(),"404_transfer_dialog");
                                        break;
                                    default:
                                        dialogAlert = DialogAlert.newInstance("La respuesta no es conocida solicita asistencia telefonica.","Estatus Transferencia",R.drawable.ic_call_made_indigo_700_18dp);
                                        dialogAlert.show(getSupportFragmentManager(),"whaaaat_transfer_dialog");
                                        break;
                                }
                            }else{
                                DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema al realizar la peticion","Problema Desconocido", R.drawable.ic_error_red_900_24dp);
                                dialogAlert.show(getSupportFragmentManager(),"error_dialog_unknown");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            checkBalance();
                        }
                    }

                    @Override
                    public void onBefore() {
                        progressFragmentInside.show(getSupportFragmentManager(), "progress_inside_wuju");
                    }
                }).execute();
            }
        }
    };

    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}