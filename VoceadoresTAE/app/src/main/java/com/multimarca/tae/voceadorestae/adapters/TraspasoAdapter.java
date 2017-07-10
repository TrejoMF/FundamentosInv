package com.multimarca.tae.voceadorestae.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.DetalleTraspaso;
import com.multimarca.tae.voceadorestae.Objects.Traspaso;
import com.multimarca.tae.voceadorestae.R;

import java.util.ArrayList;


/**
 * Created by erick on 1/11/16. Multimarca
 */
public class TraspasoAdapter extends RecyclerView.Adapter<TraspasoAdapter.ViewHolder> {

    Context context;
    ArrayList<Traspaso> Traspasos;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_traspaso, null);

        return new ViewHolder(view, new ViewHolder.HolderClick() {
            @Override
            public void onIClick(View view, ViewHolder viewHolder) {
                Bundle args = new Bundle();
                Traspaso traspaso = viewHolder.traspaso;

                args.putString("Cliente", traspaso.get_Cliente());
                args.putString("Banco", traspaso.get_Banco());
                args.putString("Auth",  traspaso.get_Auth());
                args.putString("Refe",  traspaso.get_Referencia());
                args.putString("Desc",  traspaso.get_Desc());
                args.putString("Supv",  traspaso.get_Supe());
                args.putString("Valor", traspaso.get_Valor());
                args.putString("Date",  traspaso.get_Fecha());


                Intent intent = new Intent(context, DetalleTraspaso.class);
                intent.putExtras(args);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Traspaso traspaso = this.Traspasos.get(position);
        holder.traspasoBanco.setText(traspaso.get_Banco());
        holder.traspasoFecha.setText(traspaso.get_Fecha());
        holder.traspasoValor.setText(traspaso.get_Valor());
        holder.traspasoCliente.setText(traspaso.get_Cliente());
        holder.traspaso = traspaso;
    }

    @Override
    public int getItemCount() {
        if(this.Traspasos != null)
        {
            return this.Traspasos.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView traspasoBanco;
        public TextView traspasoCliente;
        public TextView traspasoFecha;
        public TextView traspasoValor;
        public HolderClick holderClick;
        Traspaso traspaso;

        public ViewHolder(View itemView, HolderClick holderClick) {
            super(itemView);
            this.holderClick = holderClick;
            this.traspasoBanco = (TextView)itemView.findViewById(R.id.traspaso_banco);
            this.traspasoCliente = (TextView)itemView.findViewById(R.id.traspaso_cliente);
            this.traspasoValor = (TextView)itemView.findViewById(R.id.traspaso_valor);
            this.traspasoFecha = (TextView)itemView.findViewById(R.id.traspaso_fecha);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            holderClick.onIClick(v,this);
        }

        public interface HolderClick {
            void onIClick(View view, ViewHolder viewHolder);
        }
    }
    public TraspasoAdapter(Context context, ArrayList<Traspaso> itemsMenu) {
        this.context = context;
        this.Traspasos = itemsMenu;
    }


}

