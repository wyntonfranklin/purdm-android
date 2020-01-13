package com.purdm.app;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InsightsViewHolder extends RecyclerView.ViewHolder {

    TextView header;
    TextView subTitle;
    TextView percentage;


    public InsightsViewHolder(@NonNull View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.icon);
        subTitle = itemView.findViewById(R.id.description);
        percentage = itemView.findViewById(R.id.percentage);
    }

    public void mapView(InsightsModel insightsModel) {
        if(insightsModel.type.equals("view")){
            header.setText(insightsModel.getLabel());
            subTitle.setText( insightsModel.getAmount());
            percentage.setText(insightsModel.getPercentage());
        }
    }
}
