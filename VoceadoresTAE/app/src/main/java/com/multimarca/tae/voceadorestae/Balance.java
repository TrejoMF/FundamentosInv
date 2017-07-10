package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.utils.Functions;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Balance extends AppCompatActivity {


    private static String ARG_INIT = "INITIAL";
    private static String ARG_VENTAS = "VENTAS";
    private static String ARG_COMPRAS = "COMPRAS";
    private static String ARG_ACTUAL = "ACTUAL";

    String initialString = "";
    String comprasString = "";
    String ventasString  = "";
    String balanceString = "";

    TextView inicialText;
    TextView compraslText;
    TextView ventasText;
    TextView saldoText;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.medium_header_collapsing);
        collapsingToolbarLayout.setTitle("Consulta de saldo");

        inicialText = (TextView) findViewById(R.id.balance_init_amount);
        compraslText = (TextView) findViewById(R.id.balance_today_amount);
        ventasText = (TextView) findViewById(R.id.balance_todaySales_amount);
        saldoText = (TextView) findViewById(R.id.balance_totalBalance_amount);
        toolbar = (Toolbar)findViewById(R.id.medium_toolbar);

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        setSupportActionBar(toolbar);

        drawer_menu.setUp((DrawerLayout)findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_teal);



        String title = getIntent().getExtras().getString("Titulo");

        TextView textTitle = (TextView)findViewById(R.id.medium_titulo);
        textTitle.setText(title);

        collapsingToolbarLayout.setTitleEnabled(true);
        Toolbar toolbar = (Toolbar)findViewById(R.id.medium_toolbar);
        toolbar.setTitle("Consulta de Saldo");


//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
//        backButton.setOnClickListener(onClickBack);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
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
        if(savedInstanceState == null) {
            final ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando Balance", "Espere un momento");
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

                            initialString = jsonOResponse.getString("saldo_inicial");
                            comprasString = jsonOResponse.getString("compras");
                            ventasString = jsonOResponse.getString("ventas");
                            balanceString = jsonOResponse.getString("balance");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        inicialText.setText(Functions.FormatMoney(initialString));
                        compraslText.setText(Functions.FormatMoney(comprasString));
                        ventasText.setText(Functions.FormatMoney(ventasString));
                        saldoText.setText(Functions.FormatMoney(balanceString));

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
        }


    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        initialString = savedInstanceState.getString(ARG_INIT);
        comprasString = savedInstanceState.getString(ARG_COMPRAS);
        ventasString = savedInstanceState.getString(ARG_VENTAS);
        balanceString = savedInstanceState.getString(ARG_ACTUAL);

        inicialText.setText(initialString);
        compraslText.setText(comprasString);
        ventasText.setText(ventasString);
        saldoText.setText(balanceString);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ACTUAL, balanceString);
        outState.putString(ARG_INIT, initialString);
        outState.putString(ARG_COMPRAS, comprasString);
        outState.putString(ARG_VENTAS, ventasString);

    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
