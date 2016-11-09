package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

class Utils {

    //set the solid color
    static void recreate(Activity activity) {

        activity.recreate();
    }

    //function to invert color value
    static int getComplementaryColor(int colorToInvert) {

        float r = Color.red(colorToInvert);
        float g = Color.green(colorToInvert);
        float b = Color.blue(colorToInvert);

        float newr = 255 - r;
        float newg = 255 - g;
        float newb = 255 - b;

        int red = Math.round(newr);

        int green = Math.round(newg);

        int blue = Math.round(newb);

        return android.graphics.Color.argb(255, red, green, blue);
    }

    //method to lighten colors
    private static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    //method to change the toolbar's title color
    private static void changeTitleColor(Toolbar toolbar, int color) {

        toolbar.setTitleTextColor(color);

    }

    //method to change the toolbar's overflow icon
    private static void changeOverflowIcon(Toolbar toolbar, Drawable icon) {

        toolbar.setOverflowIcon(icon);

    }

    //method to change the circular reveal view background
    private static void changeBgColor(View view, int color) {

        view.setBackground(new ColorDrawable(color));

    }

    //method to save one color to SharedPreferences
    static void sendColor(Activity activity, Integer color) {

        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        prefs.edit()
                .clear()
                .apply();

        prefs.edit()
                .putString("color", Integer.toString(color))
                .apply();
    }

    //method to retrieve the saved color
    static int retrieveColor(Context context, Activity activity) {

        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        String value = prefs.getString("color", Integer.toString(ContextCompat.getColor(context, R.color.defaultBattery)));

        return Integer.parseInt(value);
    }

    //method to apply light status bar
    private static void applyLightIcons(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {

            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }

    //determine if solid color is light or dark to apply proper colors to toolbar and statusbar
    static boolean isColorDark(Toolbar toolbar, View view, Activity activity, Context context, int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        if (darkness < 0.5) {

            applyLightIcons(activity);

            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));

            changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.secondary_text_light));

            changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots_dark));

            changeBgColor(view, lighten(getComplementaryColor(color), 0.5));

        } else {

            changeTitleColor(toolbar, ContextCompat.getColor(context, android.R.color.white));

            changeOverflowIcon(toolbar, ContextCompat.getDrawable(activity, R.drawable.ic_dots));

            activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

            changeBgColor(view, lighten(getComplementaryColor(color), 0.25));
        }
        return true;
    }

    //show about dialog
    static void showAbout(AppCompatActivity activity) {

        AboutDialog.show(activity);

    }
}
