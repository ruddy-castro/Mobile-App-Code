package com.example.expensetrackingsystem;

import android.os.Bundle;
import android.text.InputType;
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
import java.util.Map;

/**
 * The Activity for settings, users can customize their data.
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Hook method called when the activity is spawned
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.settings, new SettingsFragment()).commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * The fragment for setting.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        //Firestore
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Preference key to the database field.
        private static final Map<String, String> PREFERENCE_KEY_TO_DB_FIELD = ImmutableMap.of(
                "annual_income", "annualIncome",
                "max_daily_expense", "maxDailyExpense",
                "saving_goal", "savingGoal"
        );

        private static Map<String, Boolean> m_expenseTypes;
        private EditTextPreference m_newExpenseTypePreference;
        PreferenceCategory m_expenseTypesCategory;

        /**
         * Create preferences.
         * @param savedInstanceState
         * @param rootKey
         */
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            m_expenseTypesCategory = new PreferenceCategory(getContext());

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

            m_expenseTypesCategory.setTitle(R.string.expense_types_header);
            preferenceScreen.addPreference(m_expenseTypesCategory);

            // Initialize preference values
            initializePreferencesValue(annualIncomePreference, maxDailyExpensePreference, savingGoalPreference);

            // Add the "Add new expense" preference at the end
            m_newExpenseTypePreference = buildNewExpenseTypePreference();
        }

        /**
         * Add listener to preferences to change the database.
         * @param preference
         */
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
                                public void onSuccess(Void unused) { }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) { }
                            });
                    return true;
                }
            });
        }

        /**
         * Initialize the values for preferences.
         * @param preferences
         */
        private void initializePreferencesValue(EditTextPreference... preferences) {

            // Get the preference value from db
            final String email = mAuth.getCurrentUser().getEmail();
            db.collection("settings").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // Init info category preferences
                        for (EditTextPreference preference : preferences) {
                            final String field = PREFERENCE_KEY_TO_DB_FIELD.get(preference.getKey());
                            final Object value = documentSnapshot.get(field);
                            if (value != null) {
                                preference.setText(value.toString());
                            }
                        }

                        // Init expense type category preference
                        m_expenseTypes = (Map<String, Boolean>) documentSnapshot.get("expenseTypes");
                        m_expenseTypes = m_expenseTypes == null ? new HashMap<>() : m_expenseTypes;
                        for (String expenseType : m_expenseTypes.keySet()) {
                            final EditTextPreference preference = buildExpenseTypePreference(expenseType);
                            m_expenseTypesCategory.addPreference(preference);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) { }
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

        /**
         * Edit/remove more expense type.
         * @param expenseType
         * @return
         */
        EditTextPreference buildExpenseTypePreference(String expenseType) {

            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setText(expenseType);
            preference.setPersistent(false);
            preference.setKey(expenseType);
            preference.setSummaryProvider(EditTextPreference.SimpleSummaryProvider.getInstance());

            // Get the email of the current user
            final String email = mAuth.getCurrentUser().getEmail();
            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String prevVal = ((EditTextPreference) preference).getText();
                    final String newVal = newValue.toString();
                    Map<String, Object> updates = new HashMap<>();

                    // Remove the preference if the input is empty
                    m_expenseTypes.remove(prevVal);
                    if (newVal.isEmpty()) {
                        m_expenseTypesCategory.removePreference(preference);
                    } else {
                        m_expenseTypes.put(newVal, true);
                    }
                    updates.put("expenseTypes", m_expenseTypes);

                    // Update the database.
                    db.collection("settings").document(email).update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) { }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) { }
                            });
                    return true;
                }
            });
            return preference;
        }

        /**
         * Add a new expense type.
         * @return
         */
        EditTextPreference buildNewExpenseTypePreference() {

            final EditTextPreference preference = new EditTextPreference(getContext());
            preference.setPersistent(false);
            preference.setKey("new_expense_type");
            preference.setTitle("New Expense");
            preference.setOrder(0);

            preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    final String newVal = newValue.toString();
                    if (newVal.isEmpty()) { return false; }
                    Map<String, Object> updates = new HashMap<>();
                    m_expenseTypes.put(newVal, true);
                    updates.put("expenseTypes", m_expenseTypes);

                    // get the email of the current user
                    final String email = mAuth.getCurrentUser().getEmail();

                    // update the database with ne type
                    db.collection("settings").document(email).update(updates)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    m_expenseTypesCategory.addPreference(buildExpenseTypePreference(newVal));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) { }
                            });
                    return false;
                }
            });

            // Add preference
            m_expenseTypesCategory.addPreference(preference);
            return preference;
        }

    }
}