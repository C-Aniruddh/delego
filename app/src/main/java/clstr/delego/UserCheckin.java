package clstr.delego;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.json.Ason;
import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserCheckin extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        String process_URI = b.getString("key");
        enableFullscreen();
        setFab(getResources().getColor(R.color.colorAccent), R.drawable.check, userArrival);

        new HttpRequestTask(
                new HttpRequest(process_URI, HttpRequest.GET),
                new HttpRequest.Handler() {

                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            Ason ason = new Ason(response.body);
                            String user_name = ason.getString("Name");
                            setTitle(user_name);
                            String user_image = ason.getString("Image");
                            Log.d("TAG", user_image);
                            setUserImage(user_image);
                            String user_committee= ason.getString("Committee");
                            TextView committee = (TextView) findViewById(R.id.committeeView);
                            committee.setText(user_committee);
                            String user_type = ason.getString("Country");
                            TextView type = (TextView) findViewById(R.id.countryView);
                            type.setText(user_type);
                            String user_id = ason.getString("Numid");
                            TextView id = (TextView) findViewById(R.id.idView);
                            id.setText(user_id);
                            String user_role = ason.getString("Role");
                            TextView role = (TextView) findViewById(R.id.roleView);
                            role.setText(user_role);
                        } else {
                            String ai_response = "Can't reach the server at the moment. Please try again later.";
                            TextView textView = (TextView) findViewById(R.id.idView);
                            textView.setText(ai_response);
                        }
                    }
                }).execute();
        setContent(R.layout.content_user_checkin);
    }
    public void setUserImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        setImage(resource);
                    }
                });
    }
    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }

    private View.OnClickListener userArrival = new View.OnClickListener() {
        public void onClick(View v) {
            Bundle b = getIntent().getExtras();
            String arrival_URI = b.getString("user_arrival");
            new HttpRequestTask(
                    new HttpRequest(arrival_URI, HttpRequest.GET),
                    new HttpRequest.Handler() {

                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                String server_response = "The delegate has arrived!";
                                Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                fab.setBackgroundColor(Color.parseColor("#2E7D32"));


                            } else {
                                String server_response = "Can't reach the server at the moment. Please try again later.";
                                Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }).execute();

        }
    };


}