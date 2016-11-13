package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.color.CircleView;

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

    //method to restore toolbar color
    static void restoreToolbarColor(Activity activity, Toolbar toolbar) {


        //retrieve toolbar color
        SharedPreferences prefs4 = activity.getSharedPreferences("toolbarColor", Context.MODE_PRIVATE);

        String color1 = prefs4.getString("niceColor", Integer.toString(ContextCompat.getColor(activity, R.color.colorPrimaryDark)));

        int color = Integer.parseInt(color1);

        changeToolbarColor(activity, toolbar, color);
    }

    //method to change toolbar color
    static void changeToolbarColor(final Activity activity, final Toolbar toolbar, final Integer color) {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                toolbar.setBackgroundColor(color);

                activity.getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            }

        });
    }

    //show about dialog
    static void showAbout(AppCompatActivity activity) {

        AboutDialog.show(activity);

    }
}
