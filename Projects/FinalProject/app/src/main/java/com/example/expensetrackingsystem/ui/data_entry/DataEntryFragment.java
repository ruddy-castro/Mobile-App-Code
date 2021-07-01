package com.example.expensetrackingsystem.ui.data_entry;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetrackingsystem.R;
import com.example.expensetrackingsystem.databinding.FragmentDataEntryBinding;
import com.example.expensetrackingsystem.model.Expense;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class DataEntryFragment extends Fragment implements View.OnClickListener {

    private FragmentDataEntryBinding binding;
    //  private ImageButton newExpense;
    //  private Button txtBack;
    private RecyclerView rv;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = DataEntryFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDataEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Wire clickers from xml and set up their listeners
//        newExpense = (ImageButton) root.findViewById(R.id.ibNew);
//        txtBack = (TextView) root.findViewById(R.id.txtBackDE);
//        newExpense.setOnClickListener(this);
//        txtBack.setOnClickListener(this);

        rv = root.findViewById(R.id.rvDataEntry);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

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
                        Log.i(TAG, "expenses = " + expenses);
                        rv.setAdapter(new DataEntryRecyclerViewAdapter(expenses));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG, "Failed to fetch list of expenses");
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
//        switch (v.getId())
//        {
//            case R.id.ibNew:
//                Toast.makeText(getContext(), "Adding a new expense item.", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.txtBackDE:
//                txtBack.setTextColor(Color.RED);
//                break;
//        }
    }
}