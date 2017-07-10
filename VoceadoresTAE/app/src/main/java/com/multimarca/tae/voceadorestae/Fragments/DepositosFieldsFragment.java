package com.multimarca.tae.voceadorestae.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.multimarca.tae.voceadorestae.Depositos;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogDate;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogTimePicker;
import com.multimarca.tae.voceadorestae.Fragments.Dialogs.ProgressFragment;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.utils.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepositosFieldsFragment extends Fragment {

    private int PICK_IMAGE_REQUEST = 1;

    EditText fechaEditText;
    EditText horaEditText;
    EditText montoEditText;
    EditText folioEditText;
    EditText authEditText;
    EditText commentEdit;
    TextInputLayout authLayout;
    TextInputLayout folioLayout;
    TextInputLayout hourLayout;
    TextInputLayout dateLayout;
    Button nextButton;
    Button prevButton;
    Button fileButton;
    ImageView imageView;
    Bitmap bitmap;
    String monto = "";
    Uri uri;
    Map<String, TextInputLayout> editFields = new HashMap<>();
    View mView;

    Boolean requiredfile = true;
    Boolean errorOnForm = false;

    public DepositosFieldsFragment() {
        // Required empty public constructor
    }


    public static DepositosFieldsFragment newInstance() {

        Bundle args = new Bundle();
        DepositosFieldsFragment fragment = new DepositosFieldsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_depositos_fields, container, false);
        mView = view;
        fechaEditText = (EditText)view.findViewById(R.id.depositos_fechaField);
        horaEditText  = (EditText)view.findViewById(R.id.depositos_horaField);
        authEditText  = (EditText)view.findViewById(R.id.depositos_authField);
        folioEditText = (EditText)view.findViewById(R.id.depositos_folioField);
        folioEditText = (EditText)view.findViewById(R.id.depositos_folioField);
        nextButton    = (Button)view.findViewById(R.id.depositos_fields_next);
        prevButton    = (Button)view.findViewById(R.id.depositos_fields_prev);
        fileButton    = (Button)view.findViewById(R.id.depostios_fileField);
        authLayout    = (TextInputLayout)view.findViewById(R.id.depositos_authFieldLayout);
        folioLayout   = (TextInputLayout)view.findViewById(R.id.depositos_folioFieldLayout);
        dateLayout    = (TextInputLayout)view.findViewById(R.id.depositos_fechaFieldLayout);
        hourLayout    = (TextInputLayout)view.findViewById(R.id.depositos_horaFieldLayout);
        imageView     = (ImageView)view.findViewById(R.id.pomientras);
        montoEditText = (EditText)view.findViewById(R.id.depositos_montoField);
        commentEdit   = (EditText)view.findViewById(R.id.depositos_comentsField);

        fechaEditText.setInputType(EditorInfo.TYPE_NULL);
        horaEditText.setInputType(EditorInfo.TYPE_NULL);
        prevButton.setOnClickListener(onClickPrev);
        nextButton.setOnClickListener(onClickSend);
        fileButton.setOnClickListener(onClickFile);



        montoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                monto = s.toString();
                if(monto.length()>1) {
                    float montoFloat = Float.parseFloat(monto);
                    requiredfile = montoFloat % 1 <= 0;
                }
            }
        });

        fechaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDateDialog();
                }
            }
        });

        horaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showTimeDialog();
                }
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if(isVisibleToUser) {
                JSONObject object = ((Depositos) getActivity()).getData();
                if(object != null){
                    requiredfile = true;
                    errorOnForm = false;
                    String validationsString = object.getString("VALIDATIONS");
                    JSONArray validsArray  = new JSONArray(validationsString);

                    folioLayout.setVisibility(View.GONE);
                    authLayout.setVisibility(View.GONE);

                    editFields.put("reference1", folioLayout);
                    editFields.put("reference2", authLayout);

                    int validLenght = validsArray.length();
                    for (int i = 0; i<validLenght; i++) {
                        String objectString = validsArray.getString(i);
                        JSONObject objectJSON = new JSONObject(objectString);

                        TextInputLayout fieldToValidate = editFields.get(objectJSON.getString("field"));
                        fieldToValidate.setHint(objectJSON.getString("label_name"));
                        fieldToValidate.setVisibility(View.VISIBLE);
                        Boolean required = objectJSON.getBoolean("requested");
                        EditText editText = fieldToValidate.getEditText();
                        if(editText != null) {
                            TextWatcher textWatcher = null;
                            if (required) {
                                textWatcher = new WatcherRequired(fieldToValidate);
                                editText.addTextChangedListener(textWatcher);
                                if(editText.getText().toString().length()<1) {
                                    fieldToValidate.setErrorEnabled(true);
                                    fieldToValidate.setError("El campo es requerido");
                                }
                            } else {
                                if(textWatcher != null) {
                                    editText.removeTextChangedListener(textWatcher);
                                    fieldToValidate.setErrorEnabled(false);

                                }
                            }
                        }
                    }
                }else{
                    ((Depositos)getActivity()).getViewPager().setCurrentItem(0);
                    DialogAlert dialogAlert = DialogAlert.newInstance("Debes de dar click al boton de siguiente", "Error en formulario", R.drawable.ic_error_red_900_24dp);
                    dialogAlert.show(getActivity().getSupportFragmentManager(), "error_conitnuando");
                }
            }
        } catch (JSONException e) {
            ((Depositos)getActivity()).getViewPager().setCurrentItem(0);
            e.printStackTrace();
        }
    }

    public void showTimeDialog() {
        DialogFragment dialogFragment = new DialogTimePicker(new DialogTimePicker.ListenDataSet() {
            @Override
            public void callback(View view, int Hour, int minute) {
                String HourS = (Hour<10)?"0"+ String.valueOf(Hour): String.valueOf(Hour);
                String MinuteS = (minute<10)?"0"+ String.valueOf(minute): String.valueOf(minute);

                String time = HourS+":"+MinuteS;
                horaEditText.setText(time);
                horaEditText.clearFocus();
            }

            @Override
            public void cancel() {
                horaEditText.clearFocus();
            }
        });
        dialogFragment.show(getActivity().getSupportFragmentManager(),"time_dialog");
    }

    public void showDateDialog() {
        DialogFragment dialogTimePicker = new DialogDate(new DialogDate.ListenDataSet() {
            @Override
            public void callback(View view, int year, int month, int day) {
                month = month+1;
                String dayS = (day<10)?"0"+ String.valueOf(day): String.valueOf(day);
                String montgS = (month<10)?"0"+ String.valueOf(month): String.valueOf(month);

                String date = dayS+"/"+montgS+"/"+year;
                fechaEditText.setText(date);
                fechaEditText.clearFocus();

            }

            @Override
            public void cancel() {
                fechaEditText.clearFocus();
            }
        });
        dialogTimePicker.show(getActivity().getSupportFragmentManager(), "date_dialog");
    }

    public View.OnClickListener onClickSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //todo realizar modificaciones y generar una clase para consumo rest (OPTIMIZAR)
            String fecha = fechaEditText.getText().toString();
            String hora =  horaEditText.getText().toString();
            Log.d("TAGGGG", "AQUI ANDAMOS 1 ");

            String Sales_Point = "";
            String account_id = "";
            String sales_point_name = "";
            String reference = "";
            String email = "";
            String bank_id = "";
            String type = "";
            String reference1  = "";
            String reference2  = "";
            String amount      = "";
            try {
                final JSONObject object = ((Depositos)getActivity()).getData();

                Sales_Point = object.getString("sales_point");
                account_id = object.getString("account_id");
                sales_point_name = object.getString("sales_point_name");
                reference = object.getString("reference");
                email = object.getString("email");
                bank_id = object.getString("bank_id");
                type = object.getString("type");
                if(type.equals("3")) {
                    requiredfile = false;
                }
                reference1 = folioEditText.getText().toString();
                reference2 = authEditText.getText().toString();
                amount = montoEditText.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(fecha.length() < 5) {

                dateLayout.setErrorEnabled(true);
                dateLayout.setError("Selecciona una fecha antes de continuar");
                return;
            }
            if(hora.length() < 5) {
                hourLayout.setErrorEnabled(true);
                hourLayout.setError("Selecciona una hora antes de continuar");
                return;
            }

            if(bitmap == null && requiredfile) {
                DialogAlert dialogAlert = DialogAlert.newInstance("Selecciona un archivo antes de continuar", "Error en formulario", R.drawable.ic_error_red_900_24dp);
                dialogAlert.show(getActivity().getSupportFragmentManager(), "error_form");
                return;
            }

            ((Depositos)getActivity()).getViewPager().setCurrentItem(1);

            final String finalSales_Point = Sales_Point;
            final String finalAccount_id = account_id;
            final String finalReference = reference;
            final String finalType = type;
            final String finalReference1 = reference1;
            final String finalSales_point_name = sales_point_name;
            final String finalBank_id = bank_id;
            final String finalAmount = amount;
            final String finalEmail = email.length()>50 ? "" : email;
            final String finalReference2 = reference2;
            if(errorOnForm)
                return;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ProgressFragment progressFragment = ProgressFragment.newInstance("Reporte de deposito", "Se esta realizando el reporte del deposito, espere  porfavor");
                    if(!getActivity().isFinishing()) {
                        progressFragment.show(getActivity().getSupportFragmentManager(), "load_deposit");
                    }
                    try {
                        MultipartUtility formDeposit = new MultipartUtility(Global.URL_D+ "api/client_deposits/add/", "UTF-8");
                        File f = (bitmap!=null)?persistImage(bitmap, "deposito"):null;
                        formDeposit.addHeaderField("User-Agent", "CodeJava");
                        formDeposit.addHeaderField("Test-Header", "Header-Value");

                        String dateTime = fechaEditText.getText().toString()+" "+horaEditText.getText().toString()+":00";

                        formDeposit.addFormField("sales_point", finalSales_Point);
                        formDeposit.addFormField("comment", commentEdit.getText().toString());
                        formDeposit.addFormField("account_id", finalAccount_id);
                        formDeposit.addFormField("reference", finalReference);
                        formDeposit.addFormField("transference_type", finalType);
                        formDeposit.addFormField("plataform_id", Global.PLATAFORM);
                        formDeposit.addFormField("reference1", finalReference1);
                        formDeposit.addFormField("sales_point_name", finalSales_point_name);
                        formDeposit.addFormField("bank_id", finalBank_id);
                        formDeposit.addFormField("datetime_deposit", dateTime);
                        formDeposit.addFormField("amount", finalAmount);
                        formDeposit.addFormField("email", finalEmail);
                        formDeposit.addFormField("reference2", finalReference2);
                        formDeposit.addFormField("coadsy_user", Global.USUARIO(getContext()));
                        if(f != null)
                            formDeposit.addFilePart("attch_file", f);
                        String response = formDeposit.finish();

                        try {

                            if(response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");

                                Boolean error = jsonObject.getBoolean("error");

                                if(error) {
                                    DialogAlert dialogAlert = DialogAlert.newInstance(message, "Deposito", R.drawable.ic_error_red_900_24dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "confirmacion_dialog");
                                }else {

                                    DialogAlert dialogAlert = DialogAlert.newInstance(message, "Deposito", R.drawable.ic_info_blue_700_36dp);
                                    dialogAlert.show(getActivity().getSupportFragmentManager(), "confirmacion_dialog");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((Depositos) getActivity()).setData(new JSONObject());
                                            ((Depositos) getActivity()).getViewPager().setCurrentItem(0);
                                        }
                                    });


                                }
                            } else{
                                DialogAlert dialogAlert = DialogAlert.newInstance("Hubo un problema en la informacion que mandaste!", "Deposito", R.drawable.ic_error_red_900_24dp);
                                dialogAlert.show(getActivity().getSupportFragmentManager(), "confirmacion_dialog");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressFragment.dismiss(getActivity().getSupportFragmentManager());
                    } catch (IOException e) {
                        progressFragment.dismiss(getActivity().getSupportFragmentManager());
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };
    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public View.OnClickListener onClickPrev = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Depositos)getActivity()).getViewPager().setCurrentItem(0);
        }
    };

    View.OnClickListener onClickFile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            Log.d("TaG", data.getData().toString());
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class WatcherRequired implements TextWatcher {

        TextInputLayout mTextInputLayout;
        public WatcherRequired(TextInputLayout textInputLayout) {
            mTextInputLayout = textInputLayout;
            errorOnForm = (mTextInputLayout.getEditText().getText().length()<1);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length() < 0) {
                errorOnForm = true;
                mTextInputLayout.setErrorEnabled(true);
            }else{
                mTextInputLayout.setErrorEnabled(false);
                errorOnForm = false;
            }
        }
    }
}
