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
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.expensetrackingsystem.model.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

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

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Extract preferences
            PreferenceManager preferenceManager = getPreferenceManager();
            EditTextPreference annualIncomePreference = (EditTextPreference) preferenceManager.findPreference("annual_income");
            EditTextPreference maxDailyExpensePreference = (EditTextPreference) preferenceManager.findPreference("max_daily_expense");
            EditTextPreference savingGoalPreference = (EditTextPreference) preferenceManager.findPreference("saving_goal");

            // Make preferences input type to be numeric
            makeEditTextPreferenceNumeric(annualIncomePreference);
            makeEditTextPreferenceNumeric(maxDailyExpensePreference);
            makeEditTextPreferenceNumeric(savingGoalPreference);

            // Add listener to update the DB when a preference is updated
            addListenerToOnUpdatePreference(annualIncomePreference, "annualIncome");
            addListenerToOnUpdatePreference(maxDailyExpensePreference, "maxDailyExpense");
            addListenerToOnUpdatePreference(savingGoalPreference, "savingGoal");
        }

        private void addListenerToOnUpdatePreference(Preference preference, String field) {
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String email = mAuth.getCurrentUser().getEmail();
                    final Double newVal = Double.parseDouble(newValue.toString());
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
                    return false;
                }
            });
        }

        private void makeEditTextPreferenceNumeric(EditTextPreference preference) {
            preference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull @NotNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }


    }
}