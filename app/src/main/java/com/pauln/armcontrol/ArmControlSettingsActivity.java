package com.pauln.armcontrol;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import java.util.ArrayList;

public class ArmControlSettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";
    public static final String STREAM_KEY = "stream";

    private String[] mPreferenceKeys = {HOST_KEY, PORT_KEY, STREAM_KEY};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.armcontrolsettings);
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();

        // Load the existing preferences into teh summary screen
        ArrayList<Preference> mPreferences = new ArrayList<Preference>();
        for (String prefKey : mPreferenceKeys) {
            Preference pref = (Preference) getPreferenceManager().findPreference(prefKey);
            mPreferences.add(pref);
            onSharedPreferenceChanged(prefs, prefKey);
        }}
    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        Preference pref = findPreference(key);

        // Reset the summary text with the new value
        if (pref instanceof EditTextPreference) {
            EditTextPreference prefText = (EditTextPreference) pref;
            prefText.setSummary(prefText.getText());
        }
    }
}
