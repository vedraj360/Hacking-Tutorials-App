package innovatives.hackingtutorials;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import me.drakeet.materialdialog.MaterialDialog;

import static innovatives.hackingtutorials.R.string.app_name;


public class MainActivity extends AppCompatActivity {

    private static final String URLs = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1XYIXIFzTkMymAWababY-K0HQXqfLU27ptqrIhbzPXuo&sheet=Sheet1";
    private static String appPackageName = "innovatives.hackingtutorials";
    //  private static final String url = "https://script.google.com/macros/s/AKfycbygukdW3tt8sCPcFDlkMnMuNu9bH5fpt7bKV50p2bM/exec?id=1fVNiNH8-QVgf4sCwIy1g_P__tqOyqf7zHBdAvKOGRQk&sheet=Sheet1";
    List<Sheet1> listsheet;
    Adapter adapter;
    ProgressWheel progressWheel;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView shimerText;
    RecyclerView recyclerView;
    CircleRefreshLayout circleRefreshLayout;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    ImageView imageView;
    InterstitialAd interstitialAd;
    FancyAlertDialog.Builder fancyAlertDialog;
    boolean check = true;
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT < 21) {
            navigationView.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            Utils.darkenStatusBar(this, R.color.black);
            actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, app_name, app_name);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

        }


        coordinatorLayout = findViewById(R.id.coordinator);
        navigationView = findViewById(R.id.navigation_view);
        Navigation();


        circleRefreshLayout = findViewById(R.id.refresh_layout);
        shimerText = findViewById(R.id.shimertex);
        progressWheel = new ProgressWheel(getApplicationContext());
        progressWheel = findViewById(R.id.progress_wheel);
        recyclerView = findViewById(R.id.infolist);

        imageView = findViewById(R.id.imageView);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Dividerclass(this));
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setAddDuration(5000);
        animator.setRemoveDuration(0);
        recyclerView.setItemAnimator(animator);
        progressWheel.spin();


        listsheet = new ArrayList<>();

        JsonRequest();

        circleRefreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void refreshing() {
                Toast.makeText(MainActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                if (!isConnected()) {
                    // Toast.makeText(MainActivity.this, "requests" + isConnected(), Toast.LENGTH_SHORT).show();
                    snack();
                } else {
                    //  Toast.makeText(MainActivity.this, "request" + isConnected(), Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                    JsonRequests();
                }
            }
        });


        interstitialAd = new InterstitialAd(this);
        // interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        Random random = new Random();
        int value = random.nextInt(4);
        if (value % 2 == 0) {
            interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/5729761416000");
        } else {
            interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/52589881220000");

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


    }

    @Override
    protected void onStart() {
        super.onStart();

        shimmerFrameLayout = findViewById(R.id.shimmerlayout);
        shimmerFrameLayout.startShimmerAnimation();
        if (!isConnected()) {
            shimmerFrameLayout.stopShimmerAnimation();
            shimerText.setVisibility(View.INVISIBLE);
            shimmerFrameLayout.setVisibility(View.INVISIBLE);
            progressWheel.stopSpinning();
            // Toast.makeText(this, "Not", Toast.LENGTH_SHORT).show();
            snackc();
        }


    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }

    }


    private void JsonRequest() {
        StringRequest stringRequest = new StringRequest(URLs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                shimmerFrameLayout.stopShimmerAnimation();
                shimerText.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                toolbar.setVisibility(View.VISIBLE);
                circleRefreshLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT > 21) {
                    Utils.darkenStatusBar(MainActivity.this, R.color.materialcolor);
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("Sheet1");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sheetobj = jsonArray.getJSONObject(i);
                        Sheet1 sheet1 = new Sheet1(sheetobj.getString("Title"), sheetobj.getString("Link"), sheetobj.getInt("Pos"));
                        listsheet.add(sheet1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new Adapter(MainActivity.this, listsheet, fancyAlertDialog);
                AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
                ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
                scaleInAnimationAdapter.setDuration(1000);
                recyclerView.setAdapter(scaleInAnimationAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimerText.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                progressWheel.stopSpinning();
                snackc();

            }
        });


        fancyAlertDialog = new FancyAlertDialog.Builder(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void JsonRequests() {
        toolbar.setVisibility(View.VISIBLE);
        circleRefreshLayout.setVisibility(View.VISIBLE);
        StringRequest stringRequests = new StringRequest(URLs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                imageView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                listsheet.clear();


                try {
                    JSONObject objs = new JSONObject(response);
                    JSONArray jsonArray = objs.getJSONArray("Sheet1");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject sheetobjs = jsonArray.getJSONObject(i);
                        Sheet1 sheets1 = new Sheet1(sheetobjs.getString("Title"), sheetobjs.getString("Link"), sheetobjs.getInt("Pos"));
                        listsheet.add(sheets1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new Adapter(MainActivity.this, listsheet, fancyAlertDialog);
                AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
                ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(alphaInAnimationAdapter);
                scaleInAnimationAdapter.setDuration(500);
                recyclerView.setAdapter(scaleInAnimationAdapter);
                // recyclerView.setAdapter(adapter);
                recyclerView.invalidate();
                adapter.notifyDataSetChanged();
                circleRefreshLayout.finishRefreshing();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                circleRefreshLayout.finishRefreshing();
                snackc();
            }

        });


        RequestQueue requestQueues = Volley.newRequestQueue(this);
        requestQueues.add(stringRequests);

    }


    private Boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void snackc() {

        coordinatorLayout = findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please Check Your Connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonRequests();
            }
        }).setActionTextColor(Color.WHITE);
        View svk = snackbar.getView();
        svk.setBackgroundColor(Color.BLACK);
        TextView t = svk.findViewById(android.support.design.R.id.snackbar_text);
        t.setText("Please Check Your Connection");
        t.setTextColor(Color.WHITE);
        snackbar.show();

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    public void snack() {

        coordinatorLayout = findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    //recyclerView.setVisibility(View.VISIBLE);
                    JsonRequests();
                } else {
                    snack();
                }

            }
        }).setActionTextColor(Color.WHITE);
        View svk = snackbar.getView();
        svk.setBackgroundColor(Color.BLACK);
        TextView t = svk.findViewById(android.support.design.R.id.snackbar_text);
        t.setText("Please Check Your Connection");
        t.setTextColor(Color.WHITE);
        snackbar.show();
        imageView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        circleRefreshLayout.finishRefreshing();
    }

    private void Navigation() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {

                    case R.id.nav_Home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(3, true);
                        break;
                    case R.id.rate:
                        googlePlat();
                        drawerLayout.closeDrawer(3, true);
                        break;

                    case R.id.share:
                        share();
                        break;


                    case R.id.about_us:
                        final MaterialDialog materialDialog = new MaterialDialog(MainActivity.this);
                        materialDialog.setTitle("About Us").
                                setMessage("We Are Extreme Coders and Developers").
                                setPositiveButton("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        materialDialog.dismiss();
                                        drawerLayout.closeDrawer(3, true);
                                    }
                                }).show();
                }


                return false;
            }
        });

    }


    private void googlePlat() {
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");

            startActivity(appStoreIntent);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void share() {
        PackageManager p = getPackageManager();
        try {
            PackageInfo info = p.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            Intent S = new Intent();
            S.setAction(Intent.ACTION_SEND);
            S.putExtra(Intent.EXTRA_TEXT, "Wanna be a hacker !!,learn Hacking like a Pro ,So just hit the link, and start your journey... https://play.google.com/store/apps/details?id=" + appPackageName);
            S.setType("text/plain");
            S.setPackage("com.whatsapp");
            startActivity(S);
        } catch (PackageManager.NameNotFoundException e) {

            Toast.makeText(getApplicationContext(), "Please Install Whats App", Toast.LENGTH_SHORT).show();
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


