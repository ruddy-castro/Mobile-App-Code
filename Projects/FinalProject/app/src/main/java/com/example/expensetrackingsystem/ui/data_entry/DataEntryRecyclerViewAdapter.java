package com.example.expensetrackingsystem.ui.data_entry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.model.Expense;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DataEntryRecyclerViewAdapter extends RecyclerView.Adapter <DataEntryRecyclerViewAdapter.ViewHolder> {
    private final List<Expense> mValues;

    public DataEntryRecyclerViewAdapter(List<Expense> Expenses) { this.mValues = Expenses; }

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
        holder.mName.setText(mValues.get(position).getExpenseType());
        holder.mPrice.setText(holder.mItem.getAmount().toString());

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        holder.mDate.setText(df.format(holder.mItem.getTimestamp().toDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Set up listener for selecting an item
                Toast.makeText(holder.itemView.getContext(),
                        "Adding a new item with same information as " + mValues.get(position).getExpenseType() + ".",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        final View mView;
        final TextView mName;
        final TextView mPrice;
        final TextView mDate;
        Expense mItem;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            mView = itemView;
            mName = itemView.findViewById(R.id.tvItem);
            mPrice = itemView.findViewById(R.id.tvPrice);
            mDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
