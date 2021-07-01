package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDailyExpensesBinding;
import com.example.expensetrackingsystem.model.Expense;
import com.example.expensetrackingsystem.ui.data_entry.DataEntryRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class DailyExpensesFragment extends Fragment implements View.OnClickListener {

    private DailyExpensesViewModel dailyExpensesViewModel;
    private FragmentDailyExpensesBinding binding;

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

        // Retrieve the widgets
        mBack = root.findViewById(R.id.btnDeBack);
        mNew = root.findViewById(R.id.btnDeNew);
        rvExpense = root.findViewById(R.id.expensesList);
        rvExpense.setLayoutManager(new LinearLayoutManager(getContext()));

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

    @Override
    public void onClick(View v){
        // TODO: Add specific code for each listener
    }
}