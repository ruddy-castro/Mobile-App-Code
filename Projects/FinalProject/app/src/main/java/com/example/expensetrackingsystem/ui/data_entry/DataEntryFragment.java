package com.example.expensetrackingsystem.ui.data_entry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDataEntryBinding;
import com.example.expensetrackingsystem.model.ExpenseItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DataEntryFragment extends Fragment implements View.OnClickListener {

    private DataEntryViewModel dataEntryViewModel;
    private FragmentDataEntryBinding binding;
    private ImageButton newExpense;
    private TextView txtBack;
    private RecyclerView rv;
    private FirebaseFirestore db;
    private FirebaseAuth user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataEntryViewModel =
                new ViewModelProvider(this).get(DataEntryViewModel.class);

        binding = FragmentDataEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();

        // Wire clickers from xml and set up their listeners
        newExpense = (ImageButton) root.findViewById(R.id.ibNew);
        txtBack = (TextView) root.findViewById(R.id.txtBackDE);
        newExpense.setOnClickListener(this);
        txtBack.setOnClickListener(this);

        rv = root.findViewById(R.id.rvDataEntry);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        dataEntryViewModel.getExpensesData().observe(getViewLifecycleOwner(), new Observer<List<ExpenseItem>>() {
            @Override
            public void onChanged(List<ExpenseItem> expenseItems) {
                rv.setAdapter(new DataEntryRecyclerViewAdapter(expenseItems));
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        // TODO: Add specific code for each listener
        switch (v.getId())
        {
            case R.id.ibNew:
                Toast.makeText(getContext(), "Adding a new expense item.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtBackDE:
                txtBack.setTextColor(Color.RED);
                break;
        }
    }
}