package clstr.delego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NotificationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        TextView notifContent = (TextView) findViewById(R.id.notificationContent);
        TextView notifTitle = (TextView) findViewById(R.id.notificationTitle);
        Bundle b = getIntent().getExtras();
        String title = b.getString("title");
        String content = b.getString("content");
        notifContent.setText(content);
        notifTitle.setText(title);


    }
}
