package com.example.expensetrackingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

/**
 * A class for enter expense.
 */
public class ExpenseEntryActivity extends AppCompatActivity {

    private Spinner m_spinnerExpenseType;
    private EditText m_txtAmount;
    private EditText m_txtDate;
    private Button m_btnAddExpense;

    // Firestore
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        // Retrieve the widgets
        m_spinnerExpenseType = findViewById(R.id.spinnerExpenseType);
        m_txtAmount = findViewById(R.id.txtAmount);
        m_txtDate = findViewById(R.id.txtDate);
        m_btnAddExpense = findViewById(R.id.btnAddExpense);

        // Get the email of the current user.
        final String email = mAuth.getCurrentUser().getEmail();
        // Collect data in the settings database.
        db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Boolean> expenseTypes = (Map<String, Boolean>) documentSnapshot.get("expenseTypes");
                        // Set adapter for the spinner
                        List<String> expenseTypesList = expenseTypes == null ? new ArrayList<>() : new ArrayList<>(expenseTypes.keySet());
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ExpenseEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, expenseTypesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        m_spinnerExpenseType.setAdapter(adapter);
                    }
                });

        // Set on click listener for the add expense button.
        m_btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Format the date
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Timestamp timestamp;
                try {
                    Date date = df.parse(m_txtDate.getText().toString());
                    timestamp = new Timestamp(date);
                } catch (ParseException e) { return; }
                // Get the expense
                Expense expense = Expense.builder()
                        .amount(Double.parseDouble(m_txtAmount.getText().toString()))
                        .expenseType(m_spinnerExpenseType.getSelectedItem().toString())
                        .timestamp(timestamp)
                        .email(email)
                        .build();

                // Add expense to the expenses list
                db.collection("expenses").add(expense)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // send user back to the welcome screen.
                                Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) { }
                        });

            }
        });

    }
}