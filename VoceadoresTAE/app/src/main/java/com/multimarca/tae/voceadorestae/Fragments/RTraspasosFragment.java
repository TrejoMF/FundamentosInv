package com.multimarca.tae.voceadorestae.Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogDate;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Objects.Traspaso;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.RTraspasos;
import com.multimarca.tae.voceadorestae.utils.Functions;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RTraspasosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RTraspasosFragment extends Fragment {

    private String TypeReport = null;
    private String Parent_ID = null;

    List<JSONObject> clientsObjects;
    List<String> clientsStrings;

    Spinner clientsSpinner;
    Spinner typeSpinner;
    Button acceptButton;
    EditText editDateI;
    EditText editDateF;


    public RTraspasosFragment() {
        // Required empty public constructor
    }
    public static RTraspasosFragment newInstance() {
        RTraspasosFragment fragment = new RTraspasosFragment();
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
        View view = inflater.inflate(R.layout.fragment_rtraspasos, container, false);

        acceptButton    = (Button)view.findViewById(R.id.rtraspasos_accept);
        clientsSpinner  = (Spinner)view.findViewById(R.id.rtraspasos_client);
        typeSpinner     = (Spinner)view.findViewById(R.id.rtraspasos_tipo);
        editDateF       = (EditText)view.findViewById(R.id.rtraspasos_dateF_edit);
        editDateI       = (EditText)view.findViewById(R.id.rtraspasos_dateI_edit);


        editDateI.setInputType(EditorInfo.TYPE_NULL);
        editDateF.setInputType(EditorInfo.TYPE_NULL);

        editDateI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePicker(editDateI);
                }
            }
        });
        editDateF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePicker(editDateF);
                }
            }
        });

        acceptButton.setOnClickListener(onClickAccept);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                TypeReport = (parent.getItemAtPosition(position)!=null)?parent.getItemAtPosition(position).toString():"";

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        JSONObject jsonToSend = new JSONObject();
        if(!(Global.TIPOP(getContext()).matches("VTA"))) {
            try {
                jsonToSend.put("ws", "Local_9091");
                jsonToSend.put("request", Global.PAPI(getContext()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final ProgressFragment progressFragment = ProgressFragment.newInstance("Cargando Datos", "Espere un momento");

            new RestApiClient(Global.URL, "clientsCbox", jsonToSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                @Override
                public void onFinish(String Result) {
                    progressFragment.dismiss(getActivity().getSupportFragmentManager());
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Result);

                        String jsonResponseString = jsonObject.getString("response");
                        JSONArray jsonResponse = new JSONArray(jsonResponseString);
                        clientsStrings = new ArrayList<>();
                        clientsObjects = new ArrayList<>();

                        for (int i = 0; i < jsonResponse.length(); i++) {
                            JSONObject Client = jsonResponse.getJSONObject(i);

                            clientsObjects.add(Client);
                            clientsStrings.add(Client.getString("Nombre"));

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, clientsStrings
                        );
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        clientsSpinner.setAdapter(adapter);

                        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    Parent_ID = clientsObjects.get(position).getString("Id_Punto_Venta");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onBefore() {
                    progressFragment.show(getActivity().getSupportFragmentManager(), "progress_data");

                }
            }).execute();
        }else{
            clientsSpinner.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                clientsSpinner.setAlpha(0.2f);
            }

            Parent_ID = Global.USUARIO(getContext());
        }
        return view;

    }
    public void  showDatePicker(final EditText editText) {
        DialogFragment dateFragment = new DialogDate(new DialogDate.ListenDataSet() {
            @Override
            public void callback(View view, int year, int month, int day) {
                String dateFormat =  String.valueOf(day) + "/" + String.valueOf(month+1) +"/"+ String.valueOf(year);
                editText.setText(dateFormat);
                editText.clearFocus();
            }

            @Override
            public void cancel() {
                editText.clearFocus();

            }
        });
        dateFragment.show(getActivity().getSupportFragmentManager(), "datepicker");
    }
    public View.OnClickListener onClickAccept = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressFragment progressFragment  = ProgressFragment.newInstance("Cargando Transpasos", "Espere un momento!");

            JSONObject jsonObjectParams = new JSONObject();
            JSONObject jsonObjectTosend = new JSONObject();
            JSONArray jsonArrayReady    = new JSONArray();

            String dateI = editDateI.getText().toString();
            String dateF = editDateF.getText().toString();

            if(Functions.daysBetweenDates(dateI, dateF)>15) {
                DialogAlert dialogAlert = DialogAlert.newInstance("El periodo de fecha no debe de ser mayor a 15 dias","Error en formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(), "error_dates");
                return;
            }
            if(TypeReport == null || dateF.length() < 3 || dateI.length() < 3) {
                DialogAlert dialogAlert = DialogAlert.newInstance("Llena todos los campos para poder continuar","Error en formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(), "error_dates");
                return;
            }

            try {
                jsonObjectParams.put("Start_Date", dateI + " 00:00:00");
                jsonObjectParams.put("User", Parent_ID);
                jsonObjectParams.put("Parent_ID", Global.USUARIO(getContext()));
                jsonObjectParams.put("Type_Report", TypeReport);
                jsonObjectParams.put("End_Date", dateF + " 23:59:59");
                jsonObjectParams.put("Type_Balance", "TAE");

                jsonArrayReady.put(jsonObjectParams);
                jsonObjectTosend.put("ws",Global.WS_D);
                jsonObjectTosend.put("request","["+jsonObjectParams.toString()+"]");


            } catch (JSONException e) {
                e.printStackTrace();
            }


            new RestApiClient(Global.URL, "rtransfers", jsonObjectTosend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                @Override
                public void onFinish(String Result) {
                    progressFragment.dismiss(getActivity().getSupportFragmentManager());
                    Log.d("RTRASPASOS", Result);
                    JSONObject response;
                    JSONArray jsonArray;
                    try {

                        response = new JSONObject(Result);
                        String message = response.getString("response");
                        jsonArray = new JSONArray(message);

                        int lenght = jsonArray.length();
                        if(lenght < 1 ) {
                            DialogAlert dialogAlert = DialogAlert.newInstance("No se encontraron traspasos para este periodo de fechas", "Sin resultados", R.drawable.ic_info_blue_700_36dp);
                            dialogAlert.show(getActivity().getSupportFragmentManager(), "no_result");
                        }
                        ArrayList<Traspaso> traspasoArrayList = new ArrayList<>();
                        for(int i = 0; i<lenght; i++) {
                            String traspasoString = jsonArray.getString(i);
                            JSONObject traspasoObject = new JSONObject(traspasoString);

                            String Fecha = Functions.CleanDateString(traspasoObject.getString("Fecha"));
                            String Cliente = traspasoObject.getString("Cliente");
                            String Referencia = traspasoObject.getString("Referencia");
                            String Autorizacion = traspasoObject.getString("Autorizacion");
                            String Banco = traspasoObject.getString("Banco");
                            String Descriptcion = traspasoObject.getString("Descripcion");
                            String Supervisor = traspasoObject.getString("Supervisor");
                            String Valor = Functions.FormatMoney(traspasoObject.getString("Valor"));

                            Traspaso traspaso = new Traspaso(i,Fecha,Cliente,Valor,Banco,Referencia,Autorizacion,Descriptcion,Supervisor);
                            traspasoArrayList.add(traspaso);

                        }
                        ((RTraspasos)getActivity()).setTraspasos(traspasoArrayList);
                        ((RTraspasos) getActivity()).getViewPager().setCurrentItem(1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBefore() {
                    if(!getActivity().isFinishing()) {
                        progressFragment.show(getActivity().getSupportFragmentManager(), "load_transfers");
                    }

                }
            }).execute();

        }
    };
}
