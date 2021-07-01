package com.example.expensetrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.expensetrackingsystem.model.Expense;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExpenseEntryActivity extends AppCompatActivity {

    private Spinner m_spinnerExpenseType;
    private EditText m_txtAmount;
    private EditText m_txtDate;
    private Button m_btnAddExpense;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "Ly: "; // ExpenseEntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        m_spinnerExpenseType = findViewById(R.id.spinnerExpenseType);
        m_txtAmount = findViewById(R.id.txtAmount);
        m_txtDate = findViewById(R.id.txtDate);
        m_btnAddExpense = findViewById(R.id.btnAddExpense);

        final String email = mAuth.getCurrentUser().getEmail();
        db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Boolean> expenseTypes = (Map<String, Boolean>) documentSnapshot.get("expenseTypes");
                        List<String> expenseTypesList = expenseTypes == null ? new ArrayList<>() : new ArrayList<>(expenseTypes.keySet());
                        Log.i(TAG, "expenseTypesList = " + expenseTypesList);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ExpenseEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, expenseTypesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        m_spinnerExpenseType.setAdapter(adapter);
                    }
                });

        m_btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Timestamp timestamp;
                try {
                    Date date = df.parse(m_txtDate.getText().toString());
                    Log.i(TAG, "date = " + date);
                    timestamp = new Timestamp(date);
                } catch (ParseException e) {
                    Log.w(TAG, "Unable to parse the date");
                    return;
                }
                Expense expense = Expense.builder()
                        .amount(Double.parseDouble(m_txtAmount.getText().toString()))
                        .expenseType(m_spinnerExpenseType.getSelectedItem().toString())
                        .timestamp(timestamp)
                        .email(email)
                        .build();

                db.collection("expenses").add(expense)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.i(TAG, "Successfully saved new expense to DB");

                                Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.i(TAG, "Failed to save new expense entry", e);
                            }
                        });

            }
        });

    }
}