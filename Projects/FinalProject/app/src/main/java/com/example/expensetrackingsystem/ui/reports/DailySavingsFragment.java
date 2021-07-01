package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DailySavingsFragment extends Fragment {

    private FragmentDailySavingsBinding binding;

    private AnyChartView mSavingChart;
    private RecyclerView rvSaving;

    //Saving goal:
    //TODO retrieve real saving goal:
    private double mSavingGoal = 5000;
    private double mTotalSpent = 0;
    private double mDailySaving = 0;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDailySavingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Retrieve the widgets
        rvSaving = root.findViewById(R.id.savingList);
        rvSaving.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup charts
        mSavingChart = root.findViewById(R.id.dailySavingChart);

        // Get the email
        final String email = mAuth.getCurrentUser().getEmail();

        // Use email to get the dailySaving amount
        db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i("Ivy: ", "Got settings document successfully");

                        // Get the Annual Income
                        double annualIncome = documentSnapshot.getDouble("annualIncome");
                        mDailySaving = annualIncome / 365;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("Ivy: ", "Failed to get settings document");
                    }
                });
        
        // Use email to get the amount spend on a date
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
                        // create the chart
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
        tempData.add(new ValueDataEntry("daily saving", mDailySaving));
        pie.data(tempData);

        chart.setChart(pie);
    }
}