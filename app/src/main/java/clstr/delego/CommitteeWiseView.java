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

public class CommitteeWiseView extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener  {

    public static final String URL = Constants.WEB_SERVER+ "by_committee/";
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
        // get intent data
        Intent i = getIntent();
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type = "";
        type = prefs.getString("type", "owner");

        // Selected image id
        int position = i.getExtras().getInt("id");
        if (position == 0){
            String final_uri = URL + "UNSC";
            collapsibleToolbar.setTitle("UNSC");
            toolbar.setTitle("UNSC");
            image.setImageResource(R.drawable.unsc);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if(position == 1){
            String final_uri = URL + "DISEC";
            collapsibleToolbar.setTitle("DISEC");
            image.setImageResource(R.drawable.disec);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if(position == 2){
            String final_uri = URL + "UNSRM";
            collapsibleToolbar.setTitle("UNSRM");
            image.setImageResource(R.drawable.unsrm);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if (position == 3){
            String final_uri = URL + "UNHRC";
            collapsibleToolbar.setTitle("UNHRC");
            image.setImageResource(R.drawable.unhrc);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if(position == 4){
            String final_uri = URL + "GA Legal";
            final_uri = final_uri.replaceAll(" ", "%20");
            collapsibleToolbar.setTitle("GA Legal");
            image.setImageResource(R.drawable.ga_legal);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if (position == 5){
            String final_uri = URL + "ECOFIN";
            collapsibleToolbar.setTitle("ECOFIN");
            image.setImageResource(R.drawable.ecofin);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if (position == 6){
            String final_uri = URL + "Indo-Pak";
            collapsibleToolbar.setTitle("Indo-Pak");
            image.setImageResource(R.drawable.league_of_nations);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if (position == 7){
            String final_uri = URL + "India, Inc";
            final_uri = final_uri.replaceAll(" ", "%20");
            collapsibleToolbar.setTitle("India, Inc");
            image.setImageResource(R.drawable.india_inc);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if (position == 8){
            String final_uri = URL + "League of Nations";
            final_uri = final_uri.replaceAll(" ", "%20");
            collapsibleToolbar.setTitle("League of Nations");
            image.setImageResource(R.drawable.league_of_nations);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if(position == 9){
            String final_uri = URL + "SPECPOL";
            collapsibleToolbar.setTitle("SPECPOL");
            image.setImageResource(R.drawable.specpol);
            mListView = (ListView) findViewById(R.id.list_view_commwise);
            mListView.setOnItemClickListener(this);
            new LoadJSONTask(this).execute(final_uri);
        } else if(position ==10){
            String final_uri = URL + "UNEA";
            collapsibleToolbar.setTitle("UNEA");
            image.setImageResource(R.drawable.unea);
        }

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
        final String arrival_URI = Constants.WEB_SERVER +"user_arrival/" + user_id;
        final String attendance_URI = Constants.WEB_SERVER + "attendance/" + user_id + "&";
        final String formals_URI = Constants.WEB_SERVER + "formals/" + user_id + "&";
        final String informals_URI = Constants.WEB_SERVER + "informals/" + user_id + "&";
        final String checkAttendance = Constants.WEB_SERVER + "current_attendance/" + user_id + "&";
        Intent sendStuff = new Intent(CommitteeWiseView.this, UserCheckin.class);
        sendStuff.putExtra("user_attendance", attendance_URI);
        sendStuff.putExtra("user_check_attendance", checkAttendance);
        sendStuff.putExtra("key", process_URI);
        sendStuff.putExtra("user_arrival", arrival_URI);
        sendStuff.putExtra("user_formals", formals_URI);
        sendStuff.putExtra("user_informals", informals_URI);
        startActivity(sendStuff);

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(CommitteeWiseView.this, mAndroidMapList, R.layout.all_users_listitem,
                new String[] { KEY_COMMITTEE, KEY_NAME},
                new int[] { R.id.user_type_View, R.id.user_name_View});
        //Toast.makeText(this, "InsideLoad List View", Toast.LENGTH_SHORT).show();
        mListView.setAdapter(adapter);

    }
}
