package clstr.delego;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import clstr.delego.models.Delegate;
import clstr.delego.models.Notification;
import clstr.delego.models.NotificationResponse;
import clstr.delego.models.Response;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class LoadJSONTaskNotifications extends AsyncTask<String, Void, NotificationResponse> {

    public LoadJSONTaskNotifications(Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(List<Notification> androidList);
        void onError();
    }

    private Listener mListener;

    @Override
    protected NotificationResponse doInBackground(String... strings) {
        try {

            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();

            return gson.fromJson(stringResponse, NotificationResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(NotificationResponse response) {

        if (response != null) {

            mListener.onLoaded(response.getAndroid());

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();
        return response.toString();
    }
}