package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.pavelsikun.vintagechroma.ChromaPreference;

class Utils {

    //method to save charge color to SharedPreferences
    static void sendChargeColor(Activity activity, Integer color) {

        SharedPreferences prefs;

        prefs = activity.getSharedPreferences("primaryColor", Context.MODE_PRIVATE);

        prefs.edit()
                .clear()
                .apply();

        prefs.edit()
                .putString("bottomColor", Integer.toString(color))
                .apply();
    }

    //method to save discharge color to SharedPreferences
    static void sendDischargeColor(Activity activity, Integer color) {

        SharedPreferences prefs2;

        prefs2 = activity.getSharedPreferences("secondaryColor", Context.MODE_PRIVATE);

        prefs2.edit()
                .clear()
                .apply();

        prefs2.edit()
                .putString("topColor", Integer.toString(color))
                .apply();
    }

    //method to save battery percent color
    static void sendBatteryColor(Activity activity, Integer color) {

        SharedPreferences prefs3;

        prefs3 = activity.getSharedPreferences("batteryPercentage", Context.MODE_PRIVATE);

        prefs3.edit()
                .clear()
                .apply();

        prefs3.edit()
                .putString("batteryColor", Integer.toString(color))
                .apply();
    }

    //method to save toolbar color to SharedPreferences
    static void sendToolbarColor(Activity activity, Integer color) {

        SharedPreferences prefs4;

        prefs4 = activity.getSharedPreferences("toolbarColor", Context.MODE_PRIVATE);

        prefs4.edit()
                .clear()
                .apply();

        prefs4.edit()
                .putString("niceColor", Integer.toString(color))
                .apply();
    }


    //properly format a color from chroma preference input to hex code
    static String ColorValue(int color) {

        return String.format("#%06X", (0xFFFFFF & color));
    }

    //method to retrieve battery charge colors
    static int retrieveChargeColor(SharedPreferences prefs, Context context) {

        String charge = prefs.getString("bottomColor", Integer.toString(ContextCompat.getColor(context, R.color.defaultBattery)));

        return Integer.parseInt(charge);
    }

    //method to retrieve battery discharge colors
    static int retrieveDischargeColor(SharedPreferences prefs2, Context context) {

        String discharge = prefs2.getString("topColor", Integer.toString(ContextCompat.getColor(context, R.color.defaultBatteryComplementary)));

        return Integer.parseInt(discharge);
    }

    //method to retrieve battery text color
    static int retrieveBatteryColor(SharedPreferences prefs3, Context context) {

        String batteryperc = prefs3.getString("batteryColor", Integer.toString(ContextCompat.getColor(context, android.R.color.white)));

        return Integer.parseInt(batteryperc);

    }

    //method to retrieve battery text color
    private static int retrieveToolbarColor(SharedPreferences prefs4, Context context) {

        String color1 = prefs4.getString("niceColor", Integer.toString(ContextCompat.getColor(context, R.color.colorPrimaryDark)));

        return Integer.parseInt(color1);

    }

    //method to restore toolbar color
    static void restoreToolbarColor(Activity activity, Toolbar toolbar) {

        //retrieve toolbar color
        SharedPreferences prefs4 = activity.getSharedPreferences("toolbarColor", Context.MODE_PRIVATE);

        changeToolbarColor(activity, toolbar, retrieveToolbarColor(prefs4, activity));

    }

    //method to restore toolbar color
    static void restoreToolbarPreferenceColor(Activity activity, ChromaPreference toolbarColor) {


        //retrieve toolbar color
        SharedPreferences prefs4 = activity.getSharedPreferences("toolbarColor", Context.MODE_PRIVATE);

        //restore toolbar preference summary
        toolbarColor.setSummary(ColorValue(retrieveToolbarColor(prefs4, activity)));
    }

    //method to change toolbar color
    static void changeToolbarColor(final Activity activity, final Toolbar toolbar, final Integer color) {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                toolbar.setBackgroundColor(color);

                activity.getWindow().setStatusBarColor(shiftColor(color, 0.9f));
            }

        });
    }

    //used to shift color down a bit
    private static int shiftColor(int color, float fraction) {

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= fraction; // value component

        return Color.HSVToColor(hsv);
    }
}
