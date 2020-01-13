package com.purdm.app;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InsightsHeaderHolder extends RecyclerView.ViewHolder {

    PieChart mPieChart;

    public InsightsHeaderHolder(@NonNull View itemView) {
        super(itemView);
        mPieChart = itemView.findViewById(R.id.piechart);
    }

    public void mapView(List<InsightsModel> items) {
        loadPieChart(items);
    }

    public void loadPieChart(final List<InsightsModel> insights) {
        mPieChart.clearChart();
        for(int i=0; i<= insights.size()-1; i++){
            float value = (float) 3.0;
            InsightsModel model = insights.get(i);
            if(model.getType().equals("view")){
                mPieChart.addPieSlice(new PieModel(model.getLabel(), model.getFloatAmount(), Color.parseColor(model.getColor())));
            }
        }
    }
}
