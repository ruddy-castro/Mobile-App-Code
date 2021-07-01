package com.example.expensetrackingsystem.ui.data_entry;

import android.os.Bundle;
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

/**
 * The fragment for data entry, show the list of expenses.
 */
public class DataEntryFragment extends Fragment {

    private FragmentDataEntryBinding binding;
    private RecyclerView rv;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDataEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Retrieve the widgets
        rv = root.findViewById(R.id.rvDataEntry);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the email of the current user
        final String email = mAuth.getCurrentUser().getEmail();

        // Collect data from expenses database
        db.collection("expenses").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Add all expenses to a list
                List<Expense> expenses = queryDocumentSnapshots.getDocuments()
                        .stream()
                        .map(document -> Expense.builder()
                                .email(email)
                                .amount(document.getDouble("amount"))
                                .expenseType(document.getString("expenseType"))
                                .timestamp(document.getTimestamp("timestamp"))
                                .build())
                        .collect(Collectors.toList());
                // Set adapter to the recycle view to show the list
                rv.setAdapter(new DataEntryRecyclerViewAdapter(expenses));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) { }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}