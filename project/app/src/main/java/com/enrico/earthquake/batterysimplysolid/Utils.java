package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

class Utils {

    //method to change the toolbar's navigation icon
    static void changeNavigationIcon(final Toolbar toolbar, final Drawable icon) {

        toolbar.post(new Runnable() {

            @Override
            public void run() {
                toolbar.setNavigationIcon(icon);
            }
        });
    }

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

    //show about dialog
    static void showAbout(AppCompatActivity activity) {

        AboutDialog.show(activity);

    }
}
