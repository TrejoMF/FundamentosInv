package com.multimarca.tae.voceadorestae;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogDate;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Fragments.Drawer_menu;
import com.multimarca.tae.voceadorestae.Objects.Venta;
import com.multimarca.tae.voceadorestae.adapters.VentaAdapter;
import com.multimarca.tae.voceadorestae.utils.Functions;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RVentas extends AppCompatActivity {

    EditText editDate;
    TableLayout tableLayout;
    Context context;
    Toolbar toolbar;

    TextView fechaText;
    TextView montoText;
    TextView totalText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rventas);

//        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
        TextView medium_header_titulo  = (TextView)findViewById(R.id.medium_titulo);

        fechaText  = (TextView)findViewById(R.id.rventas_fecha_text);
        montoText  = (TextView)findViewById(R.id.rventas_monto_text);
        totalText  = (TextView)findViewById(R.id.rventas_totales_text);

        editDate = (EditText)findViewById(R.id.rventas_datepicker);
        toolbar = (Toolbar)findViewById(R.id.medium_toolbar);



        context = this;
        Bundle extras  = getIntent().getExtras();
        String titulo  = extras.getString("Titulo");

        Drawer_menu drawer_menu = (Drawer_menu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_menu);


        setSupportActionBar(toolbar);

        drawer_menu.setUp((DrawerLayout)findViewById(R.id.drawer_layout), toolbar, R.color.colorPrimary_blue400);
        toolbar.setTitle("Notificaciones");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);
        medium_header_titulo.setText(titulo);
        editDate.setInputType(EditorInfo.TYPE_NULL);
        editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();

                }
            }
        });
    }

    public void checkTransactions(final String date) {
        JSONObject params = new JSONObject();
        JSONObject tosend = new JSONObject();


        final ProgressFragment progressFragment = ProgressFragment.newInstance("Revisando Transferencias","Espere Porfavor!");
        try {
            params.put("User",Global.USUARIO(getApplicationContext()));
            params.put("Carrier_ID","TODO");
            params.put("date_sales",date);
            tosend.put("ws",Global.WS);
            tosend.put("request",params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new RestApiClient(
                Global.URL,
                "sales_report",
                tosend,
                RestApiClient.METHOD.POST,
                new RestApiClient.RestInterface() {
                    @Override
                    public void onFinish(String Result) {
                        progressFragment.dismiss(getSupportFragmentManager());
                        JSONObject response;
                        try {
                            response = new JSONObject(Result);
                            String array = response.getString("response");
                            JSONArray jsonArray = new JSONArray(array);
                            int length = jsonArray.length();
                            ArrayList<Venta> optionsVenta = new ArrayList<>();

                            float total = 0.0f;
                            for(int i =0; i<length; i++) {
                                try {
                                    JSONObject transaction = jsonArray.getJSONObject(i);
                                    total += Float.parseFloat(transaction.getString("Cantidad"));
                                    Venta venta = new Venta("Numero: \n"+transaction.getString("Telefono"), "Monto: \n" + Functions.FormatMoney(transaction.getString("Cantidad")), "Hora: \n"+transaction.getString("Fecha"), "Folio: \n"+transaction.getString("Folio_Operadora"), "Compañía: \n"+transaction.getString("Nombre"));
                                    optionsVenta.add(venta);
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            RecyclerView principalRecycler = (RecyclerView)findViewById(R.id.recycler_ventas);
                            principalRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            VentaAdapter menuAdapter = new VentaAdapter(getApplicationContext(), optionsVenta);
                            principalRecycler.setAdapter(menuAdapter);


                            totalText.setText(getString(R.string.rventas_text_hintTotakes) + " " + String.valueOf(length));
                            montoText.setText(getString(R.string.rventas_text_hint_totalVenta) + " " + Functions.FormatMoney(String.valueOf(total)));
                            fechaText.setText(getString(R.string.rventas_text_hintFecha) + " " + date);
                            editDate.clearFocus();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onBefore() {
                        progressFragment.show(getSupportFragmentManager(),"load_transactions");

                    }
                }
        ).execute();

    }

    public void  showDatePicker() {
        DialogFragment dateFragment = new DialogDate(new DialogDate.ListenDataSet() {
            @Override
            public void callback(View view, int year, int month, int day) {
                String dateFormat =  String.valueOf(day) + "/" + String.valueOf(month+1) +"/"+ String.valueOf(year);
                editDate.setText(dateFormat);
                checkTransactions(dateFormat);
            }

            @Override
            public void cancel() {
                editDate.clearFocus();

            }
        });
        dateFragment.show(getSupportFragmentManager(), "datepicker");
    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
