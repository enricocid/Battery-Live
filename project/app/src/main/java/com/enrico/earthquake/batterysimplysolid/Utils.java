package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.support.v7.widget.Toolbar;

import com.enrico.colorpicker.colorDialog;


class Utils {

    //method to restore toolbar color
    static void restoreToolbarColor(Activity activity, Toolbar toolbar) {

        changeToolbarColor(activity, toolbar, colorDialog.getPickerColor(activity, 4));

    }

    //method to change toolbar color
    static void changeToolbarColor(final Activity activity, final Toolbar toolbar, final int color) {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                toolbar.setBackgroundColor(color);

                activity.getWindow().setStatusBarColor(colorDialog.shiftColor(color, 0.9f));

            }

        });
    }
}