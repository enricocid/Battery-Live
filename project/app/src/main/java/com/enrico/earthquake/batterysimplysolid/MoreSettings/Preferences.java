package com.enrico.earthquake.batterysimplysolid.MoreSettings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.enrico.earthquake.batterysimplysolid.R;
import com.enrico.earthquake.batterysimplysolid.Utils;

import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;

public class Preferences {

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

    //multi-preference dialog for live wallpaper mode options
    public static void resolveMode(Context context, Canvas c, int color) {

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
                p.setColor(Utils.getComplementaryColor(color));

                p2.setColor(color);

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
}
