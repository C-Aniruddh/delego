package clstr.delego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clstr.delego.models.Delegate;

public class MyCommitteeView extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    public static final String URL = Constants.WEB_SERVER + "by_committee/";
    private static final String KEY_COMMITTEE = "committee";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "country";
    private static final String KEY_USERID = "numid";
    private static final String KEY_IDENTIFIER = "identifier";
    private ListView mListView;
    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_wise_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsibleToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        ImageView image = (ImageView) findViewById(R.id.ViewImage);
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type = "";
        type = prefs.getString("type", "owner");
        String committee = prefs.getString("user_committee", "None");
        String final_uri = URL + committee;
        collapsibleToolbar.setTitle(committee);
        mListView = (ListView) findViewById(R.id.list_view_commwise);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(final_uri);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onLoaded(List<Delegate> androidList) {

        for (Delegate android : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_COMMITTEE, android.getType());
            map.put(KEY_NAME, android.getName());
            map.put(KEY_IMAGE, android.getImage());
            map.put(KEY_USERID, android.getID());
            map.put(KEY_IDENTIFIER, android.getIdentifier());
            // Toast.makeText(this, map.toString(), Toast.LENGTH_SHORT).show();

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
        final String user_id = mAndroidMapList.get(i).get(KEY_IDENTIFIER);
        //Toast.makeText(this, "User ID : " + user_id, Toast.LENGTH_SHORT).show();
        final String process_URI = Constants.WEB_SERVER + "user_details/" + user_id;
        final String arrival_URI = Constants.WEB_SERVER + "user_arrival/" + user_id;
        final String attendance_URI = Constants.WEB_SERVER + "attendance/" + user_id + "&";
        final String formals_URI = Constants.WEB_SERVER + "formals/" + user_id + "&";
        final String informals_URI = Constants.WEB_SERVER + "informals/" + user_id + "&";
        Intent sendStuff = new Intent(MyCommitteeView.this, UserCheckin.class);
        sendStuff.putExtra("user_attendance", attendance_URI);
        sendStuff.putExtra("key", process_URI);
        sendStuff.putExtra("user_arrival", arrival_URI);
        sendStuff.putExtra("user_formals", formals_URI);
        sendStuff.putExtra("user_informals", informals_URI);
        startActivity(sendStuff);

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(MyCommitteeView.this, mAndroidMapList, R.layout.all_users_listitem,
                new String[]{KEY_COMMITTEE, KEY_NAME},
                new int[]{R.id.user_type_View, R.id.user_name_View});
        //Toast.makeText(this, "InsideLoad List View", Toast.LENGTH_SHORT).show();
        mListView.setAdapter(adapter);

    }
}
