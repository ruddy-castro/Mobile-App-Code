package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDailyExpensesBinding;
import com.example.expensetrackingsystem.model.Expense;
import com.example.expensetrackingsystem.ui.data_entry.DataEntryRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;

public class DailyExpensesFragment extends Fragment implements View.OnClickListener {

    private FragmentDailyExpensesBinding binding;
    private List<DataEntry> data;

    private TextView mDate;
    private AnyChartView mExpenseChart;
    private RecyclerView rvExpense;
    private Button mGenerate;
    private Cartesian cartesian;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailyExpensesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Retrieve the widgets
        mGenerate = root.findViewById(R.id.btnGenerate);
        mDate = root.findViewById(R.id.editTextDate);
        rvExpense = root.findViewById(R.id.expensesList);

        // Setup charts
        mExpenseChart = root.findViewById(R.id.dailyExpensesChart);
        data = new ArrayList<>();

        mGenerate.setOnClickListener(this);

        cartesian= AnyChart.column();
        cartesian.animation(true);
        cartesian.title("Daily Expenses Per Item");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Expense");

        mExpenseChart.setChart(cartesian);

        return root;
    }

    private void populateChart(Cartesian cartesian) {
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnGenerate:
                // Query Firestore for data
                final String email = mAuth.getCurrentUser().getEmail();

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date date = null;
                Date fromDate = null;
                Date toDate = null;
                try {
                    date = df.parse(mDate.getText().toString());
                } catch (ParseException e) { }

                if (date != null) {
                    Calendar c = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c.setTime(date);
                    c2.setTime(date);
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    c2.add(Calendar.DAY_OF_MONTH, 1);
                    fromDate = c.getTime();
                    toDate = c2.getTime();
                }

                // TODO: Figure out how to query for a specific day
                db.collection("expenses").whereEqualTo("email", email)
                        .whereGreaterThanOrEqualTo("timestamp", fromDate)
                        .whereLessThanOrEqualTo("timestamp", toDate).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<Expense> expenses = queryDocumentSnapshots.getDocuments()
                                        .stream()
                                        .map(document -> Expense.builder()
                                                .email(email)
                                                .amount(document.getDouble("amount"))
                                                .expenseType(document.getString("expenseType"))
                                                .timestamp(document.getTimestamp("timestamp"))
                                                .build())
                                        .collect(Collectors.toList());
                                rvExpense.setAdapter(new DataEntryRecyclerViewAdapter(expenses));

                                if (data.size() > 0)
                                    data.clear();

                                // Adding data into chart data list
                                for (int i = 0; i < expenses.size(); i++)
                                    data.add(new ValueDataEntry(expenses.get(i).getExpenseType().toString(), expenses.get(i).getAmount()));

                                //populateChart(cartesian);
                                cartesian.removeAllSeries();
                                cartesian.column(data);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {}
                        });
        }

    }
}


