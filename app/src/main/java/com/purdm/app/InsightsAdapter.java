package com.purdm.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InsightsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    List<InsightsModel> list;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public InsightsAdapter(List<InsightsModel> models){
        this.list = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=null;
        if (viewType == TYPE_HEADER) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.insights_header, parent, false);
            return new InsightsHeaderHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {
            RecyclerView.ViewHolder vh =null;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.insights_row, parent, false);
            return new InsightsViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof InsightsViewHolder){
            ((InsightsViewHolder) holder).mapView(list.get(position));
        }else if(holder instanceof InsightsHeaderHolder){
            ((InsightsHeaderHolder) holder).mapView(list);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void refreshAdapter(List<InsightsModel> models){
        this.list = models;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
