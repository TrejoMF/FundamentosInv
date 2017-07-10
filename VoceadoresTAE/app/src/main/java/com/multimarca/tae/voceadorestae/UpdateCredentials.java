package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCredentials extends AppCompatActivity {

    EditText oldPass;
    EditText newPass;
    EditText newRePass;
    EditText usertoUpdate;

    Button buttonUpdate;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_credentials);

        oldPass = (EditText)findViewById(R.id.oldPass);
        newPass = (EditText)findViewById(R.id.newPass);
        newRePass = (EditText)findViewById(R.id.newRePass);
        usertoUpdate = (EditText)findViewById(R.id.usertoupdate);
        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);

//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
//        backButton.setOnClickListener(onClickBack);
        toolbar      = (Toolbar)findViewById(R.id.medium_toolbar);

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);



        buttonUpdate = (Button)findViewById(R.id.updatePass);
        buttonUpdate.setOnClickListener(updateCredentials);
        medium_header_titulo.setText("Actualizar Credenciales");


    }


    View.OnClickListener updateCredentials = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final ProgressFragment progressFragment = ProgressFragment.newInstance("Esperando Respuesta", "Se esta realizando la peticion, espere un momento");
            JSONObject jsonParams = new JSONObject();
            JSONObject jsonToSend = new JSONObject();

            String usertoUpdateS   = usertoUpdate.getText().toString();
            String oldPassString   = oldPass.getText().toString();
            String newPassString   = newPass.getText().toString();
            String newRePassString = newRePass.getText().toString();


            if(newPassString.equals(newRePassString)) {
                try {
                    jsonParams.put("User", usertoUpdateS);
                    jsonParams.put("Password", oldPassString);
                    jsonParams.put("New_Password", newPassString);
                    jsonToSend.put("ws", Global.WS);
                    jsonToSend.put("request", jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new RestApiClient(Global.URL, "updatePass", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                @Override
                public void onFinish(String Result) {
                    progressFragment.dismiss(getSupportFragmentManager());
                    try {
                        JSONObject jsonResponse = new JSONObject(Result);

                        String status = jsonResponse.getString("status");

                        if(status.equals("0"))
                        {
                            DialogAlert dialogAlert = DialogAlert.newInstance("Usuario o antigua contraseña no valida", "Error al actualizar", R.drawable.ic_error_red_900_24dp);
                            dialogAlert.show(getSupportFragmentManager(),"error_update");
                        }else{
                            DialogAlert dialogAlert = DialogAlert.newInstance("contraseña actualizada correctamente", "Credenciales Actualizadas.", R.drawable.ic_account_circle_red_a700_24dp);
                            dialogAlert.show(getSupportFragmentManager(),"correct_update");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        DialogAlert dialogAlert = DialogAlert.newInstance("Respuesta del servidor inesperada. Revisa tu coneccion a internet", "Error en servidor", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                        dialogAlert.show(getSupportFragmentManager(), "error_sync");
                    }
                    Log.d("UPDATED",Result);
                }

                @Override
                public void onBefore() {
                    progressFragment.show(getSupportFragmentManager(), "wait_dialog");
                }
            }).execute();

            }else {
                DialogAlert dialogAlert = DialogAlert.newInstance("la nueva contraseña no coincide", "Error en formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getSupportFragmentManager(), "dialog_errorForm");
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
