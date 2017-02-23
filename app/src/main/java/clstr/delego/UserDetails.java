package clstr.delego;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.json.Ason;
import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class UserDetails extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle b = getIntent().getExtras();
        String process_URI = b.getString("key");
        new HttpRequestTask(
                new HttpRequest(process_URI, HttpRequest.GET),
                new HttpRequest.Handler() {

                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            Ason ason = new Ason(response.body);
                            String user_name = ason.getString("name");
                            collapsingToolbar.setTitle(user_name);
                            String user_image = ason.getString("user_image");
                            ImageView user_image_view = (ImageView) findViewById(R.id.user_image);
                            UrlImageViewHelper.setUrlDrawable(user_image_view, user_image, R.drawable.mountains);
                            String user_age = "Age : " + ason.getString("user_type");
                            TextView age = (TextView) findViewById(R.id.typeView);
                            age.setText(user_age);
                            String user_type = "Type : " + ason.getString("user_age");
                            TextView type = (TextView) findViewById(R.id.ageView);
                            type.setText(user_type);
                            String user_id = "ID : " + ason.getString("user_id");
                            TextView id = (TextView) findViewById(R.id.idView);
                            id.setText(user_id);
                        } else {
                            String ai_response = "Can't reach the server at the moment. Please try again later.";
                            TextView textView = (TextView) findViewById(R.id.idView);
                            textView.setText(ai_response);
                        }
                    }
                }).execute();
    }
}
