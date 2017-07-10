package com.multimarca.tae.voceadorestae.Fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.multimarca.tae.voceadorestae.NewUser;
import com.multimarca.tae.voceadorestae.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RUserResumeFragment extends Fragment {

    JSONObject mJsonObject;
    TextView passText;
    TextView userText;
    Button buttonFuga;
    public RUserResumeFragment() {
        // Required empty public constructor
    }

    public static RUserResumeFragment newInstance() {
        RUserResumeFragment fragment = new RUserResumeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ruser_resume, container, false);
        passText = (TextView)view.findViewById(R.id.rresume_pass_value);
        userText = (TextView)view.findViewById(R.id.rresume_user_value);
        buttonFuga = (Button)view.findViewById(R.id.buttonFuga);
        buttonFuga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        passText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    clipboard.setText(passText.getText());
                    Toast.makeText(getContext(), "Contraseña copiada", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            JSONObject jsonObject = ((NewUser)getActivity()).getmJsonObjectUserFragment();
            if(jsonObject != null) {
                mJsonObject = jsonObject;
                    try {
                        passText.setText(mJsonObject.getString("pass"));
                        userText.setText(mJsonObject.getString("user"));
                        mJsonObject.put("stage","3");
                        ((NewUser)getActivity()).setmJsonObjectUserFragment(mJsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }

    }
}
