package com.multimarca.tae.voceadorestae.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.NewUser;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.utils.InputFilterMinMax;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RComisionesFragment extends Fragment {

    EditText mIaveComisionEdit;
    EditText mTeleComisionEdit;
    EditText mServComisionEdit;
    EditText mGralComisionEdit;
    TextView mIaveComisionText;
    TextView mTeleComisionText;
    TextView mServComisionText;
    TextView mGralComisionText;
    Button mAddButton;

    String IAVEParentCom;
    String TeleParentCom;
    String ServParentCom;
    String ComiString;

    JSONArray jsonArrayDevices;
    JSONObject mJsonObject;


    public static RComisionesFragment newInstance() {
        RComisionesFragment fragment = new RComisionesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_rcomisiones, container, false);

        JSONObject jsonObject = new JSONObject();

        mIaveComisionText = (TextView)view.findViewById(R.id.rcomision_ciave_parent);
        mTeleComisionText = (TextView)view.findViewById(R.id.rcomision_ctelevia_parent);
        mServComisionText = (TextView)view.findViewById(R.id.rcomision_cservicios_parent);
        mGralComisionText = (TextView)view.findViewById(R.id.rcomision_comision_parent);
        mIaveComisionEdit = (EditText)view.findViewById(R.id.rcomision_ciave_user);
        mTeleComisionEdit = (EditText)view.findViewById(R.id.rcomision_ctelevia_user);
        mServComisionEdit = (EditText)view.findViewById(R.id.rcomision_cservicios_user);
        mGralComisionEdit = (EditText)view.findViewById(R.id.rcomision_comision_user);
        mAddButton = (Button)view.findViewById(R.id.rcomision_button_add);


        try {
            jsonObject.put("ws", Global.WS);
            jsonObject.put("request", Global.USUARIO(getContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new RestApiClient(Global.URL, "get_commissions", jsonObject, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                try {
                    if(Result != null) {
                        JSONObject result = new JSONObject(Result);

                        String responseString = result.getString("response");
                        JSONObject jsonResponse = new JSONObject(responseString);
                        String products_commissions = jsonResponse.getString("products_and_commissions_pos_template");
                        JSONArray jsonArrayCommission = new JSONArray(products_commissions);

                        JSONObject jsonObjectIAVE = jsonArrayCommission.getJSONObject(0);
                        IAVEParentCom = jsonObjectIAVE.getString("fatherCommission");

                        mIaveComisionText.setText(IAVEParentCom);

                        JSONObject jsonObjectTELE = jsonArrayCommission.getJSONObject(1);
                        TeleParentCom = jsonObjectTELE.getString("fatherCommission");

                        mTeleComisionText.setText(TeleParentCom);

                        JSONObject jsonObjectServ = jsonArrayCommission.getJSONObject(2);
                        ServParentCom = jsonObjectServ.getString("fatherCommission");

                        mServComisionText.setText(ServParentCom);

                        String special_parameters_templateString = jsonResponse.getString("special_parameters_template");
                        JSONArray jsonArrayParameters = new JSONArray(special_parameters_templateString);

                        JSONObject jsonObjectComi = jsonArrayParameters.getJSONObject(0);
                        ComiString = String.valueOf(round(Double.parseDouble(jsonObjectComi.getString("fatherCommission")), 3));

                        mGralComisionText.setText(ComiString);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore() {

            }
        }).execute();

        mAddButton.setOnClickListener(addUser);

        return view;
    }


    View.OnClickListener addUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            JSONObject paramUserObject = new JSONObject();
            JSONObject jsonToSend = new JSONObject();

            if(mJsonObject != null) {
                try {
                    String user = mJsonObject.getString("user");
                    String name = mJsonObject.getString("name");
                    String pass = mJsonObject.getString("pass");
                    String tuser = mJsonObject.getString("tuser");

                    if(Global.TIPOP(getContext()).equals("PDV")) {
                        paramUserObject.put("parent_id", Global.USUARIO(getContext()));
                        paramUserObject.put("user_modified", Global.USUARIO(getContext()));

                    }else{
                        paramUserObject.put("parent_id", Global.PAPI(getContext()));
                        paramUserObject.put("user_modified", Global.USUARIO(getContext()));
                    }

                    paramUserObject.put("user", user);
                    paramUserObject.put("password", pass);
                    paramUserObject.put("name", name);
                    paramUserObject.put("phone", "");
                    paramUserObject.put("email", "");
                    paramUserObject.put("client_profile", tuser);

                    JSONArray commissionsTransactions = new JSONArray();


                    JSONObject commission = new JSONObject();

                    /*IAVE COMISION*/
                    double comission_user = Double.parseDouble(mIaveComisionEdit.getText().toString());
                    double comission_parent = Double.parseDouble(mIaveComisionText.getText().toString());

                    JSONObject commission26 = new JSONObject();
                    if(comission_parent>=comission_user) {
                        commission26.put("carrier", "26");
                        commission26.put("commission", String.valueOf(comission_user));
                        commission26.put("status", "true");
                        commission26.put("statusFlag", "false");
                    }else {
                        DialogAlert dialogAlert = DialogAlert.newInstance("La Comision del PDV debe de ser menor a tu comisión", "Error de comisión IAVE", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_comis");
                        return;
                    }
                    commissionsTransactions.put(commission26);

                    /*TELEVIA COMISION*/
                    comission_user = Double.parseDouble(mTeleComisionEdit.getText().toString());
                    comission_parent = Double.parseDouble(mTeleComisionText.getText().toString());

                    JSONObject commission30 = new JSONObject();
                    if(comission_parent>=comission_user) {
                        commission30.put("carrier", "30");
                        commission30.put("commission", String.valueOf(comission_user));
                        commission30.put("status", "true");
                        commission30.put("statusFlag", "false");
                    }else {
                        DialogAlert dialogAlert = DialogAlert.newInstance("La Comision del PDV debe de ser menor a tu comisión.", "Error de comisión de Televia", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_comis");
                        return;
                    }
                    commissionsTransactions.put(commission30);

                    /*SERVICIOS COMISION*/
                    comission_user = Double.parseDouble(mServComisionEdit.getText().toString());
                    comission_parent = Double.parseDouble(mServComisionText.getText().toString());

                    JSONObject commission99 = new JSONObject();
                    if(comission_parent<=comission_user) {
                        commission99.put("carrier", "99");
                        commission99.put("commission", String.valueOf(comission_user));
                        commission99.put("status", "true");
                        commission99.put("statusFlag", "false");
                    }else {
                        DialogAlert dialogAlert = DialogAlert.newInstance("La Comision del PDV debe de ser mayor a tu comisión", "Error de comisión de servicios", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_comis");
                        return;
                    }
                    commissionsTransactions.put(commission99);
                    paramUserObject.put("commissionsTransactions", commissionsTransactions);

                    JSONArray specialParameters = new JSONArray();

                    /*purchaseCommission*/
                    comission_user = Double.parseDouble(mGralComisionEdit.getText().toString());
                    comission_parent = Double.parseDouble(mGralComisionText.getText().toString());

                    JSONObject parametes = new JSONObject();
                    if(comission_parent>=comission_user) {
                        parametes.put("name", "purchaseCommission");
                        parametes.put("commission", String.valueOf(comission_user));
                    }else{
                        DialogAlert dialogAlert = DialogAlert.newInstance("La Comision del PDV debe de ser menor a tu comisión", "Error de comisión", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_comis");
                        return;
                    }
                    specialParameters.put(parametes);
                    paramUserObject.put("specialParameters", specialParameters);
                    jsonToSend = new JSONObject();
                    jsonToSend.put("ws",Global.WS);
                    jsonToSend.put("request", paramUserObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", e.toString());
                }
                final ProgressFragment progressFragment = ProgressFragment.newInstance("Agregando usuario", "Espere por favor");

                new RestApiClient(Global.URL, "save_user", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                    @Override
                    public void onFinish(String Result) {
                        progressFragment.dismiss(getFragmentManager());

                        if(Result!=null) {
                            try {
                                JSONObject jsonResult = new JSONObject(Result);
                                String response = jsonResult.getString("response");
                                JSONObject jsonResponse = new JSONObject(response);
                                String responseWS = jsonResponse.getString("Response");

                                if (responseWS.equals("1")) {
                                    DialogAlert dialogAlert = DialogAlert.newInstance("El PDV se registro correctamente", "PDV agregado", R.drawable.ic_info_blue_700_36dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "chido_dialog");
                                    ViewPager viewPager = ((NewUser)getActivity()).getViewPager();
                                    mJsonObject.put("stage","1");
                                    ((NewUser)getActivity()).setmJsonObjectUserFragment(mJsonObject);
                                    viewPager.setCurrentItem(2);
                                }else if(responseWS.equals("0")) {
                                    DialogAlert dialogAlert = DialogAlert.newInstance("El PDV ya se habia registrado", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "error_dialog");
                                }else if(responseWS.equals("-2")){
                                    DialogAlert dialogAlert = DialogAlert.newInstance("Hay un problema con la información que nos mandaste", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "error_dialog");
                                }else{
                                    DialogAlert dialogAlert = DialogAlert.newInstance("No podemos entender la respuesta del servidor", "Error al agregar", R.drawable.ic_error_red_900_24dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "error_dialog");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("TAGGGGGG", Result);
                        }
                    }
                    @Override
                    public void onBefore() {
                        progressFragment.show(getFragmentManager(), "progress");
                    }
                }).execute();
            }
        }
    };

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            double comission_parent_Iave = Double.parseDouble(IAVEParentCom);
            double comission_parent_Tele = Double.parseDouble(TeleParentCom);
            double comission_parent_Serv = Double.parseDouble(ServParentCom);
            double comission_parent_Gral = Double.parseDouble(ComiString);
            mIaveComisionEdit.setFilters(new InputFilter[]{ new InputFilterMinMax(0.0, comission_parent_Iave)});
            mTeleComisionEdit.setFilters(new InputFilter[]{ new InputFilterMinMax(0.0, comission_parent_Tele)});
            mServComisionEdit.setFilters(new InputFilter[]{ new InputFilterMinMax(comission_parent_Serv, 100.00)});
            mGralComisionEdit.setFilters(new InputFilter[]{ new InputFilterMinMax(0.00,comission_parent_Gral)});

            JSONObject jsonObject = ((NewUser)getActivity()).getmJsonObjectUserFragment();
            if(jsonObject != null) {
                mJsonObject = jsonObject;
            }
        }

    }
}
