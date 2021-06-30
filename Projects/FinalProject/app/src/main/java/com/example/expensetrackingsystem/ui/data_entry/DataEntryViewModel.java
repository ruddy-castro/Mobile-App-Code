package com.example.expensetrackingsystem.ui.data_entry;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetrackingsystem.model.ExpenseItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataEntryViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<ExpenseItem>> expenses;
    private FirebaseFirestore db;
    private FirebaseAuth user;

    public DataEntryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is data entry fragment");

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance();

        expenses = new MutableLiveData<>();

        getExpensesData();
    }

    private void getExpensesData() {
        db.collection("expenses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    ArrayList<ExpenseItem> items = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if (document.getString("email").equals(user.getCurrentUser().getEmail()))
                        {
                            Log.d("Data Entry VM", document.getId() + " => " + document.getData());
                            ExpenseItem e = ExpenseItem.builder().build();
                            e.setAmount(Float.parseFloat(document.getString("amount")));
                            e.setEmail(document.getString("email"));
                            e.setExpenseTypeId(document.getString("expenseTypeId"));
                            e.setTimestamp((Timestamp) document.get("timestamp"));

                            items.add(e);
                        }
                    }

                    expenses.setValue(items);
                }

                else
                    {
                        Log.d("Data Entry VM", "Error getting documents: ", task.getException());
                    }
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }


}