package com.enrico.earthquake.batterysimplysolid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.WindowManager;


public class LiveWallpaper extends WallpaperService {

    int color;
    SharedPreferences prefs;

    //the livewallpaper service and engine

    @Override
    public Engine onCreateEngine() {

        //retrieve set color
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String value = prefs.getString("color", Integer.toString(ContextCompat.getColor(getApplicationContext(), R.color.defaultBattery)));

        color = Integer.parseInt(value);

        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {

        private final Handler handler = new Handler();
        int result;
        int height;
        private int width;
        private boolean visible = true;
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                draw();
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            draw();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            visible = false;
            handler.removeCallbacks(drawRunner);
        }

        //draw shit according to battery levels
        private void draw() {

            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {

                    WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

                    //retrieve display specifications
                    DisplayMetrics d = new DisplayMetrics();
                    window.getDefaultDisplay().getMetrics(d);

                    width = d.widthPixels;
                    height = d.heightPixels;
                    Paint p = new Paint();
                    Paint p2 = new Paint();

                    //get battery level
                    BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                    //do some proportions
                    int battery_complementary = height - ((batLevel * height) / 100);

                    p.setColor(Utils.getComplementaryColor(color));

                    p2.setColor(color);

                    //draw rectangles according to display height and battery level
                    c.drawRect(0, 0, width, height, p2);

                    c.drawRect(0, 0, width, battery_complementary, p);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, 100);

            }

        }

    }

}


