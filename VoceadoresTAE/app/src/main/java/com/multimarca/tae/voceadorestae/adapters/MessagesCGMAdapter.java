package com.multimarca.tae.voceadorestae.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.NotificationDetail;
import com.multimarca.tae.voceadorestae.Objects.Message;
import com.multimarca.tae.voceadorestae.R;

import java.util.ArrayList;


/**
 * Created by erick on 1/11/16. Multimarca
 */
public class MessagesCGMAdapter extends RecyclerView.Adapter<MessagesCGMAdapter.ViewHolder> {

    Context context;
    ArrayList<Message> Messages;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message, null);

        return new ViewHolder(view, new ViewHolder.HolderClick() {
            @Override
            public void onIClick(View view, ViewHolder viewHolder) {
                Bundle args = new Bundle();
                Message message = viewHolder.message;
                args.putString("Message", message.getMessage());
                args.putString("Title", message.getTitle());
                Intent intent = new Intent(context, NotificationDetail.class);
                intent.putExtras(args);
                context.startActivity(intent);


/*

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
        Message message = this.Messages.get(position);
        holder.messageMessage.setText(message.getMessage());
        holder.messageTitle.setText(message.getTitle());
        holder.message = message;
    }

    @Override
    public int getItemCount() {
        if(this.Messages != null)
        {
            return this.Messages.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView messageTitle;
        public TextView messageMessage;
        HolderClick holderClick;
        Message message;

        public ViewHolder(View itemView, HolderClick holderClick) {
            super(itemView);
            this.holderClick = holderClick;
            this.messageMessage = (TextView)itemView.findViewById(R.id.message_message);
            this.messageTitle = (TextView)itemView.findViewById(R.id.message_title);
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
    public MessagesCGMAdapter(Context context, ArrayList<Message> itemsMenu) {
        this.context = context;
        this.Messages = itemsMenu;
    }

}

