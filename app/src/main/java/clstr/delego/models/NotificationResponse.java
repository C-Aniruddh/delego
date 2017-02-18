package clstr.delego.models;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class NotificationResponse {
    private List<Notification> notification = new ArrayList<Notification>();

    public List<Notification> getAndroid() {

        return notification;
    }
}
