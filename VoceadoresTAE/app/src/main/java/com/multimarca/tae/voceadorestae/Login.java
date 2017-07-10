package com.multimarca.tae.voceadorestae;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.utils.Validators;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends FragmentActivity {

    Button Login_Button;
    JSONObject jsonObject;
    JSONObject newJsonObject;
    TextInputLayout textInputLayoutUser;
    TextInputLayout textInputLayoutPass;
    Boolean error = false;

    ProgressFragment progressFragment = ProgressFragment.newInstance("Inicio de sesión","Validando usuario y contraseña");
    DialogAlert dialogAlertSesion = DialogAlert.newInstance("Usuario o contraseña incorrecta", "Error al autenticar", R.drawable.ic_account_circle_red_a700_24dp);
    DialogAlert dialogAlertError = DialogAlert.newInstance("Hubo un problema al conectar con el servicio web", "Error de conexión", R.drawable.ic_signal_wifi_off_blue_500_18dp);

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Global.LOGGED(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            finish();
        }
        Login_Button = (Button) findViewById(R.id.login_button);

        jsonObject = new JSONObject();
        newJsonObject = new JSONObject();
        textInputLayoutUser = (TextInputLayout) findViewById(R.id.login_layout_user);
        textInputLayoutPass = (TextInputLayout) findViewById(R.id.login_layout_pass);

        Login_Button.setOnClickListener(ListenClick);

    }


    View.OnClickListener ListenClick = new View.OnClickListener() {

        @SuppressWarnings("ConstantConditions")
        @Override
        public void onClick(View v) {
            final String User;
            User = textInputLayoutUser.getEditText().getText().toString();
            final String Pass;
            Pass = textInputLayoutPass.getEditText().getText().toString();

            EditText textViewUser = textInputLayoutUser.getEditText();
            EditText textViewPass = textInputLayoutPass.getEditText();

            textViewUser.addTextChangedListener(new Watcher(textInputLayoutUser, "Introduzca un usuario"));
            textViewPass.addTextChangedListener(new Watcher(textInputLayoutPass, "Introduzca una contraseña"));


            if (!Validators.isULength(User, 0)) {
                error = Validators.showError(textInputLayoutUser, "Introduzca un usuario");
            }
            if (!Validators.isULength(Pass, 0)) {
                error = Validators.showError(textInputLayoutPass, "Introduzca una contraseña");
            }
            if (error)
                return;
            try {
                jsonObject.put("User", User);
                jsonObject.put("Password", Pass);
                newJsonObject.put("request", jsonObject.toString());
                newJsonObject.put("ws", Global.WS_D);
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }

            new RestApiClient(
                    Global.URL,
                    "login",
                    newJsonObject,
                    RestApiClient.METHOD.POST,
                    new RestApiClient.RestInterface() {
                        @Override
                        public void onFinish(String Result) {
                            try {
                                Login_Button.setClickable(true);

                                if (!isFinishing()) {
                                    progressFragment.dismiss(getSupportFragmentManager());
                                }

                                if (Result != null) {
                                    JSONObject jsonObject = new JSONObject(Result);
                                    String jsonresponse = jsonObject.getString("response");

                                    JSONObject jsonResponseObject = new JSONObject(jsonresponse);

                                    String status = jsonObject.getString("status");
                                    if (status.equals("1")) {
                                        Intent intent = new Intent(getApplicationContext(), Principal.class);
                                        startActivity(intent);

                                        String tipo_perfil = jsonResponseObject.getString("tipo_perfil");

                                        Global.EdLOGGED(getApplicationContext());
                                        Global.EdUSUARIO(getApplicationContext(), User);
                                        Global.EdPASS(getApplicationContext(), Pass);
                                        Global.EdNAME(getApplicationContext(), jsonResponseObject.getString("nombre"));

                                        switch (tipo_perfil) {
                                            case "PDV":
                                                Global.EdPAPI(getApplicationContext(), jsonResponseObject.getString("id_punto_venta"));
                                                break;
                                            case "OPE":
                                                Global.EdPAPI(getApplicationContext(), jsonResponseObject.getString("id_padre"));
                                                Global.EdUSUARIO(getApplicationContext(), jsonResponseObject.getString("id_padre"));

                                                break;
                                            default:
                                                Global.EdPAPI(getApplicationContext(), "NULL");
                                                break;
                                        }

                                        Global.EdTIPOP(getApplicationContext(), tipo_perfil);
                                        finish();
                                    } else {
                                        if (!isFinishing()) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialogAlertSesion.show(getSupportFragmentManager(), "dialog_alert");
                                                }
                                            });

                                        }
                                    }
                                } else {
                                    if (!isFinishing()) {
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialogAlertError.show(getSupportFragmentManager(), "dialog_alert");

                                            }
                                        });

                                    }
                                }
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();
                                progressFragment.dismiss();

                            }
                        }

                        @Override
                        public void onBefore() {
                            if (!isFinishing()) {
                                progressFragment.show(getSupportFragmentManager(), "dialog_fragment");
                            }

                            Login_Button.setClickable(false);
                        }
                    }).execute();
        }
    };

    class Watcher implements TextWatcher {

        private TextInputLayout __textInputLayout;
        private String __mensaje;

        public Watcher(TextInputLayout textInputLayout, String mensaje) {
            this.__textInputLayout = textInputLayout;
            this.__mensaje = mensaje;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(!Validators.isULength(s.toString(),0)) {
                error = Validators.showError(this.__textInputLayout, this.__mensaje);
            }else{
                Validators.hideError(textInputLayoutUser);
                error = false;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!Validators.isULength(s.toString(),0)) {
                error = Validators.showError(this.__textInputLayout, this.__mensaje);
            }else{
                Validators.hideError(this.__textInputLayout);
                error = false;
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void updatePass(View view){
        Intent intent = new Intent(getApplicationContext(), UpdateCredentials.class);
        startActivity(intent);
    }
}
