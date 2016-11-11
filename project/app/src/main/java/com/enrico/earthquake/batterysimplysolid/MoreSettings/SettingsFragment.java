package com.enrico.earthquake.batterysimplysolid.MoreSettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.enrico.earthquake.batterysimplysolid.BuildConfig;
import com.enrico.earthquake.batterysimplysolid.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //initialize version from BuildConfig
    String version = BuildConfig.VERSION_NAME;

    //get the version preference
    android.support.v7.preference.Preference preferenceversion = findPreference("build_number");

    //dynamically set app's version
    preferenceversion.setSummary(version);

    //grey out version preference
    preferenceversion.setEnabled(false);

    }

    //add preferences from xml
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.info_pref);
    }

    //register preferences changes
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
    }

    //unregister preferences changes
    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
        super.onPause();
    }
}
