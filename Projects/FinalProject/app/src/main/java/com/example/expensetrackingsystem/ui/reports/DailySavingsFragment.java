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

import com.example.expensetrackingsystem.databinding.FragmentDailySavingsBinding;

public class DailySavingsFragment extends Fragment {

    private DailySavingsViewModel dailySavingsViewModel;
    private FragmentDailySavingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dailySavingsViewModel =
                new ViewModelProvider(this).get(DailySavingsViewModel.class);

        binding = FragmentDailySavingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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