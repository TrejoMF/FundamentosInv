package com.multimarca.tae.voceadorestae.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.Login;
import com.multimarca.tae.voceadorestae.Objects.MenuElement;
import com.multimarca.tae.voceadorestae.R;
import com.multimarca.tae.voceadorestae.utils.Global;
import com.multimarca.tae.voceadorestae.wservices.RestApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuElement> itemsMenu;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_menu_principal, null);

        return new ViewHolder(view, new ViewHolder.HolderClick() {
            @Override
            public void onIClick(View view, ViewHolder viewHolder) {
                Bundle args = new Bundle();
                args.putString("Titulo",viewHolder.Titulo);
                if(viewHolder.intent != null ) {
                    viewHolder.intent.putExtras(args);
                    context.startActivity(viewHolder.intent);
                }else{
                    Global.EdLOGGED(context);
                    context.startActivity(new Intent(context, Login.class));

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("token", Global.TOKEN(context));
                        jsonObject.put("sales_point", Global.USUARIO(context));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new RestApiClient(Global.URL_D, "api/gcm_client/delete_sales_point/", jsonObject, RestApiClient.METHOD.POST, new RestApiClient.RestInterface() {
                        @Override
                        public void onFinish(String Result) {

                        }

                        @Override
                        public void onBefore() {

                        }
                    }).execute();
                    Global.EdUSUARIO(context, "");
                    Global.EdTOKEN(context, "");
                    Global.EdPASS(context, "");
                    ((Activity) context).finish();
                }
            }
        });
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuElement menuElement = this.itemsMenu.get(position);
        holder.opcion_TextView.setText(menuElement.get__Name());
//        holder.imageViewIcon.setImageResource(menuElement.getIconButton());
        holder.intent = menuElement.get__intentClass();
        holder.Titulo = menuElement.get__Titulo();
    }

    @Override
    public int getItemCount() {
        if(this.itemsMenu != null)
        {
            return this.itemsMenu.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView opcion_TextView;
        public HolderClick holderClick;
//        public ImageView imageViewIcon;
        public Intent intent;
        public String Titulo;

        public ViewHolder(View itemView, HolderClick holderClick) {
            super(itemView);
            this.holderClick = holderClick;
            this.opcion_TextView = (TextView)itemView.findViewById(R.id.item_menu_texto);
//            this.imageViewIcon = (ImageView)itemView.findViewById(R.id.iconButton);
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
    public MenuAdapter(Context context, ArrayList<MenuElement> itemsMenu) {
        this.context = context;
        this.itemsMenu = itemsMenu;
    }
}