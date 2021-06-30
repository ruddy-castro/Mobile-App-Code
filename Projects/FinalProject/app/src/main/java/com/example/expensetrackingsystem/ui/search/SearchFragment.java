package com.example.expensetrackingsystem.ui.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private SearchViewModel searchViewModelViewModel;
    private FragmentSearchBinding binding;
    private TextView txtBack;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModelViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Wire TextViews from Search and set up their listeners
        txtBack = (TextView) root.findViewById(R.id.txtBackS);
        txtBack.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            // TODO: Add specific code for each listener
            case R.id.txtBackS:
                txtBack.setTextColor(Color.RED);
                break;
        }
    }
}