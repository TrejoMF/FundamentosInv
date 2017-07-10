package com.multimarca.tae.voceadorestae;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class DetalleTraspaso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_traspaso);

        Bundle extras  = getIntent().getExtras();
        String _Cliente = extras.getString("Cliente");
        String _Banco = extras.getString("Banco");
        String _Auth = extras.getString("Auth");
        String _Referencia = extras.getString("Refe");
        String _Desc = extras.getString("Desc");
        String _Supe = extras.getString("Supv");
        String _Valor = extras.getString("Valor");
        String _Fecha = extras.getString("Date");


        TextView clienteText = (TextView)findViewById(R.id.dtraspaso_Cliente);
        TextView bancoText = (TextView)findViewById(R.id.dtraspaso_Banco);
        TextView authText = (TextView)findViewById(R.id.dtraspaso_Auth);
        TextView refText = (TextView)findViewById(R.id.dtraspaso_Referencia);
        TextView supeText = (TextView)findViewById(R.id.dtraspaso_Supe);
        TextView valorText = (TextView)findViewById(R.id.dtraspaso_Valor);
        TextView fechaText = (TextView)findViewById(R.id.dtraspaso_Fecha);
        TextView descText = (TextView)findViewById(R.id.dtraspaso_Desc);
        TextView title = (TextView)findViewById(R.id.medium_titulo);

        title.setText("Detalle de traspaso");

        Log.d("TAGDETALLE",
                _Cliente + "--" +
                        _Banco + "--" +
                        _Auth + "--" +
                        _Referencia + "--" +
                        _Supe + "--" +
                        _Valor + "--" +
                        _Fecha + "--" +
                        _Desc + "--"
        );

        clienteText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Cliente), _Cliente));
        bancoText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_banco), _Banco));
        authText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Auth), _Auth));
        refText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Ref), _Referencia));
        supeText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Supe), _Supe));
        valorText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Valor), _Valor));
        fechaText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Fecha), _Fecha));
        descText.setText(String.format("%s%s", getString(R.string.dtraspaso_text_Desc), _Desc));

    }
}
