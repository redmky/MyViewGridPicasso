package com.redmky.myviewgridpicasso;

/**
 * Created by redmky on 10/18/2015.
 */
public class SettingsActivity extends android.preference.PreferenceActivity
        implements android.preference.Preference.OnPreferenceChangeListener {


    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        // Add a button to the header list.
        addPreferencesFromResource(R.xml.pref_general);


        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        // TODO: Add preferences
        bindPreferenceSummaryToValue(findPreference(getString(com.redmky.myviewgridpicasso.R.string.pref_movie_key)));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(android.preference.Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                android.preference.PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ((android.preference.ListPreference) preference).getValue()));
    }

    @Override
    public boolean onPreferenceChange(android.preference.Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof android.preference.ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            android.preference.ListPreference listPreference = (android.preference.ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                if (((android.preference.ListPreference) preference).getValue() != stringValue) {
                    //call to get movie data
                    com.redmky.myviewgridpicasso.MainActivity.getMovieData(stringValue);
                }

                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}
