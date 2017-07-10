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
import com.multimarca.tae.voceadorestae.Services;

import org.json.JSONException;
import org.json.JSONObject;


public class ResumeServices extends Fragment {

    TextView folioTextView;
    TextView dateTextView;
    TextView descTextView;
    AppBarLayout action_bar_big;
    Button gobackButton;
    TextView NoticeTextView;
    TextView BalanceTextView;

    public ResumeServices() {
        // Required empty public constructor
    }

    public static ResumeServices newInstance() {
        ResumeServices fragment = new ResumeServices();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_resume_services, container, false);

        folioTextView     = (TextView)view.findViewById(R.id.resume_folio_number);
        dateTextView      = (TextView)view.findViewById(R.id.resume_date_today);
        BalanceTextView   = (TextView) view.findViewById(R.id.resume_balance_number);
        NoticeTextView    = (TextView)view.findViewById(R.id.notice_tae);
        action_bar_big    = (AppBarLayout) getActivity().findViewById(R.id.action_bar_big);
        gobackButton      = (Button) view.findViewById(R.id.resume_goback);

        gobackButton.setOnClickListener(goBack);

        descTextView = (TextView)getActivity().findViewById(R.id.big_header_titulo);

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            JSONObject jsonObject = ((Services)getActivity()).getData();
            try {
                if(jsonObject != null) {
                    String status   = jsonObject.getString("status");
                    String folioString = (jsonObject.has("folio")) ?  jsonObject.getString("folio") : "";
                    String dateString  = (jsonObject.has("date")) ?  jsonObject.getString("date") : "";
                    String descString  = (jsonObject.has("description")) ?  jsonObject.getString("description") : "";
                    String noticeString = (jsonObject.has("notice")) ?  jsonObject.getString("notice") : "";
                    String Balance = (jsonObject.has("balance")) ?  jsonObject.getString("balance") : "";


//                    BalanceTextView.setText(Balance);
                    folioTextView.setText(folioString);
                    dateTextView.setText(dateString);
                    descTextView.setText(descString);
                    NoticeTextView.setText(noticeString);
                    BalanceTextView.setText(Balance);
                    if(status.equals("00")) {
                        setCorrect();
                    }else{
                        setError();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    View.OnClickListener goBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Services)getActivity()).getViewPager().setCurrentItem(0);
        }
    };

    public void setError() {

        action_bar_big.setBackgroundColor(Color.parseColor("#d32f2f"));
        folioTextView.setTextColor(Color.parseColor("#b71c1c"));
        dateTextView.setTextColor(Color.parseColor("#b71c1c"));
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
        folioTextView.setTextColor(Color.parseColor("#1b5e20"));
        dateTextView.setTextColor(Color.parseColor("#1b5e20"));
        NoticeTextView.setTextColor(Color.parseColor("#1b5e20"));
        BalanceTextView.setTextColor(Color.parseColor("#1b5e20"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#1b5e20"));
        }
    }

}
