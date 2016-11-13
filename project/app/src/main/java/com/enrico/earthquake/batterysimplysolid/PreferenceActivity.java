package com.enrico.earthquake.batterysimplysolid;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.pavelsikun.vintagechroma.ChromaPreference;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;

@SuppressLint("NewApi")
public class PreferenceActivity extends AppCompatActivity {

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

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final int mItemId = item.getItemId();

                        switch (mItemId) {

                            //about button
                            case R.id.about:

                                //show about dialog
                                Utils.showAbout(PreferenceActivity.this);

                                break;
                        }

                        return false;
                    }
                });

        //change toolbar navigation icon and title
        Utils.changeNavigationIcon(toolbar, ContextCompat.getDrawable(this, R.drawable.ic_close));

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }

    }

    //create the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    //provide back navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.info_pref);

            //get preference screen
            final PreferenceScreen screen = getPreferenceScreen();

            //get charge color preference
            final ChromaPreference chargePreference = (ChromaPreference) findPreference("chargeColor");

            //on click save the selected color to SharedPreferences
            chargePreference.setOnColorSelectedListener(new OnColorSelectedListener() {
                @Override
                public void onColorSelected(@ColorInt int color) {

                    Utils.sendChargeColor(getActivity(), color);

                }
            });

            //get discharge color preference
            final ChromaPreference dischargePreference = (ChromaPreference) findPreference("dischargeColor");

            //on click save the selected color to SharedPreferences
            dischargePreference.setOnColorSelectedListener(new OnColorSelectedListener() {
                @Override
                public void onColorSelected(@ColorInt int color) {

                    Utils.sendDischargeColor(getActivity(), color);

                }
            });

            //get apply preference
            final Preference apply = findPreference("apply");

            //open Live Wallpaper chooser
            apply.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent intent = new Intent(
                            WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);

                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            new ComponentName(getActivity(), LiveWallpaper.class));

                    startActivity(intent);

                    return true;
                }
            });

            //battery percentage color chooser preference
            final ChromaPreference batteryColor = (ChromaPreference) findPreference("batteryColor");

            //on click save the selected color to SharedPreferences
            batteryColor.setOnColorSelectedListener(new OnColorSelectedListener() {
                @Override
                public void onColorSelected(@ColorInt int color) {
                    Utils.sendBatteryColor(getActivity(), color);
                }
            });

            //get typeface and size options
            final Preference typefacePreference = findPreference(getActivity().getString(R.string.pref_typeface));
            final Preference sizePreference = findPreference("sizeTxt");

            //get battery percentage enabler
            Preference batteryPreference = findPreference("batteryText");

            //check if battery is enabled or not and add o remove typeface, color and size preferences
            resolveBatteryPreference(screen, typefacePreference, typefacePreference, sizePreference);

            //on click battery preferences enable or remove typeface, color and size preferences
            batteryPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    resolveBatteryPreference(screen, typefacePreference, batteryColor, sizePreference);

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

            //initialize version from BuildConfig
            String version = BuildConfig.VERSION_NAME;

            //get the version preference
            Preference preferenceversion = findPreference("build_number");

            //dynamically set app's version
            preferenceversion.setSummary(version);

            //grey out version preference
            preferenceversion.setEnabled(false);

        }
    }

}
