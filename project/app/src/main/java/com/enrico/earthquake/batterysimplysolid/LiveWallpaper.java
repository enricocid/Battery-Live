package com.enrico.earthquake.batterysimplysolid;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;

import com.enrico.earthquake.batterysimplysolid.MoreSettings.Preferences;


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

        //draw shit according to battery levels and preferences
        private void draw() {

            SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {

                    //set colors from preferences
                    Preferences.resolveMode(getBaseContext(), c, color);

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


