package com.enrico.earthquake.batterysimplysolid;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.enrico.colorpicker.colorDialog;

import java.util.ArrayList;

import static com.enrico.earthquake.batterysimplysolid.PreferenceActivity.SettingsFragment.batteryColor;
import static com.enrico.earthquake.batterysimplysolid.PreferenceActivity.SettingsFragment.chargePreference;
import static com.enrico.earthquake.batterysimplysolid.PreferenceActivity.SettingsFragment.dischargePreference;
import static com.enrico.earthquake.batterysimplysolid.PreferenceActivity.SettingsFragment.toolbarColor;

@SuppressLint("NewApi")
public class PreferenceActivity extends AppCompatActivity implements colorDialog.ColorSelectedListener {

    //ContextThemeWrapper
    ContextThemeWrapper themeWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
        themeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());

        Preferences.applyTheme(themeWrapper, getBaseContext());

        //apply light status bar icons if enabled
        Preferences.applyLightIcons(this);

        setContentView(R.layout.preference_activity);

        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //provide back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //theming
        final ViewGroup vg = (ViewGroup) getWindow().getDecorView();
        vg.postDelayed(new Runnable() {

            @Override
            public void run() {
                final ArrayList<View> views = new ArrayList<>();
                vg.findViewsWithText(views, getResources().getText(R.string.about),
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (views.isEmpty()) {
                    return;
                }

                ((ActionMenuItemView) views.get(0)).setIcon(Preferences.LightIconsEnabled(PreferenceActivity.this) ? getDrawable(R.drawable.ic_information) : getDrawable(R.drawable.ic_information_dark));
                toolbar.setTitleTextColor(Preferences.LightIconsEnabled(PreferenceActivity.this) ? Color.BLACK : Color.WHITE);
                final Drawable drawable = getResources().getDrawable(Preferences.LightIconsEnabled(PreferenceActivity.this) ? R.drawable.ic_close : R.drawable.ic_close_dark, getTheme());
                getSupportActionBar().setHomeAsUpIndicator(drawable);

            }

        }, 100);

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final int mItemId = item.getItemId();

                        switch (mItemId) {

                            //about button
                            case R.id.about:

                                //show about activity

                                Intent aboutActivity = new Intent(PreferenceActivity.this, AboutActivity.class);
                                startActivity(aboutActivity);

                                break;
                        }

                        return false;
                    }
                });

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }

    }

    //inflate the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    //close app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColorSelection(DialogFragment dialogFragment, int color) {

        int tag;

        tag = Integer.valueOf(dialogFragment.getTag());

        switch (tag) {

            case 1:

                colorDialog.setColorPreferenceSummary(chargePreference, color, PreferenceActivity.this, getResources());
                colorDialog.setPickerColor(PreferenceActivity.this, 1, color);

                break;

            case 2:

                colorDialog.setColorPreferenceSummary(dischargePreference, color, PreferenceActivity.this, getResources());
                colorDialog.setPickerColor(PreferenceActivity.this, 2, color);
                break;

            case 3:

                colorDialog.setColorPreferenceSummary(batteryColor, color, PreferenceActivity.this, getResources());
                colorDialog.setPickerColor(PreferenceActivity.this, 3, color);
                break;

            case 4:

                colorDialog.setColorPreferenceSummary(toolbarColor, color, PreferenceActivity.this, getResources());
                colorDialog.setPickerColor(PreferenceActivity.this, 4, color);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                Utils.changeToolbarColor(this, toolbar, color);

                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        //color picker preferences
        static Preference chargePreference;
        static Preference dischargePreference;
        static Preference batteryColor;
        static Preference toolbarColor;

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.info_pref);

            final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

            //get preference screen
            final PreferenceScreen screen = getPreferenceScreen();

            //get picker preferences

            //get charge color preference
            chargePreference = findPreference("chargeColor");

            //get discharge color preference
            dischargePreference = findPreference("dischargeColor");

            //battery percentage color chooser preference
            batteryColor = findPreference("batteryColor");

            //toolbar color preference
            toolbarColor = findPreference("toolbarColor");

            //get preferences colors
            int color = colorDialog.getPickerColor(getActivity(), 1);
            int color2 = colorDialog.getPickerColor(getActivity(), 2);
            int color3 = colorDialog.getPickerColor(getActivity(), 3);
            int color4 = colorDialog.getPickerColor(getActivity(), 4);

            //set preferences colors
            colorDialog.setColorPreferenceSummary(chargePreference, color, getActivity(), getResources());
            colorDialog.setColorPreferenceSummary(dischargePreference, color2, getActivity(), getResources());
            colorDialog.setColorPreferenceSummary(batteryColor, color3, getActivity(), getResources());
            colorDialog.setColorPreferenceSummary(toolbarColor, color4, getActivity(), getResources());

            //set all color pickers dialogs
            //charge color
            chargePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    colorDialog.showColorPicker(appCompatActivity, 1);
                    return false;
                }
            });

            //discharge color
            dischargePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    colorDialog.showColorPicker(appCompatActivity, 2);
                    return false;
                }
            });

            //battery color
            batteryColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    colorDialog.showColorPicker(appCompatActivity, 3);
                    return false;
                }
            });

            //toolbar color
            toolbarColor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    colorDialog.showColorPicker(appCompatActivity, 4);
                    return false;
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

            //get typeface and size options
            final Preference typefacePreference = findPreference(getActivity().getString(R.string.pref_typeface));
            final Preference sizePreference = findPreference("sizeTxt");

            //get battery percentage enabler
            Preference batteryPreference = findPreference("batteryText");

            //check if battery is enabled or not and add o remove typeface, color and size preferences
            resolveBatteryPreference(screen, typefacePreference, batteryColor, sizePreference);

            //on click battery preferences enable or remove typeface, color and size preferences
            batteryPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    resolveBatteryPreference(screen, typefacePreference, batteryColor, sizePreference);

                    return true;
                }
            });

            //initialize shared preference change listener
            //some preferences when enabled requires an app reboot
            mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                    //on theme selection restart the app
                    if (key.equals(getResources().getString(R.string.pref_theme)) | key.equals("lightColored")) {
                        getActivity().recreate();
                    }

                }
            };

            //get LightStatusBar preference
            Preference LightStatusBar = findPreference("lightColored");

            //hide this option on pre-marshmallow devices
            if (Build.VERSION.SDK_INT < 23) {

                LightStatusBar.setEnabled(false);

                LightStatusBar.setSummary(getString(R.string.message));

                //show this option on >= 23 devices
            } else {
                LightStatusBar.setEnabled(true);
            }
        }

        //register preferences changes
        @Override
        public void onResume() {
            super.onResume();

            //restore toolbar color and relative preference summary on resume
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

            int color4 = colorDialog.getPickerColor(getActivity(), 4);

            Utils.changeToolbarColor(getActivity(), toolbar, color4);

            //register preferences changes
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
        }

        //unregister preferences changes
        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
            super.onPause();
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
    }
}
