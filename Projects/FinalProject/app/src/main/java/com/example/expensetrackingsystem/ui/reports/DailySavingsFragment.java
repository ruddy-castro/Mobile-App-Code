package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDailySavingsBinding;
import com.example.expensetrackingsystem.model.Expense;
import com.example.expensetrackingsystem.ui.data_entry.DataEntryRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DailySavingsFragment extends Fragment implements View.OnClickListener{

    private FragmentDailySavingsBinding binding;

    private Button mBack;
    private AnyChartView mSavingChart;
    private RecyclerView rvSaving;

    //Saving goal:
    //TODO retrieve real saving goal:
    private float mSavingGoal = 5000;
    private float mTotalSpent = 0;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*dailySavingsViewModel =
                new ViewModelProvider(this).get(DailySavingsViewModel.class);*/

        binding = FragmentDailySavingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Retrieve the widgets
        mBack = (Button) root.findViewById(R.id.btnDsBack);
        rvSaving = root.findViewById(R.id.savingList);
        rvSaving.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup charts
        mSavingChart = root.findViewById(R.id.dailySavingChart);

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
                        rvSaving.setAdapter(new DataEntryRecyclerViewAdapter(expenses));

                        // calculate the total spent
                        for (int i = 0; i < expenses.size(); i++) {
                            mTotalSpent += expenses.get(i).getAmount();
                        }
                        populateChart(mSavingChart);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {}
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateChart(AnyChartView chart) {
        Pie pie = AnyChart.pie();
        List<DataEntry> tempData = new ArrayList<>();

        tempData.add(new ValueDataEntry("total spent", mTotalSpent));
        tempData.add(new ValueDataEntry("left", mSavingGoal-mTotalSpent));
        pie.data(tempData);

        chart.setChart(pie);
    }

    @Override
    public void onClick(View v){
        // TODO: Add specific code for each listener
    }
}