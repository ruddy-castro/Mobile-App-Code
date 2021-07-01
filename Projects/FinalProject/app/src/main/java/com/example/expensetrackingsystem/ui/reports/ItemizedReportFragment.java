package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentItemizedReportBinding;
import com.example.expensetrackingsystem.model.Expense;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ItemizedReportFragment extends Fragment {

    private FragmentItemizedReportBinding binding;

    private EditText m_txtDateFrom;
    private EditText m_txtDateTo;
    private Button m_btnGo;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "Ly: "; // ItemizedReportFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentItemizedReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        m_txtDateFrom = root.findViewById(R.id.txtDateFrom);
        m_txtDateTo = root.findViewById(R.id.txtDateTo);
        m_btnGo = root.findViewById(R.id.btnItemizedReport);
        System.out.printf("Ly: btn = %s\n", m_btnGo.getText().toString());

        // Init the pie chart
//        AnyChartView anyChartView = root.findViewById(R.id.itemizedReportChart);
//        Pie pie = AnyChart.pie();
//        pie.title("Itemized Report");
//        pie.labels().position("outside");
//        pie.legend().title().enabled(true);
////        pie.legend().title().text("legend title");
//
//        // Add listener for the Go button
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        final String email = mAuth.getCurrentUser().getEmail();
        Log.i(TAG, "Setting listener for go button");
        m_btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Generating itemized report");
                System.out.println("Hello");
//                Date fromDate = null;
//                Date toDate = null;
//                try {
//                    fromDate = df.parse(m_txtDateFrom.getText().toString());
//                    toDate = df.parse(m_txtDateTo.getText().toString());
//                } catch (ParseException e) { }
//                db.collection("expenses")
//                        .whereEqualTo("email", email)
//                        .whereGreaterThanOrEqualTo("timestamp", fromDate)
//                        .whereLessThanOrEqualTo("timestamp", toDate)
//                        .orderBy("timestamp", Query.Direction.ASCENDING)
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                Log.i(TAG, "Retrieved the expenses successfully");
//                                List<DataEntry> data = queryDocumentSnapshots.getDocuments()
//                                        .stream()
//                                        .map(document -> new ValueDataEntry(document.getTimestamp("timestamp").toDate().toString(), document.getDouble("amount")))
//                                        .collect(Collectors.toList());
//
//                                // Populate chart
//                                pie.data(data);
//                                anyChartView.setChart(pie);
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull @NotNull Exception e) {
//                                Log.w(TAG, "Failed to fetch expenses");
//                            }
//                        });
            }
        });


        /*
        final TextView textView = binding.textItemizedReport;
        itemizedReportViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}