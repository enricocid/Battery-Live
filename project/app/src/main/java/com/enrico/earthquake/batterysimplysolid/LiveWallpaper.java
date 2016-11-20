package com.enrico.earthquake.batterysimplysolid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;


public class LiveWallpaper extends WallpaperService {

    //get all customizable colors
    int chargecolor;

    int dischargecolor;

    int batterycolor;

    //shared preferences
    SharedPreferences prefs;

    SharedPreferences prefs2;

    SharedPreferences prefs3;

    Rect rect;

    Paint paint;

    //the livewallpaper service and engine

    @Override
    public Engine onCreateEngine() {


        //retrieve charge color
        prefs = this.getSharedPreferences("primaryColor", Context.MODE_PRIVATE);

        chargecolor = Utils.retrieveChargeColor(prefs, getBaseContext());

        //retrieve discharge color

        prefs2 = this.getSharedPreferences("secondaryColor", Context.MODE_PRIVATE);

        dischargecolor = Utils.retrieveDischargeColor(prefs2, getBaseContext());

        //retrieve battery percentage color

        prefs3 = this.getSharedPreferences("batteryPercentage", Context.MODE_PRIVATE);

        batterycolor = Utils.retrieveBatteryColor(prefs3, getBaseContext());

        //allocate rect and paint
        rect = new Rect();
        paint = new Paint();

        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {

        private final Handler handler = new Handler();
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

                handler.post(drawRunner);
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

        //draw shit according to battery levels and preferences
        private void draw() {

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    //set colors from preferences
                    Preferences.resolveMode(getBaseContext(), canvas, chargecolor, dischargecolor);

                    //get battery level
                    BatteryManager bm = (BatteryManager) getBaseContext().getSystemService(BATTERY_SERVICE);
                    int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

                    if (Preferences.batteryText(getBaseContext()))
                        Preferences.drawText(getBaseContext(), paint, canvas, Integer.toString(batLevel), batterycolor, rect);
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, 100);

            }

        }

    }

}


