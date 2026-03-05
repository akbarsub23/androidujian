package com.smkn1gempol.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends Activity {

    private static final String PRIMARY_URL   = "https://lms.semakinpol.my.id";
    private static final String FALLBACK_URL  = "http://192.168.1.100";

    private WebView webView;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private TextView errorText;
    private android.widget.Button btnRetry, btnExit;
    private ImageButton btnRefresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean loadingPrimary = true;
    private boolean hasError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        webView            = findViewById(R.id.webView);
        progressBar        = findViewById(R.id.progressBar);
        errorLayout        = findViewById(R.id.errorLayout);
        errorText          = findViewById(R.id.errorText);
        btnRetry           = findViewById(R.id.btnRetry);
        btnExit            = findViewById(R.id.btnExit);
        btnRefresh         = findViewById(R.id.btnRefresh);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        setupWebView();
        setupSwipeRefresh();

        btnRetry.setOnClickListener(v -> loadUrl());
        btnExit.setOnClickListener(v -> showExitDialog());
        btnRefresh.setOnClickListener(v -> {
            v.animate().rotation(v.getRotation() + 360f).setDuration(500).start();
            refreshPage();
        });

        loadUrl();
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeColors(0xFF4DBAFF, 0xFF1A5299, 0xFFA8D8F0);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF0D2137);
        swipeRefreshLayout.setOnRefreshListener(this::refreshPage);
    }

    private void refreshPage() {
        hasError = false;
        errorLayout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        if (webView.getUrl() != null && !webView.getUrl().isEmpty()) {
            webView.reload();
        } else {
            loadUrl();
        }
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setUserAgentString(settings.getUserAgentString() + " SMKN1GempolApp/1.0");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                webView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                hasError = false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    hasError = true;
                    swipeRefreshLayout.setRefreshing(false);
                    if (loadingPrimary) {
                        loadingPrimary = false;
                        new Handler().postDelayed(() -> webView.loadUrl(FALLBACK_URL), 500);
                    } else {
                        showError("Tidak dapat terhubung ke server.\n\n"
                            + "• " + PRIMARY_URL + "\n• " + FALLBACK_URL
                            + "\n\nTarik layar ke bawah atau tekan \uD83D\uDD04 untuk coba lagi.");
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
    }

    private void loadUrl() {
        loadingPrimary = true;
        hasError = false;
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        if (isNetworkAvailable()) {
            webView.loadUrl(PRIMARY_URL);
        } else {
            loadingPrimary = false;
            webView.loadUrl(FALLBACK_URL);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void showError(String message) {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            webView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorText.setText(message);
        });
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Keluar Aplikasi")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi SMKN 1 GEMPOL?")
            .setPositiveButton("Ya, Keluar", (dialog, which) -> { finishAffinity(); System.exit(0); })
            .setNegativeButton("Batal", null)
            .show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Handler().postDelayed(() -> {
            android.app.ActivityManager am = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), 0);
        }, 300);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasError) loadUrl();
    }
}
