package innovatives.hackingtutorials;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import osmandroid.project_basics.Task;

/**
 * Created by VedX&Div on 10/23/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {


    StoragePref storagePref;
    private Context context;
    private List<Sheet1> data;
    private InterstitialAd interstitialAd;
    FancyAlertDialog.Builder fancyAlertDialog;
    android.os.Handler handler;
    // private static int count = 0;
    private static int count;


    public Adapter(Context context, List<Sheet1> data, FancyAlertDialog.Builder fancyAlertDialog) {

        this.context = context;
        this.data = data;
        this.fancyAlertDialog = fancyAlertDialog;

    }


    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.infolayout, parent, false);
        return new AdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {

        final Sheet1 sheetlist = data.get(position);
        holder.title.setText(sheetlist.getTitle());
        holder.link.setText(sheetlist.getLink());
        Glide.with(context).load(R.drawable.icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.circleImageView);

        storagePref = new StoragePref(context);

        count = storagePref.getCount();

        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-1361359682897035/73675813");
//        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = sheetlist.getPos();
                Toast.makeText(context, "" + pos, Toast.LENGTH_SHORT).show();
                if (pos == 1) {
                    handlerfun();
                    if (count < 11) {
                        ButtonClick(sheetlist);
                        //   Toast.makeText(context, "" + count, Toast.LENGTH_SHORT).show();
                    }
                    if (count > 11) {
                        if (storagePref.isFirstTimeLaunch()) {
                            storagePref.launchHomeScreen(false);
                            storagePref.setCount(count);
                            ButtonClick(sheetlist);
                        } else {
                            handler.removeMessages(0);
                            Intent intent = new Intent(context, webViewActivity.class);
                            intent.putExtra("link", sheetlist.getLink());
                            context.startActivity(intent);

                            // Toast.makeText(context, "" + count, Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    Intent intent = new Intent(context, webViewActivity.class);
                    intent.putExtra("link", sheetlist.getLink());
                    context.startActivity(intent);
                    //  Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView title, link;
        CircleImageView circleImageView;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            link = itemView.findViewById(R.id.link);
            circleImageView = itemView.findViewById(R.id.image);

        }
    }

    public void ButtonClick(final Sheet1 sh) {
        fancyAlertDialog.setTitle("Rate Us!")
                .setBackgroundColor(Color.parseColor("#e9678cb1"))  //Don't pass R.color.colorvalue
                .setMessage("To Access Premium Section of this App")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground(Color.parseColor("#e9678cb1"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Rate")
                .setNegativeBtnBackground(Color.parseColor("#e9678cb1"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Task.RateApp(context, "innovatives.hackingtutorials");
                        if (count > 11) {
                            if (storagePref.isFirstTimeLaunch()) {
                                storagePref.launchHomeScreen(false);
                                storagePref.setCount(count);
                            } else {

                                Intent intent = new Intent(context, webViewActivity.class);
                                intent.putExtra("link", sh.getLink());
                                context.startActivity(intent);

                                // Toast.makeText(context, "" + count, Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        storagePref.launchHomeScreen(true);
                    }
                })
                .build();
    }

    private void handlerfun() {
        handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count++;
                handler.postDelayed(this, 4000);
            }
        }, 1000);

    }


}
