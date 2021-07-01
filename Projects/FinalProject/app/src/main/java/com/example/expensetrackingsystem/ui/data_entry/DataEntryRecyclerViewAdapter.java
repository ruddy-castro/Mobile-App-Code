package com.example.expensetrackingsystem.ui.data_entry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.model.ExpenseItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataEntryRecyclerViewAdapter extends RecyclerView.Adapter <DataEntryRecyclerViewAdapter.ViewHolder> {
    private final List<ExpenseItem> mValues;

    public DataEntryRecyclerViewAdapter(List<ExpenseItem> expenseItems) { this.mValues = expenseItems; }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getExpenseTypeId());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Set up listener for selecting an item
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        final View mView;
        final TextView mName;
        ExpenseItem mItem;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.tvItem);
        }
    }
}
