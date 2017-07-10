package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class NotificationDetail extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        Bundle extras  = getIntent().getExtras();

        toolbar      = (Toolbar)findViewById(R.id.medium_toolbar);

        String Title = extras.getString("Title");
        String Message = extras.getString("Message");

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(onClickBack);


        TextView textViewMessage = (TextView)findViewById(R.id.message_notificationMessage);
        TextView textViewTitle = (TextView)findViewById(R.id.medium_titulo);


        textViewMessage.setText(Message);
        textViewTitle.setText(Title);

    }

    public View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
