package com.multimarca.tae.voceadorestae.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.Objects.Carrier;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.Tae;
import com.multimarca.tae.voceadorestae.databases.DbManager;
import com.multimarca.tae.voceadorestae.utils.Functions;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.utils.Validators;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaeFragment extends Fragment {

    List<String> prices = new ArrayList<>();
    List<String> observations = new ArrayList<>();

    private String CarrierID;
    private String CarrierName;
    private String Amount;
    private String PriceObservation = "";
    private String Number;
    private Boolean error = true;
    private int length = 10;

    private Boolean imReady = false;
    EditText editPhone;
    EditText editRePhone;
    TextInputLayout phoneTextInputLayout;
    TextInputLayout rephoneTextInputLayout;
    TextView numberTextView;
    AppBarLayout appBarLayout;

    TextWatcher textWatcherPhone;
    TextWatcher textWatcherRePhone;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public static TaeFragment newInstance() {
        TaeFragment fragment = new TaeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view =  inflater.inflate(R.layout.fragment_tae, container, false);
        final Spinner spinnerAmount = (Spinner) view.findViewById(R.id.tae_monto);
        appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.action_bar_big);
        phoneTextInputLayout = (TextInputLayout) view.findViewById(R.id.tae_layout_number);
        rephoneTextInputLayout = (TextInputLayout) view.findViewById(R.id.tae_layout_re_number);

        final TextView companyTextView = (TextView) getActivity().findViewById(R.id.big_header_companyText);
        final TextView amountTextView = (TextView) getActivity().findViewById(R.id.big_header_amount);
        numberTextView = (TextView) getActivity().findViewById(R.id.big_header_numberText);
        imReady = true;

        editPhone = phoneTextInputLayout.getEditText();
        editRePhone = rephoneTextInputLayout.getEditText();


        Spinner spinnerCarrier = (Spinner) view.findViewById(R.id.tae_company);
        Button submitButton = (Button)view.findViewById(R.id.tae_send);
        Button cleanButton = (Button)view.findViewById(R.id.tae_clean);

        submitButton.setOnClickListener(payClick);
        cleanButton.setOnClickListener(cleanData);

        DbManager dbManager = DbManager.getInstance(getActivity().getApplicationContext());
        final List<Carrier> carriers = dbManager.getAllCarriers(Global.Types.TAE);

        int size = carriers.size();
        final List<String> carStrings = new ArrayList<>();

        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    appBarLayout.setExpanded(false, true);
                } else {
                    appBarLayout.setExpanded(true, true);

                }
            }
        });

        editRePhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    appBarLayout.setExpanded(false, true);
                } else {
                    appBarLayout.setExpanded(true, true);

                }
            }
        });
        if(size>0) {
            for (int i = 0; i < size; i++) {
                String name = carriers.get(i).getName();
                carStrings.add(name);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, carStrings
            );
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerCarrier.setAdapter(adapter);



            spinnerCarrier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(carriers != null && carriers.size()>0) {
                        Carrier carrier = carriers.get(position);
                        prices = carrier.getPricesList();
                        observations = carrier.getObservationPrices();
                        Log.d("TAGGGGGGG", observations.toString());
                        CarrierID = carrier.getCode();
                        CarrierName = carrier.getName();

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getActivity().getApplicationContext(), R.layout.spinner_dropdown_item, prices
                        );
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinnerAmount.setAdapter(adapter);
                        String CarrierNameString = getString(R.string.data_Compañia_hint) + CarrierName;
                        companyTextView.setText(CarrierNameString);
                        editPhone.setText("");
                        editRePhone.setText("");
                        switch (CarrierID) {
                            case "26":
                                length = 11;
                                editPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                                editRePhone.setInputType(InputType.TYPE_CLASS_PHONE);
                                editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                editRePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                textWatcherPhone = new Watcher(phoneTextInputLayout, "Número no valido", 26);
                                textWatcherRePhone = new WatcherRePhone(rephoneTextInputLayout, "Número no valido", 26);
                                editPhone.addTextChangedListener(textWatcherPhone);
                                editRePhone.addTextChangedListener(textWatcherRePhone);
                                break;
                            case "30":
                                length = 13;
                                editPhone.setInputType(InputType.TYPE_CLASS_TEXT);
                                editRePhone.setInputType(InputType.TYPE_CLASS_TEXT);
                                editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                editRePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                editPhone.addTextChangedListener(new Watcher(phoneTextInputLayout, "Número no valido", 30));
                                editRePhone.addTextChangedListener(new WatcherRePhone(rephoneTextInputLayout, "Número no valido", 30));
                                break;
                            default:
                                length = 10;
                                editPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                                editRePhone.setInputType(InputType.TYPE_CLASS_PHONE);
                                editPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                editRePhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                                editPhone.addTextChangedListener(new Watcher(phoneTextInputLayout, "Número no valido", length));
                                editRePhone.addTextChangedListener(new WatcherRePhone(rephoneTextInputLayout, "Número no valido", length));
                                break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(prices != null && prices.size()>0) {
                        Amount = String.valueOf(prices.get(position));
                        Log.d("ITEM-SELECTED", observations.toString());
                        if (observations.size() == prices.size()) {
                            PriceObservation = observations.get(position);
                            amountTextView.setText(PriceObservation);
                        } else {
                            Amount = String.valueOf(prices.get(position));
                            String AmountString = getString(R.string.data_amount_hint) + Amount;
                            amountTextView.setText(AmountString);
                        }
                        clearFocuses();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema al recuperar los carriers, Intenta de manera manual en el menu de la esquina superior derecha. Si el problema continua revisa si tu usuario tiene los permisos necesarios.","Problema al recuperar carriers",R.drawable.ic_error_red_900_24dp);
            dialogAlert.show(getActivity().getSupportFragmentManager(), "dialog_data_carriers");
            Global.EdVACTUALIZAR(getContext(),true);
        }
        return view;
    }

    class Watcher implements TextWatcher {

        TextInputLayout __textInputLayout;
        String __mensaje;
        int __length;
        public Watcher(TextInputLayout textInputLayout, String error, int length) {

            __textInputLayout = textInputLayout;
            __mensaje = error;
            __length = length;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(!Validators.isValidPhone(s.toString(), __length)) {
                error = Validators.showError(this.__textInputLayout, this.__mensaje);
            }else{
                Validators.hideError(__textInputLayout);
                error = false;
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            if(!Validators.isValidPhone(s.toString(),__length)) {
                error = Validators.showError(this.__textInputLayout, this.__mensaje);
            }else{
                Validators.hideError(__textInputLayout);
                if(s.toString().equals(editRePhone.getText().toString())) {
                    error = Validators.hideError(rephoneTextInputLayout);
                    Number = s.toString();
                    String numberText = getString(R.string.data_number_hint) + Number;
                    numberTextView.setText(numberText);

                }else
                    error = Validators.showError(rephoneTextInputLayout,"Los numeros no coinciden");
            }
        }
    }

    class WatcherRePhone implements TextWatcher {

        TextInputLayout __textInputLayout;
        String __mensaje;
        int __length;
        public WatcherRePhone(TextInputLayout textInputLayout, String error, int length) {
            __textInputLayout = textInputLayout;
            __mensaje = error;
            __length  = length;

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!Validators.isValidPhone(s.toString(), __length)) {
                if(!s.equals(editPhone.getText().toString()))
                    error = Validators.showError(this.__textInputLayout, "El telefono no coincide");
                else
                    error = Validators.showError(this.__textInputLayout, this.__mensaje);
            }else{
                if(!s.equals(editPhone.getText().toString()))
                    error = Validators.showError(this.__textInputLayout, this.__mensaje);
                else {
                    error = Validators.hideError(__textInputLayout);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!Validators.isValidPhone(s.toString(),__length)) {
                if(s.toString().equals(editPhone.getText().toString()))
                    error = Validators.showError(this.__textInputLayout, this.__mensaje);
                else {
                    error = Validators.showError(this.__textInputLayout, "El telefono no coincide");
                }
            }else{
                if(s.toString().equals(editPhone.getText().toString())) {
                    error = Validators.hideError(__textInputLayout);
                    Number = s.toString();
                    String numberText = getString(R.string.data_number_hint) + Number;
                    numberTextView.setText(numberText);

                }else {
                    error = Validators.showError(this.__textInputLayout, "El telefono no coincide");

                }
            }
        }
    }

    View.OnClickListener payClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            JSONObject data = new JSONObject();
            JSONObject dataReady = new JSONObject();
            clearFocuses();
            if (!error) {
                try {

                    CarrierID = (CarrierID.length() < 2) ? "0" + CarrierID : CarrierID;
                    DbManager dbManager = DbManager.getInstance(getContext());
                    long _id = dbManager.insertTransaction(CarrierID, CarrierName, Number, Amount, String.valueOf(Global.Types.TAE));
                    Log.d("TAG", String.valueOf(_id));

                    data.put("Number", Number);
                    data.put("Price", Amount);
                    data.put("Carrier", CarrierID);
                    data.put("Carrier_Name", "");
                    data.put("Folio_POS", Functions.FOLIO(_id));
                    data.put("IP", "127.0.0.1");
                    data.put("User", Global.USUARIO(getActivity().getApplicationContext()));
                    data.put("Password", Global.PASS(getActivity().getApplicationContext()));

                    dataReady.put("request", data.toString());
                    dataReady.put("ws", Global.WS);
                    final ProgressFragment progressFragment = ProgressFragment.newInstance("Realizando recarga", "Espere por favor");
                    new RestApiClient(
                            Global.URL,
                            "request",
                            dataReady,
                            RestApiClient.METHOD.POST,
                            new RestApiClient.RestInterface() {
                                @Override
                                public void onFinish(String Result) {
                                    appBarLayout.setExpanded(true, true);
                                    progressFragment.dismiss(getFragmentManager());
                                    if (Result != null) {
                                        JSONObject ObjectMessage;
                                        JSONObject jsonObject;
                                        try {
                                            String response;
                                            jsonObject = new JSONObject(Result);
                                            response = jsonObject.getString("response");

                                            ObjectMessage = new JSONObject(response);
                                            ObjectMessage.put("status",jsonObject.getString("status"));
                                            ObjectMessage.put("number",Number);
                                            ObjectMessage.put("carrier",CarrierName);


                                            editRePhone.setText("");
                                            editPhone.setText("");
                                            ((Tae) getActivity()).setData(ObjectMessage);
                                            ((Tae) getActivity()).getViewPager().setCurrentItem(1);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        DialogAlert dialogAlert = DialogAlert.newInstance("Ocurrio un problema al conectar con el servidor", "Error en conexión", R.drawable.ic_signal_wifi_off_blue_500_18dp);
                                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_conexion");
                                    }
                                }

                                @Override
                                public void onBefore() {

                                    progressFragment.show(getFragmentManager(), "progress");

                                }
                            }
                    ).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                DialogAlert dialogAlert = DialogAlert.newInstance("Debes de introducir un numero valido", "Error en Formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(),"error_formulario");
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && imReady){
            numberTextView.setText("Numero: ");
            error = true;
        }
    }

    View.OnClickListener cleanData = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            editPhone.setText("");
            editRePhone.setText("");
            numberTextView.setText("Numero: ");
            error = true;
        }
    };

    public void clearFocuses() {
        editRePhone.clearFocus();
        editPhone.clearFocus();
        View views = getActivity().getCurrentFocus();
        if (views != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(views.getWindowToken(), 0);
        }
    }

}
