package clstr.delego;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import clstr.delego.models.Delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllUsers extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    public static final String URL = Constants.WEB_SERVER+ "allusers";

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "user_image";
    private static final String KEY_USERID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
    }

    @Override
    public void onLoaded(List<Delegate> androidList) {

        for (Delegate android : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_TYPE, android.getType());
            map.put(KEY_NAME, android.getName());
            map.put(KEY_IMAGE, android.getImage());
            map.put(KEY_USERID, android.getID());
            Toast.makeText(this, map.toString(), Toast.LENGTH_SHORT).show();

            mAndroidMapList.add(map);

        }

        loadListView();
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String user_id = mAndroidMapList.get(i).get(KEY_USERID);
        //Toast.makeText(this, user_id, Toast.LENGTH_SHORT).show();
        final String process_URI = Constants.WEB_SERVER + "user_details/" + Integer.parseInt(user_id);
        final String arrival_URI = Constants.WEB_SERVER +"user_arrival/" + Integer.parseInt(user_id);
        Intent sendStuff = new Intent(AllUsers.this, UserCheckin.class);
        sendStuff.putExtra("key", process_URI);
        sendStuff.putExtra("user_arrival", arrival_URI);
        startActivity(sendStuff);

        Toast.makeText(this, mAndroidMapList.get(i).get(KEY_NAME),Toast.LENGTH_SHORT).show();

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(AllUsers.this, mAndroidMapList, R.layout.all_users_listitem,
                new String[] { KEY_TYPE, KEY_NAME},
                new int[] { R.id.user_type_View, R.id.user_name_View});
        Toast.makeText(this, "InsideLoad List View", Toast.LENGTH_SHORT).show();
        mListView.setAdapter(adapter);

    }
}
