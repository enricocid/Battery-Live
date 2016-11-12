package com.enrico.earthquake.batterysimplysolid;

import android.animation.Animator;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.enrico.earthquake.batterysimplysolid.PreferenceActivity;


public class MainActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback {

    //android's Wallpaper Manager
    WallpaperManager myWallpaperManager;

    //menu items
    Menu menu;

    //battery views
    View batteryCompl;

    View batteryPerc;

    View batteryTop;

    ImageView imageView;

    //fab
    FloatingActionButton fab;

    Toolbar toolbar;

    //blink animation
    Animation animBlink;

    //create the toolbar's menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        getMenuInflater().inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //set the view
        setContentView(R.layout.home);

        //initialize the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //set the toolbar
        setSupportActionBar(toolbar);

        //set the menu's toolbar click listener
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final int mItemId = item.getItemId();

                        //share button using share intent
                        switch (mItemId) {

                            //about button
                            case R.id.about:

                                //show about dialog
                                Utils.showAbout(MainActivity.this);

                                break;

                            //settings button
                            case R.id.option:

                                //open Settings Activity
                                Intent ii = new Intent(MainActivity.this, PreferenceActivity.class);
                                startActivity(ii);

                                break;
                        }

                        return false;
                    }
                });


        //open color chooser by clicking the fab button
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openColorChooser();

            }
        });


        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.post(new Runnable() {
            @Override
            public void run() {

                //set blink animation to imageView
                animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);

                imageView.startAnimation(animBlink);

            }
        });

    }

    //open material color picker
    private void openColorChooser() {

        new ColorChooserDialog.Builder(this, R.string.color_palette)
                .allowUserColorInputAlpha(false)
                .titleSub(R.string.colors)
                .accentMode(true)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .dynamicButtonColor(true)
                .show();
    }

    //change view colors
    private void setColor(final int color) {

        fab = (FloatingActionButton) findViewById(R.id.fab);

        batteryPerc = findViewById(R.id.battery_percentage);

        batteryCompl = findViewById(R.id.battery_complementary);

        batteryTop = findViewById(R.id.battery_top);

        MainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                }
                getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
                getWindow().setNavigationBarColor(color);

                batteryPerc.setBackgroundColor(color);
                batteryCompl.setBackgroundColor(Utils.getComplementaryColor(color));
                batteryTop.setBackgroundColor(Utils.getComplementaryColor(color));
                fab.setColorFilter(Utils.getComplementaryColor(color));
                fab.setBackgroundTintList(ColorStateList.valueOf(color));

            }
        });

    }

    //change colors on app resume
    private void updateColorOnResume() {

        setColor(Utils.retrieveColor(getBaseContext(), this));

        final View shape = findViewById(R.id.circle);

        //apply light or dark shits
        Utils.isColorDark(toolbar, shape, MainActivity.this, getBaseContext(), Utils.retrieveColor(getBaseContext(), MainActivity.this));

        //set circular reveal animation
        shape.post(new Runnable() {

            @Override
            public void run() {
                // Create a reveal {@link Animator} that starts clipping the view from
                // the top left corner until the whole view is covered.

                Animator animator = ViewAnimationUtils.createCircularReveal(
                        shape,
                        0,
                        0,
                        0,
                        (float) Math.hypot(shape.getWidth(), shape.getHeight()));

                // Set a natural ease-in/ease-out interpolator.
                animator.setInterpolator(new AccelerateDecelerateInterpolator());

                // Finally start the animation
                animator.start();
            }
        });

    }

    //method to set live wallpaper
    public void setLiveWallpaper(View view) {

        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);

        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(MainActivity.this, LiveWallpaper.class));

        startActivity(intent);
    }

    //do shit on color selected
    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int color) {

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        setColor(color);

        recreate();

        Utils.sendColor(MainActivity.this, color);

    }

    //on resume activity do shit
    @Override
    public void onResume() {
        super.onResume();
        updateColorOnResume();
    }

}

