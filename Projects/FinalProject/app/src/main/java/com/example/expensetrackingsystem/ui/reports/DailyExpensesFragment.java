package com.example.expensetrackingsystem.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackingsystem.databinding.FragmentDailyExpensesBinding;

public class DailyExpensesFragment extends Fragment {

    private DailyExpensesViewModel dailyExpensesViewModel;
    private FragmentDailyExpensesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dailyExpensesViewModel =
                new ViewModelProvider(this).get(DailyExpensesViewModel.class);

        binding = FragmentDailyExpensesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}