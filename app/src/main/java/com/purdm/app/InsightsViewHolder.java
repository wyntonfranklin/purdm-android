package com.purdm.app;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InsightsViewHolder extends RecyclerView.ViewHolder {

    TextView header;
    TextView subTitle;
    TextView percentage;
    View color;


    public InsightsViewHolder(@NonNull View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.icon);
        subTitle = itemView.findViewById(R.id.description);
        percentage = itemView.findViewById(R.id.percentage);
        color = itemView.findViewById(R.id.color);
    }

    public void mapView(InsightsModel insightsModel) {
        if(insightsModel.type.equals("view")){
            header.setText(insightsModel.getLabel());
            subTitle.setText( "$" + insightsModel.getAmount());
            percentage.setText(String.valueOf(insightsModel.getFloatPercentage()) + "%");
            color.setBackgroundColor(Color.parseColor(insightsModel.getColor()));
        }
    }
}
