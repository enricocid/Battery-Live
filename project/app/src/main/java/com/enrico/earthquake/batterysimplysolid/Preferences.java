package com.enrico.earthquake.batterysimplysolid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;

class Preferences {

    //multi-preference dialog for live wallpaper mode options
    static void resolveMode(Context context, Canvas c, int charge, int discharge) {

        int height;

        int width;

        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //retrieve display specifications
        DisplayMetrics d = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(d);

        width = d.widthPixels;
        height = d.heightPixels;
        Paint p = new Paint();
        Paint p2 = new Paint();

        //get battery level
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        //do some proportions
        int battery_complementary = height - ((batLevel * height) / 100);

        //Mode options
        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_mode), String.valueOf(0));

        switch (Integer.parseInt(choice)) {
            default:
            case 0:

                //set colors
                p.setColor(discharge);

                p2.setColor(charge);

                //draw rectangles according to display height and battery level
                c.drawRect(0, 0, width, height, p2);

                c.drawRect(0, 0, width, battery_complementary, p);

                break;

            case 1:

                //draw rectangle according to battery-context
                batteryModes(context, p, batLevel);

                c.drawRect(0, 0, width, height, p);

                break;

            case 2:

                //draw rectangles according to battery level and time-context
                dayModes(context, p, p2);

                c.drawRect(0, 0, width, height, p2);

                c.drawRect(0, 0, width, battery_complementary, p);
                break;
        }
    }

    //battery-context colors
    private static void batteryModes(Context context, Paint p, int batLevel) {

        if (batLevel <= 100)
            p.setColor(ContextCompat.getColor(context, R.color.materialGreen));

        if (batLevel <= 90)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightGreen));

        if (batLevel <= 80)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightestGreen));

        if (batLevel <= 70)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightestYellow));

        if (batLevel <= 60)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightYellow));

        if (batLevel <= 50)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightestOrange));

        if (batLevel <= 40)
            p.setColor(ContextCompat.getColor(context, R.color.materialLightOrange));

        if (batLevel <= 30)
            p.setColor(ContextCompat.getColor(context, R.color.materialOrange));

        if (batLevel <= 20)
            p.setColor(ContextCompat.getColor(context, R.color.materialRed));

        if (batLevel <= 15)
            p.setColor(ContextCompat.getColor(context, R.color.materialRed));
    }

    //time-context colors
    private static void dayModes(Context context, Paint p, Paint p2) {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        //sunrise
        if (hour >= 6) {
            p.setColor(ContextCompat.getColor(context, R.color.sunriseLight));
            p2.setColor(ContextCompat.getColor(context, R.color.sunrise));
        }

        //morning
        if (hour >= 9) {
            p.setColor(ContextCompat.getColor(context, R.color.morningLight));
            p2.setColor(ContextCompat.getColor(context, R.color.morning));
        }

        //zenit
        if (hour >= 11) {
            p.setColor(ContextCompat.getColor(context, R.color.zenitLight));
            p2.setColor(ContextCompat.getColor(context, R.color.zenit));
        }

        //noon
        if (hour >= 14) {
            p.setColor(ContextCompat.getColor(context, R.color.noonLight));
            p2.setColor(ContextCompat.getColor(context, R.color.noon));
        }

        //afternoon
        if (hour >= 17) {
            p.setColor(ContextCompat.getColor(context, R.color.afternoonLight));
            p2.setColor(ContextCompat.getColor(context, R.color.afternoon));
        }

        //night
        if (hour >= 21 || hour < 6) {
            p.setColor(ContextCompat.getColor(context, R.color.nightLight));
            p2.setColor(ContextCompat.getColor(context, R.color.night));
        }

    }

    //preference for the battery text
    static boolean batteryText(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("batteryText", false);
    }

    //multi-preference dialog for articles text size
    private static Typeface resolveTypeface(Context context) {

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_typeface), String.valueOf(3));
        switch (Integer.parseInt(choice)) {
            case 3:
                return Typeface.DEFAULT;
            case 4:
            default:
                return Typeface.DEFAULT_BOLD;
            case 5:
                return Typeface.MONOSPACE;
            case 6:
                return Typeface.SANS_SERIF;
            case 7:
                return Typeface.SERIF;
        }
    }

    //preference for the batteryText
    private static String textSize(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("sizeTxt", "20");
    }

    //method to draw centered text
    static void drawText(Context context, Canvas canvas, Paint paint, String text, int color) {
        final Rect textBounds = new Rect();

        paint.getTextBounds(text, 0, text.length(), textBounds);

        int width = textBounds.width();

        paint.setTypeface(resolveTypeface(context));
        paint.setAntiAlias(true);
        paint.setElegantTextHeight(true);
        paint.setTextSize(Integer.parseInt(textSize(context)) * 10);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        paint.setColor(color);

        canvas.drawText(text, (canvas.getWidth() - width) / 2, (canvas.getHeight() - paint.ascent()) / 2, paint);

    }

    //multi-preference dialog for theme options
    private static int resolveTheme(Context context) {

        //Themes options

        //light theme
        int light = R.style.AppTheme;

        //dark theme
        int dark = R.style.AppThemeDark;

        //darker theme
        int darker = R.style.AppThemeDarker;

        String choice = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_theme), String.valueOf(8));
        switch (Integer.parseInt(choice)) {
            default:
            case 8:
                return light;
            case 9:
                return dark;
            case 10:
                return darker;
        }
    }

    //method to apply selected theme
    static void applyTheme(ContextThemeWrapper contextThemeWrapper, Context context) { 
        int theme = Preferences.resolveTheme(context);
        contextThemeWrapper.setTheme(theme);

    }

    //are light icons enabled?
    public static boolean LightIconsEnabled(Context context) {
        return android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("lightColored", false);
    }

    //method to apply light status bar
    static void applyLightIcons(Activity activity) {
        if (Preferences.LightIconsEnabled(activity)) {
            if (Build.VERSION.SDK_INT >= 23) {

                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            }
        }

    }
}
