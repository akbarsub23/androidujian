package com.smkn1gempol.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {

    private ImageView logoImage;
    private TextView titleText, subtitleText, taglineText;
    private Button btnEnter, btnExit;
    private View particleOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full screen immersive
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_splash);

        logoImage    = findViewById(R.id.logoImage);
        titleText    = findViewById(R.id.titleText);
        subtitleText = findViewById(R.id.subtitleText);
        taglineText  = findViewById(R.id.taglineText);
        btnEnter     = findViewById(R.id.btnEnter);
        btnExit      = findViewById(R.id.btnExit);

        startAnimations();

        btnEnter.setOnClickListener(v -> {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation a) {}
                public void onAnimationRepeat(Animation a) {}
                public void onAnimationEnd(Animation a) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            btnEnter.startAnimation(fadeOut);
        });

        btnExit.setOnClickListener(v -> showExitDialog());
    }

    private void startAnimations() {
        // Logo: bounce in
        Animation logoBounce = AnimationUtils.loadAnimation(this, R.anim.logo_bounce);
        logoImage.startAnimation(logoBounce);

        // Title: slide from left after 600ms
        new Handler().postDelayed(() -> {
            Animation slideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left_fade);
            titleText.setVisibility(View.VISIBLE);
            titleText.startAnimation(slideLeft);
        }, 600);

        // Subtitle: slide from right after 900ms
        new Handler().postDelayed(() -> {
            Animation slideRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_fade);
            subtitleText.setVisibility(View.VISIBLE);
            subtitleText.startAnimation(slideRight);
        }, 900);

        // Tagline: fade up after 1200ms
        new Handler().postDelayed(() -> {
            Animation fadeUp = AnimationUtils.loadAnimation(this, R.anim.fade_up);
            taglineText.setVisibility(View.VISIBLE);
            taglineText.startAnimation(fadeUp);
        }, 1200);

        // Buttons: fade in after 1600ms
        new Handler().postDelayed(() -> {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_up);
            btnEnter.setVisibility(View.VISIBLE);
            btnExit.setVisibility(View.VISIBLE);
            btnEnter.startAnimation(fadeIn);
            btnExit.startAnimation(fadeIn);
        }, 1600);

        // Logo pulse loop after initial anim
        new Handler().postDelayed(() -> {
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.logo_pulse);
            logoImage.startAnimation(pulse);
        }, 2200);
    }

    private void showExitDialog() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Keluar Aplikasi")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi SMKN 1 GEMPOL?")
            .setPositiveButton("Ya, Keluar", (dialog, which) -> {
                finishAffinity();
                System.exit(0);
            })
            .setNegativeButton("Batal", null)
            .show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}
