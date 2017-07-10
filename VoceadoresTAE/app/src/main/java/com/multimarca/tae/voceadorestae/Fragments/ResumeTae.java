package com.multimarca.tae.voceadorestae.Fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.Tae;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResumeTae extends Fragment {


    public ResumeTae() {
        // Required empty public constructor
    }

    private static String ARG_COMPANY  = "COMPANY";
    private static String ARG_NUMBER = "NUMBER";
    private static String ARG_FOLIO  = "FOLIO";
    private static String ARG_DATE   = "DATE";
    private static String ARG_STATUS   = "STATUS";
    private static String ARG_NOTICE   = "NOTICE";

    public static ResumeTae newInstance(){
        return new ResumeTae();

    }

    public String Number    = "___-___-____";
    public String Company   = "----";
    public String Folio     = "______";
    public String Date      = "___ __, ____ / __:__";
    public String Status      = "";
    public String Notice      = "";
    public String Version     = "";
    public String mBalance     = "0.0";
    public String Description     = "Recarga Elécronica";

    View view;
    TextView FolioTextView;
    TextView DateTextView;
    TextView NoticeTextView;
    TextView BalanceTextView;
    TextView descTextView;
    AppBarLayout action_bar_big;
    Button gobackButton;

    Boolean prevSaved = false;
    Boolean yaEntre = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume_tae, container, false);

        FolioTextView = (TextView) view.findViewById(R.id.resume_folio_number);
        DateTextView = (TextView) view.findViewById(R.id.resume_date_today);
        NoticeTextView = (TextView) view.findViewById(R.id.notice_tae);
        BalanceTextView = (TextView) view.findViewById(R.id.resume_balance_number);
        gobackButton = (Button) view.findViewById(R.id.resume_goback);
        action_bar_big = (AppBarLayout) getActivity().findViewById(R.id.action_bar_big);
        descTextView = (TextView)getActivity().findViewById(R.id.big_header_titulo);

        gobackButton.setOnClickListener(goBack);



        if(savedInstanceState != null) {
            prevSaved = true;

            Number = savedInstanceState.getString(ARG_NUMBER);
            Company = savedInstanceState.getString(ARG_COMPANY);
            Folio = savedInstanceState.getString(ARG_FOLIO);
            Date = savedInstanceState.getString(ARG_DATE);
            Status = savedInstanceState.getString(ARG_STATUS);
            Notice = savedInstanceState.getString(ARG_NOTICE);
        }
        yaEntre = true;
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser ) {
            try {
                JSONObject object = ((Tae) getActivity()).getData();
                if (object != null) {
                    if (!prevSaved) {
                        Number = object.getString("number");
                        Company = object.getString("carrier");
                        Folio = (object.has("folio")) ? object.getString("folio") : "";
                        Date = (object.has("date")) ? object.getString("date") : "";
                        Status = object.getString("status");
                        Notice = object.getString("notice");
                        Version = object.getString("productVersion");
                        Description = object.getString("description");
                    }
                }
                checkBalance();
                FolioTextView.setText(Folio);
                DateTextView.setText(Date);
                NoticeTextView.setText(Notice);
                descTextView.setText(Description);

                if(Status.equals("00")) {
                    setCorrect();
                }else{
                    setError();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            if(yaEntre)
                returnState();
        }

    }

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Tae)getActivity()).getViewPager().setCurrentItem(0);
        }
    };

    public void setError() {

        action_bar_big.setBackgroundColor(Color.parseColor("#d32f2f"));
        FolioTextView.setTextColor(Color.parseColor("#b71c1c"));
        DateTextView.setTextColor(Color.parseColor("#b71c1c"));
        NoticeTextView.setTextColor(Color.parseColor("#b71c1c"));
        BalanceTextView.setTextColor(Color.parseColor("#b71c1c"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#b71c1c"));
        }
    }
    public void setCorrect() {

        action_bar_big.setBackgroundColor(Color.parseColor("#388e3c"));
        FolioTextView.setTextColor(Color.parseColor("#1b5e20"));
        DateTextView.setTextColor(Color.parseColor("#1b5e20"));
        NoticeTextView.setTextColor(Color.parseColor("#1b5e20"));
        BalanceTextView.setTextColor(Color.parseColor("#1b5e20"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1b5e20"));
        }
    }
    @SuppressWarnings("ResourceType")
    public void returnState() {
        action_bar_big.setBackgroundColor(Color.parseColor(getString(R.color.colorPrimary)));
        TextView textTitle = (TextView)getActivity().findViewById(R.id.big_header_titulo);

        textTitle.setText(R.string.title_tae);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(getString(R.color.colorPrimaryDark)));
        }
    }

    public void checkBalance(){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonSend = new JSONObject();

        try {
            jsonObject.put("User", Global.USUARIO(getContext()));
            jsonObject.put("Password", Global.PASS(getContext()));
            jsonSend.put("ws", Global.WS);
            jsonSend.put("request", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RestApiClient(Global.URL, "balance", jsonSend, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
            @Override
            public void onFinish(String Result) {
                JSONObject jsonObject;
                JSONObject jsonOResponse;
                if (Result != null) {
                    try {
                        jsonObject = new JSONObject(Result);
                        String jsonResponseString = jsonObject.getString("response");
                        jsonOResponse = new JSONObject(jsonResponseString);
                        mBalance = jsonOResponse.getString("balance");
                        BalanceTextView.setText(mBalance);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onBefore() {
            }
        }).execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_NUMBER, Number);
        outState.putString(ARG_FOLIO,Folio);
        outState.putString(ARG_COMPANY,Company);
        outState.putString(ARG_DATE,Date);
        outState.putString(ARG_STATUS,Status);
        outState.putString(ARG_NOTICE,Notice);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
