package com.multimarca.tae.voceadorestae.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multimarca.tae.voceadorestae.R;

import java.util.ArrayList;


/**
 * Created by erick on 1/11/16. Multimarca
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    Context context;
    ArrayList<String> Strings;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_simple, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = this.Strings.get(position);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        if(this.Strings != null)
        {
            return this.Strings.size();
        }else{
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView)itemView.findViewById(R.id.text_simple);
        }

    }
    public SimpleAdapter(Context context, ArrayList<String> itemsRecycler) {
        this.context = context;
        this.Strings = itemsRecycler;

    }


}

