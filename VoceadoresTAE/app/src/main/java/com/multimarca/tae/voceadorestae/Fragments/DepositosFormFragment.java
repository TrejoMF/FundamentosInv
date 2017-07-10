package com.multimarca.tae.voceadorestae.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.multimarca.tae.voceadorestae.Depositos;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositosFormFragment extends Fragment {


    public DepositosFormFragment() {
        // Required empty public constructor
    }

    List<JSONObject> clientsObjects;
    List<String> clientsStrings;

    Spinner bancoSpinner;
    Spinner tipoSpinner;
    Spinner clientsSpinner;
    Spinner accountSpinner;
    Button nextButton;
    JSONObject mJsonToSend = new JSONObject();


    JSONArray mBankObject = new JSONArray();
    JSONArray mTypeObject = new JSONArray();
    JSONArray mAccountObject = new JSONArray();

    public static DepositosFormFragment newInstance() {

        Bundle args = new Bundle();
        DepositosFormFragment fragment = new DepositosFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_depositos_form, container, false);

        bancoSpinner   = (Spinner)view.findViewById(R.id.depositos_bancoField);
        tipoSpinner    = (Spinner)view.findViewById(R.id.depositos_tipoTField);
        clientsSpinner = (Spinner)view.findViewById(R.id.depositos_clientField);
        accountSpinner = (Spinner)view.findViewById(R.id.depositos_accountField);
        nextButton     = (Button)view.findViewById(R.id.depositos_form_next);


        nextButton.setOnClickListener(onClickNext);

        final JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.put("plataform", Global.PLATAFORM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando Datos", "Espere un momento");


        new RestApiClient(Global.URL_D, "api/banks/complete.info/", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                try {
                    if(Result != null) {
                        JSONObject jsonReceived = new JSONObject(Result);

                        String banksString = jsonReceived.getString("banks");
                        JSONArray banksArray = new JSONArray(banksString);

                        final List<String> bancosStrings = new ArrayList<>();
                        bancosStrings.add("Banco");
                        int lengthBank = banksArray.length();
                        for (int i = 0; i < lengthBank; i++) {
                            String bankString = banksArray.getString(i);
                            JSONObject bankJson = new JSONObject(bankString);
                            mBankObject.put(bankJson);

                            bancosStrings.add(bankJson.getString("name"));

                        }
                        ArrayAdapter<String> bancoAdapter = new ArrayAdapter<>(
                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, bancosStrings
                        );


                        bancoSpinner.setAdapter(bancoAdapter);
                        bancoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    try {
                                        position -= 1;
                                        JSONObject bancObj = mBankObject.getJSONObject(position);
                                        String transfersString = bancObj.getString("transfers");
                                        JSONArray transfersArray = new JSONArray(transfersString);

                                        String accountsString = bancObj.getString("accounts");
                                        JSONArray accountsArray = new JSONArray(accountsString);
                                        mJsonToSend.put("bank_id", bancObj.getString("id"));

                                        int lengthTransfer = transfersArray.length();
                                        List<String> tipoStrings = new ArrayList<>();
                                        tipoStrings.add("Tipo deposito");
                                        mTypeObject = new JSONArray();
                                        for (int i = 0; i < lengthTransfer; i++) {
                                            String transferType = transfersArray.getString(i);
                                            JSONObject transferObject = new JSONObject(transferType);
                                            mTypeObject.put(transferObject);
                                            Log.d("TIPOSSSSS", transferObject.getString("id"));
                                            tipoStrings.add(transferObject.getString("description"));
                                        }


                                        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(
                                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, tipoStrings
                                        );

                                        tipoSpinner.setAdapter(tipoAdapter);

                                        final List<String> accountsStrings = new ArrayList<>();
                                        int lengthAccounts = accountsArray.length();
                                        mAccountObject = new JSONArray();
                                        accountsStrings.add("Cuenta");
                                        for (int i = 0; i < lengthAccounts; i++) {
                                            String accountString = accountsArray.getString(i);
                                            JSONObject accountJson = new JSONObject(accountString);
                                            mAccountObject.put(accountJson);

                                            accountsStrings.add(accountJson.getString("name"));

                                        }
                                        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(
                                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, accountsStrings
                                        );

                                        accountSpinner.setAdapter(accountAdapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mJsonToSend.remove("bank_id");
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onBefore() {

            }
        }).execute();

        try {
            jsonToSend.put("ws", Global.WS_D);
            jsonToSend.put("request", Global.PAPI(getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new RestApiClient(Global.URL, "clientsCbox", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                progressFragment.dismiss(getActivity().getSupportFragmentManager());
                JSONObject jsonObject;
                try {
                    if(Result != null) {
                        jsonObject = new JSONObject(Result);

                        String jsonResponseString = jsonObject.getString("response");
                        JSONArray jsonResponse = new JSONArray(jsonResponseString);
                        clientsStrings = new ArrayList<>();
                        clientsObjects = new ArrayList<>();

                        clientsStrings.add("Cliente");
                        for (int i = 1; i < jsonResponse.length(); i++) {
                            JSONObject Client = jsonResponse.getJSONObject(i);

                            if (Client.getString("Tipo_Perfil").equals("PDV")) {
                                clientsObjects.add(Client);
                                clientsStrings.add(Client.getString("Nombre"));
                            }

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, clientsStrings
                        );
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        clientsSpinner.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore() {
                progressFragment.show(getActivity().getSupportFragmentManager(), "progress_data");
            }
        }).execute();

        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("POSITION", String.valueOf(position));
                if(position!=0) {
                    try {
                        position -= 1;
                        String Parent_ID = clientsObjects.get(position).getString("Id_Punto_Venta");

                        JSONObject jsonToSend = new JSONObject();

                        if(position!=0){
                            jsonToSend.put("request", Parent_ID);
                        }else{
                            jsonToSend.put("request", Global.USUARIO(getContext()));
                        }

                        jsonToSend.put("ws", Global.WS_D);

                        new RestApiClient(
                                Global.URL,
                                "search_client",
                                jsonToSend,
                                RestApiClient.METHOD.POST,
                                new RestApiClient.RestInterface() {
                                    @Override
                                    public void onFinish(String Result) {
                                        progressFragment.dismiss(getActivity().getSupportFragmentManager());
                                        if (Result != null) {
                                            JSONObject resultObject;
                                            try {
                                                resultObject = new JSONObject(Result);
                                                String responseString = resultObject.getString("response");
                                                JSONArray responseArray = new JSONArray(responseString);
                                                String infoString = responseArray.getString(0);
                                                JSONObject infoObject = new JSONObject(infoString);

                                                mJsonToSend.put("sales_point", infoObject.getString("Id_Punto_Venta"));
                                                mJsonToSend.put("sales_point_name", infoObject.getString("Nombre"));
                                                mJsonToSend.put("reference", infoObject.getString("Referencia"));
                                                mJsonToSend.put("email", infoObject.getString("email"));
                                                Log.d("INFOSALES", mJsonToSend.toString());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onBefore() {
                                        progressFragment.show(getActivity().getSupportFragmentManager(), "load_detailClient");
                                    }
                                }
                        ).execute();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    mJsonToSend.remove("sales_point");
                    mJsonToSend.remove("sales_point_name");
                    mJsonToSend.remove("reference");
                    mJsonToSend.remove("email");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    try {
                        position -= 1;
                        String transferString = mAccountObject.getString(position);
                        JSONObject transferObject = new JSONObject(transferString);
                        mJsonToSend.put("account_id", transferObject.getString("id"));
                        Log.d("TAGGGGGG", mJsonToSend.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    mJsonToSend.remove("account_id");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tipoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    try {
                        position -= 1;
                        String typeString = mTypeObject.getString(position);
                        JSONObject typeObject = new JSONObject(typeString);
                        mJsonToSend.put("VALIDATIONS", typeObject.getString("validations"));
                        mJsonToSend.put("type", typeObject.getString("id"));
                        Log.d("VA", mJsonToSend.getString("type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    mJsonToSend.remove("type");
                    mJsonToSend.remove("VALIDATIONS");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && mJsonToSend.has("bank_id")) {
            Log.d("TADDDDD",mJsonToSend.toString());
            mJsonToSend = ((Depositos)getActivity()).getData();
            if(mJsonToSend != null) {
                Log.d("TADDDDD-AFTER", mJsonToSend.toString());
                bancoSpinner.setSelection(0);
                accountSpinner.setSelection(0);
                tipoSpinner.setSelection(0);
                clientsSpinner.setSelection(0);
                mJsonToSend = new JSONObject();
                ((Depositos)getActivity()).setData(mJsonToSend);
            }
        }
    }

    public View.OnClickListener onClickNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                if(mJsonToSend.getString("type") != null && mJsonToSend.getString("account_id") != null && mJsonToSend.getString("sales_point") != null && mJsonToSend.getString("sales_point_name") != null && mJsonToSend.getString("bank_id") != null) {
                    ((Depositos)getActivity()).setData(mJsonToSend);
                    ((Depositos)getActivity()).getViewPager().setCurrentItem(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                DialogAlert dialogAlert = DialogAlert.newInstance("Selecciona todos los campos antes de continuar", "Error de formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(), "alert_form");
            }
        }
    };

}
