package clstr.delego;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clstr.delego.models.Delegate;

public class CommitteeView extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {

    private ListView mListView;

    public static final String URL = Constants.WEB_SERVER+ "by_committee/" + "UNHRC";

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    private static final String KEY_COMMITTEE = "committee";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "country";
    private static final String KEY_USERID = "numid";
    private static final String KEY_IDENTIFIER = "identifier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mListView = (ListView) findViewById(R.id.list_view_comm);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
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
        Toast.makeText(this, "User ID : " + user_id, Toast.LENGTH_SHORT).show();
        final String process_URI = Constants.WEB_SERVER + "user_details/" + user_id;
        final String arrival_URI = Constants.WEB_SERVER +"user_arrival/" + user_id;
        Intent sendStuff = new Intent(CommitteeView.this, UserCheckin.class);
        sendStuff.putExtra("key", process_URI);
        sendStuff.putExtra("user_arrival", arrival_URI);
        startActivity(sendStuff);

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(CommitteeView.this, mAndroidMapList, R.layout.all_users_listitem,
                new String[] { KEY_COMMITTEE, KEY_NAME},
                new int[] { R.id.user_type_View, R.id.user_name_View});
        Toast.makeText(this, "InsideLoad List View", Toast.LENGTH_SHORT).show();
        mListView.setAdapter(adapter);

    }
}
