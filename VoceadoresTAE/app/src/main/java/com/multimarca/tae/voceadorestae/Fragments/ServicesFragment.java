package com.multimarca.tae.voceadorestae.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Objects.Carrier;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.Services;
import com.multimarca.tae.voceadorestae.databases.DbManager;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    Button acceptButton;
    Button cleanButton;
    Spinner spinnerCarrier;
    TextInputLayout referenceLayout;
    EditText amountText;
    EditText referenceText;
    TextView numberTextView;
    TextView companyTextView;
    TextView amountTextView;
    TextView titleHeader;
    AppBarLayout action_bar_big;
    ImageButton checkBalanceButton;

    String Amount;
    String Reference;
    String CarrierID;
    String CarrierName;
    String Title = "Pago de Servicios";

    public ServicesFragment() {
        // Required empty public constructor
    }

    public static ServicesFragment newInstance(){
        return new ServicesFragment();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_services, container, false);

        referenceLayout = (TextInputLayout)view.findViewById(R.id.service_layout_reference);
        referenceText   = referenceLayout.getEditText();
        amountText      = (EditText)view.findViewById(R.id.service_amount);
        acceptButton    = (Button)view.findViewById(R.id.service_send);
        cleanButton     = (Button)view.findViewById(R.id.tae_clean);
        spinnerCarrier  = (Spinner) view.findViewById(R.id.service_company);
        checkBalanceButton  = (ImageButton) view.findViewById(R.id.checkBalance);

        companyTextView = (TextView) getActivity().findViewById(R.id.big_header_companyText);
        amountTextView  = (TextView) getActivity().findViewById(R.id.big_header_amount);
        numberTextView  = (TextView) getActivity().findViewById(R.id.big_header_numberText);
        titleHeader     = (TextView) getActivity().findViewById(R.id.big_header_titulo);

        acceptButton.setOnClickListener(sendPayment);
        cleanButton.setOnClickListener(cleanClick);
        checkBalanceButton.setVisibility(View.GONE);
        checkBalanceButton.setOnClickListener(checkJMAS);

        DbManager dbManager = DbManager.getInstance(getActivity().getApplicationContext());
        final List<Carrier> carriers = dbManager.getAllCarriers(0);
        action_bar_big = (AppBarLayout)getActivity().findViewById(R.id.action_bar_big);

        int size = carriers.size();
        final List<String> carStrings = new ArrayList<>();
        carStrings.add("Compañia");
        for (int i = 0; i < size; i++) {

            String name = carriers.get(i).getName();
            carStrings.add(name);

        }


        companyTextView.setText(getString(R.string.data_reference_hint));
        referenceText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    action_bar_big.setExpanded(false, true);

                } else {
                    action_bar_big.setExpanded(true, true);

                }
            }
        });

        amountText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    action_bar_big.setExpanded(false, true);

                } else {
                    action_bar_big.setExpanded(true, true);

                }
            }
        });
        referenceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                numberTextView.setText(getString(R.string.data_reference_hint) + " " + s.toString());
            }
        });

        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                amountTextView.setText(getString(R.string.data_amount_hint) + " " + s.toString() );
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, carStrings
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCarrier.setAdapter(adapter);

        spinnerCarrier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    position -= 1;
                    Carrier carrier = carriers.get(position);
                    CarrierID = carrier.getCode();
                    CarrierName = carrier.getName();
                    companyTextView.setText(CarrierName);
                    if (CarrierID.equals("31")) {
                        checkBalanceButton.setVisibility(View.VISIBLE);

                    } else {
                        checkBalanceButton.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        amountText.getText();
        return view;
    }

    View.OnClickListener checkJMAS = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            JSONObject jsonToSend = new JSONObject();
            JSONObject params = new JSONObject();


            try {
                params.put("password", Global.PASS(getContext()));
                params.put("user", Global.USUARIO(getContext()));
                params.put("reference", referenceText.getText().toString());

                jsonToSend.put("request", params.toString());
                jsonToSend.put("ws",Global.WS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final ProgressFragment progressFragment = ProgressFragment.newInstance("Conectando", "Consultando información. Espere un momento");
            new RestApiClient(Global.URL, "checkjmas", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                @Override
                public void onFinish(String Result) {
                    progressFragment.dismiss(getActivity().getSupportFragmentManager());
                    if(Result != null) {

                        try {
                            JSONObject objectReceived = new JSONObject(Result);
                            String response = objectReceived.getString("response");
                            JSONObject objectResponse = new JSONObject(response);

                            Log.d("JMAS", objectResponse.getString("description"));

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Cliente: ").append(objectResponse.getString("nombre"));
                            stringBuilder.append("\n");
                            stringBuilder.append("Adeudo Total: ").append(objectResponse.getString("adeudo_total"));
                            stringBuilder.append("\n");
                            stringBuilder.append("Saldo Actual: ").append(objectResponse.getString("saldo_actual"));
                            stringBuilder.append("\n");
                            stringBuilder.append("Rezago: ").append(objectResponse.getString("rezago"));

                            DialogAlert dialogJMAS = DialogAlert.newInstance(stringBuilder.toString(), "Información de Cuenta", R.drawable.ic_info_blue_700_36dp);
                            dialogJMAS.show(getActivity().getSupportFragmentManager(), "info_jmas");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema con la conexión del servicio web", "Error de conexión",  R.drawable.ic_network_wifi_blue_a700_18dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_conect");

                    }


                }

                @Override
                public void onBefore() {
                    progressFragment.show(getActivity().getSupportFragmentManager(), "wait_amoment");
                }
            }).execute();

        }
    };

    View.OnClickListener sendPayment = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final ProgressFragment progressFragment = ProgressFragment.newInstance("Realizando petición", "Espere por favor");

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonSend   = new JSONObject();

            Amount = amountText.getText().toString();
            Reference = referenceText.getText().toString();
            if(!(Reference.equals("") || Amount.equals("") || CarrierName == null)) {
                clearFocuses();
                try {

                    jsonObject.put("Number", Reference);
                    jsonObject.put("Price", Amount);
                    jsonObject.put("Carrier", (CarrierID.length()<2)?"0"+CarrierID:CarrierID);
                    jsonObject.put("Carrier_Name", CarrierName);
                    jsonObject.put("Folio_POS", "6020000001");
                    jsonObject.put("IP", "127.0.0.1");
                    jsonObject.put("User", Global.USUARIO(getActivity().getApplicationContext()));
                    jsonObject.put("Password", Global.PASS(getActivity().getApplicationContext()));

                    jsonSend.put("request",jsonObject.toString());
                    jsonSend.put("ws", Global.WS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new RestApiClient(
                        Global.URL,
                        "request",
                        jsonSend,
                        RestApiClient.METHOD.POST,
                        new RestApiClient.RestInterface() {

                            @Override
                            public void onFinish(String Result) {
                                progressFragment.dismiss(getFragmentManager());
                                action_bar_big.setExpanded(true, true);

                                if (Result != null) {
                                    try {
                                        JSONObject jsonObjectResult = new JSONObject(Result);
                                        String stringResponse = jsonObjectResult.getString("response");
                                        JSONObject jsonResponse = new JSONObject(stringResponse);
                                        jsonResponse.put("reference", Reference);
                                        jsonResponse.put("carrier", CarrierName);
                                        jsonResponse.put("status", jsonObjectResult.getString("status"));


                                        ((Services) getActivity()).setData(jsonResponse);
                                        ((Services) getActivity()).getViewPager().setCurrentItem(1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema al conectar con el servicio web", "Problema de conexión", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "dialog_conexion");
                                }
                            }

                            @Override
                            public void onBefore() {
                                progressFragment.show(getFragmentManager(), "progress");

                            }
                        }).execute();
            }else{
                DialogAlert dialogAlert = DialogAlert.newInstance("Debes de introducir una referencia y un numero valido", "Error en Formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(),"error_formulario");
            }
        }
    };


    View.OnClickListener cleanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cleanALL();
        }
    };
    public void cleanALL(){
        referenceText.setText("");
        amountText.setText("");
        titleHeader.setText(Title);
        amountTextView.setText(getString(R.string.data_amount_hint));
        numberTextView.setText(getString(R.string.data_number_hint));
        returnState();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && referenceText != null) {
            cleanALL();
        }
    }

    @SuppressWarnings("ResourceType")
    public void returnState() {
        action_bar_big.setBackgroundColor(Color.parseColor(getString(R.color.colorPrimary_blue)));
        TextView textTitle = (TextView)getActivity().findViewById(R.id.big_header_titulo);

        textTitle.setText(R.string.title_services);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(getString(R.color.colorPrimaryDark_blue)));
        }
    }
    public void clearFocuses() {
        referenceText.clearFocus();
        amountText.clearFocus();
        View views = getActivity().getCurrentFocus();

        if (views != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(views.getWindowToken(), 0);
        }
    }
}
