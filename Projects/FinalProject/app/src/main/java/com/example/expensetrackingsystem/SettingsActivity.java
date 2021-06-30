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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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

        private EditTextPreference m_newExpenseTypePreference;

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

            // Add listener to update the DB when a preference is updated
            addListenerToOnUpdatePreference(annualIncomePreference);
            addListenerToOnUpdatePreference(maxDailyExpensePreference);
            addListenerToOnUpdatePreference(savingGoalPreference);

            // Add expense type list as preferences
            PreferenceScreen preferenceScreen = preferenceManager.getPreferenceScreen();
            PreferenceCategory expenseTypesCategory = new PreferenceCategory(getContext());
            expenseTypesCategory.setTitle(R.string.expense_types_header);
            preferenceScreen.addPreference(expenseTypesCategory);

            // Initialize preference values
            initializePreferencesValue(expenseTypesCategory, annualIncomePreference, maxDailyExpensePreference, savingGoalPreference);

            // Add the "Add new expense" preference at the end
            m_newExpenseTypePreference = buildNewExpenseTypePreference(expenseTypesCategory);
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

        private void initializePreferencesValue(PreferenceCategory expenseTypesCategory, EditTextPreference... preferences) {
            // Get the preference value from db
            final String email = mAuth.getCurrentUser().getEmail();
            db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i(TAG, "Got settings document successfully");

                        // Init info category preferences
                        for (EditTextPreference preference : preferences) {
                            final String field = PREFERENCE_KEY_TO_DB_FIELD.get(preference.getKey());
                            Log.i(TAG, String.format("key=%s, field=%s\n", preference.getKey(), field));
                            final Object value = documentSnapshot.get(field);
                            if (value != null) {
                                preference.setText(value.toString());
                            }
                        }

                        // Init expense type category preference
                        final Map<String, Boolean> expenseTypes = (Map<String, Boolean>) documentSnapshot.get("expenseTypes");
                        if (expenseTypes != null) {
                            for (String expenseType : expenseTypes.keySet()) {
                                final EditTextPreference preference = buildExpenseTypePreference(expenseType);
                                expenseTypesCategory.addPreference(preference);
                            }
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
            for (EditTextPreference preference : preferences) {
                preference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                    @Override
                    public void onBindEditText(@NonNull @NotNull EditText editText) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                });
            }
        }

        EditTextPreference buildExpenseTypePreference(String expenseType) {
            Log.i(TAG, String.format("Expense type: %s", expenseType));
            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setText(expenseType);
            preference.setPersistent(false);
            preference.setKey(expenseType);
            preference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());
            final String email = mAuth.getCurrentUser().getEmail();
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String newVal = newValue.toString();
                    final String field = "itemName";
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
            return preference;
        }

        EditTextPreference buildNewExpenseTypePreference(PreferenceCategory expenseTypesCategory) {
            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setPersistent(false);
            preference.setKey("new_expense_type");
            preference.setTitle("New Expense");
            preference.setOrder(0);
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String newVal = newValue.toString();
                    Map<String, Object> updates = new HashMap<>();
                    Map<String, Object> expenseTypes = new HashMap<>();
                    expenseTypes.put(newVal, true);
                    updates.put("expenseTypes", expenseTypes);
                    final String email = mAuth.getCurrentUser().getEmail();
                    db.collection("settings").document(email).update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i(TAG, String.format("%s is saved successfully", newVal));
                                    expenseTypesCategory.addPreference(buildExpenseTypePreference(newVal));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.i(TAG, String.format("Failed to save %s", newVal));
                                }
                            });
                    return false;
                }
            });
            expenseTypesCategory.addPreference(preference);
            return preference;
        }

    }
}