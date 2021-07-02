package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentItemizedReportBinding;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment to display the itemized chart on a specific range of days.
 */
public class ItemizedReportFragment extends Fragment {

    private FragmentItemizedReportBinding binding;
    private EditText m_txtDateFrom;
    private EditText m_txtDateTo;
    private Button m_btnGo;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private AnyChartView m_anyChartView;
    private List<DataEntry> m_data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentItemizedReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // retrieve the widgets
        m_txtDateFrom = root.findViewById(R.id.txtDateFrom);
        m_txtDateTo = root.findViewById(R.id.txtDateTo);
        m_btnGo = root.findViewById(R.id.btnItemizedReport);
        m_anyChartView = root.findViewById(R.id.itemizedReportChart);

        // Populate chart
        Pie pie = AnyChart.pie();
        pie.title("Itemized Report");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title().text("Legend");
        m_anyChartView.setChart(pie);

        // set on click listener to the button
        m_btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email
                final String email = mAuth.getCurrentUser().getEmail();

                // Format the date
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date fromDate = null;
                Date toDate = null;
                try {
                    fromDate = df.parse(m_txtDateFrom.getText().toString());
                    toDate = df.parse(m_txtDateTo.getText().toString());
                } catch (ParseException e) { }

                // Using email to get the expenses
                db.collection("expenses")
                        .whereEqualTo("email", email)
                        .whereGreaterThanOrEqualTo("timestamp", fromDate)
                        .whereLessThanOrEqualTo("timestamp", toDate)
                        .orderBy("timestamp", Query.Direction.ASCENDING)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // Prepare data for the pie chart
                                Map<String, Double> map = new HashMap<>();
                                m_data = new ArrayList<>();
                                queryDocumentSnapshots.getDocuments()
                                        .stream()
                                        .forEach(document -> {
                                            final String expenseType = document.getString("expenseType");
                                            final Double amount = document.getDouble("amount");
                                            map.put(expenseType, map.getOrDefault(expenseType, 0D) + amount);
                                        });

                                // Aggregate data for the pie chart
                                map.keySet().forEach(expenseType -> m_data.add(new ValueDataEntry(expenseType, map.get(expenseType))));

                                // set the chart
                                pie.data(m_data);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {}
                        });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}