package clstr.delego;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import org.w3c.dom.Text;

public class NotificationView extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        String content = b.getString("content");
        String image_uri = b.getString("image");

        setPrimaryColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        setContent(R.layout.activity_notification_view);
        setTitle(title);
        TextView contentText  = (TextView) findViewById(R.id.notificationContent);
        contentText.setText(content);
        setImage(R.drawable.logo);
    }
    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }

    /*public void setNotificationImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        setImage(resource);
                    }
                });
    }*/

}

