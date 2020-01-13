package innovatives.hackingtutorials;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.Random;

public class webViewActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;
    int c = 0;
    boolean check = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1361359682897035/4720526299");

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        // interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        Random random = new Random();
        int value = random.nextInt(4);
        if (value % 2 == 0) {
            interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/5729761416");
        } else {
            interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/5258988122");

        }
        final AdRequest.Builder request = new AdRequest.Builder();
        interstitialAd.loadAd(request.build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(request.build());
            }

        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check) {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                   // c++;
                   // Toast.makeText(webViewActivity.this, " " + c, Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this, 10000);


            }
        }, 1000);


        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new webview());
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);


        webView.loadUrl(link);

        // Toast.makeText(this, "" + link, Toast.LENGTH_SHORT).show();


    }

    private class webview extends WebViewClient {


        ProgressWheel progressWheel = new ProgressWheel(getApplicationContext());


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            progressWheel = findViewById(R.id.progress);
            progressWheel.spin();

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressWheel = findViewById(R.id.progress);
            progressWheel.stopSpinning();
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        check = false;
        Log.d("ads", "" + check);
    }

    @Override
    protected void onResume() {
        super.onResume();
        check = true;
        Log.d("ads", "" + check);
    }
}
