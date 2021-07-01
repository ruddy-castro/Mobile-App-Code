package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;

public class DailyExpensesFragment extends Fragment implements View.OnClickListener {

    private DailyExpensesViewModel dailyExpensesViewModel;
    private FragmentDailyExpensesBinding binding;
    private List<DataEntry> data;

    private Button mBack;
    private Button mNew;
    private AnyChartView mExpenseChart;
    private RecyclerView rvExpense;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*dailyExpensesViewModel =
                new ViewModelProvider(this).get(DailyExpensesViewModel.class);*/

        binding = FragmentDailyExpensesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Retrieve the widgets
        mBack = root.findViewById(R.id.btnDeBack);
        mNew = root.findViewById(R.id.btnDeNew);
        rvExpense = root.findViewById(R.id.expensesList);
        rvExpense.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup charts
        mExpenseChart = root.findViewById(R.id.dailyExpensesChart);
        Cartesian cartesian = AnyChart.column();
        data = new ArrayList<>();

        // Query Firestore for data
        final String email = mAuth.getCurrentUser().getEmail();
        db.collection("expenses").whereEqualTo("email", email).get()
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

                        // Adding data into chart data list
                        for (int i = 0; i < expenses.size(); i++)
                            data.add(new ValueDataEntry(expenses.get(i).getTimestamp().toDate().toString(), expenses.get(i).getAmount()));

                        populateChart(cartesian, mExpenseChart);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {}
                });



        return root;
    }

    private void populateChart(Cartesian cartesian, AnyChartView anyChartView) {
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Daily Expenses Per Item");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Expense");

        anyChartView.setChart(cartesian);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v){
        // TODO: Add specific code for each listener

    }
}


