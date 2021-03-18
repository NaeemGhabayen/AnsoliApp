package com.example.ansolienapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;

import java.util.List;


public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolderTrind> {
    private Context context;
    private List<Relative> items;

    public itemAdapter(Context context, List<Relative> results) {
        this.context = context;
        this.items = results;

    }

    @NonNull
    @Override
    public ViewHolderTrind onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTrind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relative,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrind holder, int position) {
        holder.tv_relative.setText(position + " - " + items.get(position).getFullName() + "          " +
                items.get(position).getEmail());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setlist(List<Relative> Results) {
        this.items = Results;
        notifyDataSetChanged();
    }


    public Relative getAllResults(int pos) {
        return items.get(pos);
    }


    public class ViewHolderTrind extends RecyclerView.ViewHolder {
        TextView tv_relative;

        public ViewHolderTrind(@NonNull View itemView) {
            super(itemView);
            tv_relative = itemView.findViewById(R.id.tv_relative);
        }

    }


}







