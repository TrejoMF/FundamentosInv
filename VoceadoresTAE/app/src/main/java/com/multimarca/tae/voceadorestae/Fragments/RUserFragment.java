package com.multimarca.tae.voceadorestae.Fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.multimarca.tae.voceadorestae.Fragments.Dialogs.DialogAlert;
import com.multimarca.tae.voceadorestae.NewUser;
import com.multimarca.tae.voceadorestae.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class RUserFragment extends Fragment {

    Button mNextButton;
    EditText mUserEdit;
    EditText mNameEdit;
    EditText mPassEdit;
    EditText mRePassEdit;
    TextInputLayout mPassEditLayout;
    TextInputLayout mRePassEditLayout;
    SwitchCompat mAutoSwitch;
    Boolean mIsChecked = false;

    public static RUserFragment newInstance() {
        RUserFragment fragment = new RUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ruser, container, false);

        mNextButton = (Button)view.findViewById(R.id.ruser_button_next);
        mUserEdit = (EditText)view.findViewById(R.id.ruser_edit_usuario);
        mNameEdit = (EditText)view.findViewById(R.id.ruser_edit_nombre);
        mPassEdit = (EditText)view.findViewById(R.id.ruser_edit_pass);
        mRePassEdit = (EditText)view.findViewById(R.id.ruser_edit_repass);
        mPassEditLayout = (TextInputLayout) view.findViewById(R.id.ruser_ilayout_pass);
        mRePassEditLayout = (TextInputLayout) view.findViewById(R.id.ruser_ilayout_repass);
        mRePassEdit = (EditText)view.findViewById(R.id.ruser_edit_repass);
        mAutoSwitch = (SwitchCompat) view.findViewById(R.id.ruser_switch_auto);

        mNextButton.setOnClickListener(mNextTab);

        mAutoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
                if (isChecked) {
                    mRePassEditLayout.setVisibility(View.GONE);
                    mPassEditLayout.setVisibility(View.GONE);
                }else {
                    mRePassEditLayout.setVisibility(View.VISIBLE);
                    mPassEditLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    public static String randomPass() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = 3;
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(21) + 33);
            randomStringBuilder.append(tempChar);
        }
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(25) + 65);
            randomStringBuilder.append(tempChar);
        }
        for (int i = 0; i < 1; i++){
            tempChar = (char) (generator.nextInt(21) + 33);
            randomStringBuilder.append(tempChar);
        }
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(25) + 97);
            randomStringBuilder.append(tempChar);
        }
        return disorderString(randomStringBuilder, new StringBuilder(), 0);
    }
    public static String disorderString(StringBuilder stringBuilder, StringBuilder newString, int init){
        Random generator = new Random();
        int to = generator.nextInt((stringBuilder.length()-init)+1)+init;
        for(int i=to; i!=init; i--) {
            newString.append(stringBuilder.charAt(i-1));
        }

        return (stringBuilder.length() == to) ? newString.toString() : disorderString(stringBuilder, newString, to);
    }

    View.OnClickListener mNextTab = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewPager viewPager = ((NewUser)getActivity()).getViewPager();
            if(viewPager!=null) {

                String user = mUserEdit.getText().toString();
                String name = mNameEdit.getText().toString();
                String pass = mPassEdit.getText().toString();
                String repass = mRePassEdit.getText().toString();
                JSONObject params = new JSONObject();
                if(user.length()>0 && name.length()>0) {
                    if (mIsChecked || (repass.equals(pass) && repass.length()>0)) {
                        pass = (mIsChecked) ? randomPass() : pass;
                        try {
                            params.put("user", user);
                            params.put("name", name);
                            params.put("pass", pass);
                            params.put("tuser", "PDV");
                            params.put("auto", mIsChecked);
                            params.put("stage", "1");
                            Log.d("TAAAAGGGG", pass);
                            ((NewUser) getActivity()).setmJsonObjectUserFragment(params);
                            viewPager.setCurrentItem(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        DialogAlert dialogAlert = DialogAlert.newInstance("Las contraseñas no coinciden o estan vacias", "Error en Formulario", R.drawable.ic_error_red_900_24dp);
                        dialogAlert.show(getActivity().getSupportFragmentManager(), "error_form");
                    }
                }else{
                    DialogAlert dialogAlert = DialogAlert.newInstance("Todos los campos son obligatorios", "Error en Formulario", R.drawable.ic_error_red_900_24dp);
                    dialogAlert.show(getActivity().getSupportFragmentManager(), "error_form");
                }
            }
        }
    };


}
