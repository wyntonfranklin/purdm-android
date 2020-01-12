package com.purdm.app;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView header;
    TextView subTitle;
    TextView description;
    TextView rightContent;
    Context context;

    public TransactionsHolder(@NonNull View itemView) {
        super(itemView);
        header = itemView.findViewById(R.id.header);
        subTitle = itemView.findViewById(R.id.sub_title);
        description = itemView.findViewById(R.id.description);
        rightContent = itemView.findViewById(R.id.right_content);
    }

    @Override
    public void onClick(View view) {

    }

    public void mapView(TransactionModel model){
        String transFooter = model.description + " | " + model.getAccountName();
        header.setText(model.getHeader());
        description.setText(transFooter);
        subTitle.setText(model.getTransDate());
        rightContent.setText(model.getTransType());
        if(model.transType.equals("income")){
            rightContent.setTextColor(
                itemView.getContext()
                    .getResources()
                    .getColor(R.color.incomeAccent
            ));
        }
    }
}
