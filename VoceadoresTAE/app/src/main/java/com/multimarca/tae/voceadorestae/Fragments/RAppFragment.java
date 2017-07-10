package com.multimarca.tae.voceadorestae.Fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.NewUser;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RAppFragment extends Fragment {

    JSONObject jsonObject;
    JSONObject mJsonObject;
    JSONArray jsonArrayDevices;
    Spinner mAppSpinner;
    TextInputLayout mText1Layout;
    TextInputLayout mText2Layout;
    TextInputLayout mText3Layout;
    String Device_ID;
    Button mButtonAdd;
    Button mButtonSkip;
    public RAppFragment() {
    }

    public static RAppFragment newInstance() {
        RAppFragment fragment = new RAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view    =  inflater.inflate(R.layout.fragment_rapp, container, false);
        mAppSpinner  = (Spinner) view.findViewById(R.id.rcomision_sppiner_apps);
        mText1Layout = (TextInputLayout) view.findViewById(R.id.rapp_ilayout_text1);
        mText2Layout = (TextInputLayout) view.findViewById(R.id.rapp_ilayout_text2);
        mText3Layout = (TextInputLayout) view.findViewById(R.id.rapp_ilayout_text3);
        mButtonAdd   = (Button) view.findViewById(R.id.rapp_button_add);
        mButtonSkip  = (Button) view.findViewById(R.id.rapp_button_skip);
        mText1Layout.setVisibility(View.GONE);
        mText2Layout.setVisibility(View.GONE);
        mText3Layout.setVisibility(View.GONE);

        mButtonAdd.setOnClickListener(addDevice);
        mButtonSkip.setOnClickListener(skip);
        jsonObject = new JSONObject();
        try {
            jsonObject.put("ws", Global.WS);
            jsonObject.put("request", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando datos", "Espere por favor");

        new RestApiClient(Global.URL, "get_platform", jsonObject, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                progressFragment.dismiss(getFragmentManager());
                try {
                    if(Result != null) {
                        JSONObject jsonObject = new JSONObject(Result);
                        String stringDevices = jsonObject.getString("response");
                        JSONArray jsonArrayDevicesLocal = new JSONArray(stringDevices);
                        jsonArrayDevices = new JSONArray();
                        ArrayList<String> apps = new ArrayList<String>();
                        apps.add("Aplicativos");
                        for (int i = 0; i < jsonArrayDevicesLocal.length(); i++) {

                            JSONObject jsonApp = jsonArrayDevicesLocal.getJSONObject(i);
                            if(jsonApp.has("Status") && jsonApp.getBoolean("Status")) {
                                apps.add(jsonApp.getString("Name"));
                                jsonArrayDevices.put(jsonApp);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, apps
                        );
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                        mAppSpinner.setAdapter(adapter);

                        mAppSpinner.setOnItemSelectedListener(itemSelectedListener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore() {
                progressFragment.show(getFragmentManager(), "progress");

            }
        }).execute();
        return view;
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(position != 0) {
                switch (position) {
                    case 1:
                        Log.d("TAGG","Windows");
                        mText1Layout.setHint("MAC ADDRESS");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        Device_ID = "1";
                        break;
                    case 2:
                        Log.d("TAGG","JAVA");
                        mText1Layout.setHint("IMEI");
                        mText2Layout.setHint("NIP");
                        mText3Layout.setHint("NÚMERO");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.VISIBLE);
                        mText3Layout.setVisibility(View.VISIBLE);
                        Device_ID = "2";
                        break;
                    case 3:
                        Log.d("TAGG","WEB");
                        mText1Layout.setVisibility(View.GONE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        Device_ID = "3";
                        break;
                    case 4:
                        Log.d("TAGG","MOVIL SMS");
                        mText1Layout.setHint("NÚMERO");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        Device_ID = "5";
                        break;
                    case 5:
                        Log.d("TAGG","ANDROID");
                        mText1Layout.setHint("ANDROID ID/MAC ADDRESS");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        Device_ID = "6";
                        break;
                    case 6:
                        Log.d("TAGG","WHATSAPP");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        mText1Layout.setHint("NUMERO WHATSAPP");
                        Device_ID = "10";
                        break;
                    case 7:
                        Log.d("TAGG","SMS 2245");
                        mText1Layout.setHint("NÚMERO");
                        mText1Layout.setVisibility(View.VISIBLE);
                        mText2Layout.setVisibility(View.GONE);
                        mText3Layout.setVisibility(View.GONE);
                        Device_ID = "11";
                        break;
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
/*
Json: {"User":"6561827062","Device_ID":"5","Device_Address":"678905555","Nip":"","Status":"true","User_Modified":"6144135400",
"Phone":"6561996992"}

 */

    View.OnClickListener addDevice = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                String User = mJsonObject.getString("user");
                String User_Modified = Global.USUARIO(getContext());
                String Device_Address = mText1Layout.getEditText().getText().toString();
                String Nip = mText2Layout.getEditText().getText().toString();
                String Status = "true";
                String Phone = "";

                if(Device_Address.length()>0){
                    JSONObject jsonToSend = new JSONObject();
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("User",User);
                    jsonRequest.put("User_Modified",User_Modified);
                    jsonRequest.put("Device_ID",Device_ID);
                    jsonRequest.put("Device_Address",Device_Address);
                    jsonRequest.put("Nip",Nip);
                    jsonRequest.put("Status",Status);
                    jsonRequest.put("Phone",Phone);

                    jsonToSend.put("request", jsonRequest);
                    jsonToSend.put("ws", Global.WS);
                    final ProgressFragment progressFragment = ProgressFragment.newInstance("Agregando usuario", "Espere por favor");

                    new RestApiClient(Global.URL, "add_device", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                        @Override
                        public void onFinish(String Result) {
                            progressFragment.dismiss(getFragmentManager());

                            if(Result != null) {
                                JSONObject jsonResult = null;
                                try {
                                    jsonResult = new JSONObject(Result);
                                    String response = jsonResult.getString("response");
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String responseWS = jsonResponse.getString("Response");
                                    Log.d("TAASSSS",Result);

                                    switch (responseWS) {
                                        case "1": {
                                            DialogAlert dialogAlert;
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                                                dialogAlert = DialogAlert.newInstance("La aplicación se registro con exito. Copia tu CONTRASEÑA haciendo click en el texto.", "Aplicación agregada", R.drawable.ic_info_blue_700_36dp);
                                            } else{
                                                dialogAlert = DialogAlert.newInstance("La aplicación se registro con exito. Recuerda anotar tu contraseña", "Aplicación agregada", R.drawable.ic_info_blue_700_36dp);
                                            }
                                            dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                            ViewPager viewPager = ((NewUser) getActivity()).getViewPager();
                                            viewPager.setCurrentItem(4);
                                            mJsonObject.put("stage","3");
                                            ((NewUser)getActivity()).setmJsonObjectUserFragment(mJsonObject);
                                            break;
                                        }
                                        case "2": {
                                            DialogAlert dialogAlert = DialogAlert.newInstance("El identificador no puede ir en blanco", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                            dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                            break;
                                        }
                                        case "3": {
                                            DialogAlert dialogAlert = DialogAlert.newInstance("Identificador ya registrado", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                            dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                            break;
                                        }
                                        case "4": {
                                            DialogAlert dialogAlert = DialogAlert.newInstance("El Usuario no existe", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                            dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                            break;
                                        }
                                        default: {
                                            DialogAlert dialogAlert = DialogAlert.newInstance("Ocurrio un problema", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                            dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                            break;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    DialogAlert dialogAlert = DialogAlert.newInstance("Error desconocido", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                }
                            }else{
                                DialogAlert dialogAlert = DialogAlert.newInstance("Ocurrio un problema", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                            }

                        }
                        @Override
                        public void onBefore() {
                            progressFragment.show(getFragmentManager(), "progress");

                        }
                    }).execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener skip = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ViewPager viewPager = ((NewUser) getActivity()).getViewPager();
            viewPager.setCurrentItem(4);
            try {
                mJsonObject.put("stage","3");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((NewUser)getActivity()).setmJsonObjectUserFragment(mJsonObject);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            JSONObject jsonObject = ((NewUser)getActivity()).getmJsonObjectUserFragment();
            if(jsonObject != null) {
                mJsonObject = jsonObject;
            }
        }

    }
}
