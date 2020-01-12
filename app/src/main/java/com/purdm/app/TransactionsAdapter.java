package com.purdm.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purdm.app.R;
import com.purdm.app.TransactionModel;
import com.purdm.app.TransactionsHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsHolder> {

    List<TransactionModel> transactions;

    public TransactionsAdapter(List<TransactionModel> transactions){
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=null;
        RecyclerView.ViewHolder vh =null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        return new TransactionsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsHolder holder, int position) {
        holder.mapView(transactions.get(position));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void refreshAdapter(List<TransactionModel> models){
        this.transactions = models;
        this.notifyDataSetChanged();
    }
}
