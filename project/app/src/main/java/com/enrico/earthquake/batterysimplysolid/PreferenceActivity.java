package com.enrico.earthquake.batterysimplysolid;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enrico.earthquake.batterysimplysolid.BuildConfig;
import com.enrico.earthquake.batterysimplysolid.LiveWallpaper;
import com.enrico.earthquake.batterysimplysolid.R;

@SuppressLint("NewApi")
public class PreferenceActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_activity);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, 0);
            }
        });

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }
    }

    public void openColorChooser() {
        new ColorChooserDialog.Builder(PreferenceActivity.this, R.string.color_palette)
                .allowUserColorInputAlpha(false)
                .titleSub(R.string.colors)
                .accentMode(true)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //do shit on color selected
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {

        Preferences.sendBatteryColor(PreferenceActivity.this, color);

        //apply wallpaper
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);

        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(PreferenceActivity.this, LiveWallpaper.class));

        startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.info_pref);

            //initialize version from BuildConfig
            String version = BuildConfig.VERSION_NAME;

            //get the version preference
            Preference preferenceversion = findPreference("build_number");

            //dynamically set app's version
            preferenceversion.setSummary(version);

            //grey out version preference
            preferenceversion.setEnabled(false);

            //get the color preference
            final Preference colorPreference = findPreference("color");

            colorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    //open the color picker
                    ((PreferenceActivity) getActivity()).openColorChooser();

                    return true;
                }
            });

            //get preference screen
            final PreferenceScreen screen = getPreferenceScreen();

            //get typeface and size options
            final Preference typefacePreference = findPreference(getActivity().getString(R.string.pref_typeface));
            final Preference sizePreference = findPreference("sizeTxt");

            //get battery percentage enabler
            Preference batteryPreference = findPreference("batteryText");

            //check if battery is enabled or not and add o remove typeface, color and size preferences
            resolveBatteryPreference(screen, typefacePreference, colorPreference, sizePreference);

            //on click battery preferences enable or remove typeface, color and size preferences
            batteryPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    resolveBatteryPreference(screen, typefacePreference, colorPreference, sizePreference);

                    return true;
                }
            });
        }

        //method to add or remove typeface, color and size preferences
        private void resolveBatteryPreference(PreferenceScreen screen, Preference typefacePreference, Preference colorPreference, Preference sizePreference) {

            if (Preferences.batteryText(getActivity())) {

                screen.addPreference(typefacePreference);
                screen.addPreference(colorPreference);
                screen.addPreference(sizePreference);

            } else {
                screen.removePreference(typefacePreference);
                screen.removePreference(colorPreference);
                screen.removePreference(sizePreference);
            }
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
}