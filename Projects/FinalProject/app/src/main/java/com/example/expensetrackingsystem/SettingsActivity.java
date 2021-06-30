package com.example.expensetrackingsystem;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import com.example.expensetrackingsystem.model.ExpenseType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        private static final String TAG = "Ly: "; // SettingsFragment.class.getSimpleName();

        private static final Map<String, String> PREFERENCE_KEY_TO_DB_FIELD = ImmutableMap.of(
                "annual_income", "annualIncome",
                "max_daily_expense", "maxDailyExpense",
                "saving_goal", "savingGoal"
        );

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Extract preferences
            PreferenceManager preferenceManager = getPreferenceManager();
            EditTextPreference annualIncomePreference = (EditTextPreference) preferenceManager.findPreference("annual_income");
            EditTextPreference maxDailyExpensePreference = (EditTextPreference) preferenceManager.findPreference("max_daily_expense");
            EditTextPreference savingGoalPreference = (EditTextPreference) preferenceManager.findPreference("saving_goal");

            // Set persistent false
            annualIncomePreference.setPersistent(false);
            maxDailyExpensePreference.setPersistent(false);
            savingGoalPreference.setPersistent(false);

            // Initialize preference values
            initializePreferenceValue(annualIncomePreference);
            initializePreferenceValue(maxDailyExpensePreference);
            initializePreferenceValue(savingGoalPreference);

            // Add listener to update the DB when a preference is updated
            addListenerToOnUpdatePreference(annualIncomePreference);
            addListenerToOnUpdatePreference(maxDailyExpensePreference);
            addListenerToOnUpdatePreference(savingGoalPreference);

            // Add expense type list as preferences
            PreferenceScreen preferenceScreen = preferenceManager.getPreferenceScreen();
            PreferenceCategory expenseTypesCategory = new PreferenceCategory(getContext());
            expenseTypesCategory.setTitle(R.string.expense_types_header);
            preferenceScreen.addPreference(expenseTypesCategory);

            final String email = mAuth.getCurrentUser().getEmail();
            db.collection("expenseTypes").whereEqualTo("email", email).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.i(TAG, "Got expense types document successfully");
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            for (int i = 0; i < documents.size(); i++) {
                                final DocumentSnapshot document = documents.get(i);
                                final EditTextPreference preference = buildExpenseTypePreference(document.getId(), document.get("itemName").toString());
                                preference.setOrder(i);
                                expenseTypesCategory.addPreference(preference);
                            };

                            // Add the "Add new expense" preference at the end
                            final EditTextPreference preference = buildNewExpenseTypePreference(documents.size(), expenseTypesCategory);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.i(TAG, String.format("Failed to get expense types for email '%s'", email));
                        }
                    });
        }

        private void addListenerToOnUpdatePreference(Preference preference) {
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final Double newVal = Double.parseDouble(newValue.toString());
                    final String email = mAuth.getCurrentUser().getEmail();
                    final String field = PREFERENCE_KEY_TO_DB_FIELD.get(preference.getKey());
                    db.collection("settings").document(email).update(field, newVal)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i(TAG, String.format("%s is saved successfully", field));
                                }
                            })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.i(TAG, String.format("Failed to save %s", field));
                            }
                        });
                    return true;
                }
            });
        }

        private void initializePreferenceValue(EditTextPreference preference) {
            // Get the preference value from db
            final String email = mAuth.getCurrentUser().getEmail();
            db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i(TAG, "Got settings document successfully");
                        final String field = PREFERENCE_KEY_TO_DB_FIELD.get(preference.getKey());
                        Log.i(TAG, String.format("key=%s, field=%s\n", preference.getKey(), field));
                        final Object value = documentSnapshot.get(field);
                        if (value != null) {
                            preference.setText(value.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i(TAG, "Failed to get settings document");
                    }
                });

            // Make preferences input type to be numeric
            preference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull @NotNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }

        EditTextPreference buildExpenseTypePreference(String documentId, String expenseType) {
            Log.i(TAG, String.format("Expense type: %s", expenseType));
            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setText(expenseType);
            preference.setPersistent(false);
            preference.setKey(documentId);
            preference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String newVal = newValue.toString();
                    final String field = "itemName";
                    db.collection("expenseTypes").document(documentId).update(field, newVal)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i(TAG, String.format("%s is saved successfully", field));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.i(TAG, String.format("Failed to save %s", field));
                                }
                            });
                    return true;
                }
            });
            return preference;
        }

        EditTextPreference buildNewExpenseTypePreference(int order, PreferenceCategory expenseTypesCategory) {
            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setOrder(order);
            preference.setPersistent(false);
            preference.setKey("new_expense_type");
            preference.setTitle("New Expense");
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String newVal = newValue.toString();
                    final ExpenseType expenseType = ExpenseType.builder()
                            .email(mAuth.getCurrentUser().getEmail())
                            .itemName(newVal)
                            .build();
                    db.collection("expenseTypes").add(expenseType)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.i(TAG, String.format("%s is saved successfully", expenseType));
                                    expenseTypesCategory.addPreference(buildExpenseTypePreference(documentReference.getId(), expenseType.getItemName()));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.i(TAG, String.format("Failed to save %s", expenseType));
                                }
                            });
                    preference.setOrder(preference.getOrder() + 1);
                    return true;
                }
            });
            expenseTypesCategory.addPreference(preference);
            return preference;
        }

    }
}