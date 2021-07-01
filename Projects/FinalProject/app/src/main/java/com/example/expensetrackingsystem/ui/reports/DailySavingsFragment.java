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
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDailySavingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DailySavingsFragment extends Fragment {

    private DailySavingsViewModel dailySavingsViewModel;
    private FragmentDailySavingsBinding binding;

    private Button mBack;
    private AnyChartView mSavingChart;
    private RecyclerView rvSaving;

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
        rvSaving = root.findViewById(R.id.expensesList);


        /*
        final TextView textView = binding.textDailySavings;
        dailySavingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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