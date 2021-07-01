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

/**
 * Adapter for all reports and data entry.
 */
public class DataEntryRecyclerViewAdapter extends RecyclerView.Adapter <DataEntryRecyclerViewAdapter.ViewHolder> {

    private final List<Expense> mValues;
    public DataEntryRecyclerViewAdapter(List<Expense> Expenses) { this.mValues = Expenses; }

    /**
     * This method is called right when the adapter is created
     * and is used to initialize the ViewHolder(s)
     * @param parent
     * @param viewType
     * @return the ViewHolder
     */
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method is called for each ViewHolder to bind it to the adapter. Pass data to ViewHolder.
     * @param holder
     * @param position the position of the expense.
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the expense using the position
        holder.mItem = mValues.get(position);
        // Get the expense type
        holder.mName.setText(mValues.get(position).getExpenseType());
        // Get the amount
        holder.mPrice.setText(holder.mItem.getAmount().toString());

        // Format the date
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

    /**
     * Returns the number of expenses
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * A class that caches views associated with the report_list_item.xml layouts.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mName;
        final TextView mPrice;
        final TextView mDate;
        Expense mItem;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            // Retrieve the widgets
            mView = itemView;
            mName = itemView.findViewById(R.id.tvItem);
            mPrice = itemView.findViewById(R.id.tvPrice);
            mDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
