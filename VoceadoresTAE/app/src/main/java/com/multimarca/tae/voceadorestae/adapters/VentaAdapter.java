package com.multimarca.tae.voceadorestae.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Objects.Venta;
import com.multimarca.tae.voceadorestae.R;

import java.util.ArrayList;


/**
 * Created by erick on 1/11/16. Multimarca
 */
public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    Context context;
    ArrayList<Venta> Ventas;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_venta, null);

        return new ViewHolder(view, new ViewHolder.HolderClick() {
            @Override
            public void onIClick(View view, ViewHolder viewHolder) {
                Bundle args = new Bundle();
/*                Venta venta = viewHolder.venta;

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
                context.startActivity(intent);*/
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Venta venta = this.Ventas.get(position);
        holder.ventaNumero.setText(venta.getNumero());
        holder.ventaMonto.setText(venta.getMonto());
        holder.ventaFecha.setText(venta.getFecha());
        holder.ventaFolio.setText(venta.getFolio());
        holder.ventaCompany.setText(venta.getCarrier());
        holder.venta = venta;
    }

    @Override
    public int getItemCount() {
        if(this.Ventas != null)
        {
            return this.Ventas.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView ventaNumero;
        public TextView ventaMonto;
        public TextView ventaFecha;
        public TextView ventaFolio;
        public TextView ventaCompany;
        HolderClick holderClick;
        Venta venta;

        public ViewHolder(View itemView, HolderClick holderClick) {
            super(itemView);
            this.holderClick = holderClick;
            this.ventaFecha = (TextView)itemView.findViewById(R.id.venta_fecha);
            this.ventaMonto = (TextView)itemView.findViewById(R.id.venta_monto);
            this.ventaNumero = (TextView)itemView.findViewById(R.id.venta_numero);
            this.ventaCompany = (TextView)itemView.findViewById(R.id.venta_carrier);
            this.ventaFolio = (TextView)itemView.findViewById(R.id.venta_folio);
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
    public VentaAdapter(Context context, ArrayList<Venta> itemsMenu) {
        this.context = context;
        this.Ventas = itemsMenu;
    }


}

