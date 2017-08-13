package com.example.mansi.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class settings_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //adding the preference xml resource to activity_settings
            addPreferencesFromResource(R.xml.preference);

            //capturing preference by its key since preference have no id's for to be identified
            Preference changedSortPreference = findPreference(getString(R.string.list_preference_key));
            bindSummaryToValue(changedSortPreference);

            Preference changedSearchPreference = findPreference(getString(R.string.edit_preference_key));
            bindSummaryToValue(changedSearchPreference);
        }

        private void bindSummaryToValue(Preference preference) {
            //setting onPreferenceChangeListener to trigger onPreferenceChange as soon as preference is changed to update UI immediately
            preference.setOnPreferenceChangeListener(this);
            //SharedPreference is a class used for accessing and modifying preference data and here it is
            //used to read preference from xml file.
            //getDefaultSharedPreferences is used to point sharedChangedPreference to the default file that is used by changedSortPreference
            //Now after getting access to the point where file is stored we can access or modify preference data
            SharedPreferences sharedChangedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            //getString of SharedPreference class gets the default value of preference at which SharedPreference points by its key
            String preferenceSelectedValue = sharedChangedPreferences.getString(preference.getKey(), " ");
            //passing changedSortPreference Object to onPreferenceChange for updating UI with newly selected value
            onPreferenceChange(preference, preferenceSelectedValue);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String newValue = o.toString();
            //set the updated summary to this new value of String
            preference.setSummary(newValue);
            return true;
        }
    }
}
