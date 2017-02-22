package clstr.delego;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clstr.delego.models.Delegate;

public class SearchActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener{

    public static final String URL = Constants.WEB_SERVER + "search_delegate/";
    private static final String KEY_COMMITTEE = "committee";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "country";
    private static final String KEY_USERID = "numid";
    private static final String KEY_IDENTIFIER = "identifier";
    protected SearchView mSearchView = null;
    private SearchHistoryTable mHistoryDatabase;
    private ListView mListView;
    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        try {
            setSearchView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchView: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @CallSuper
    protected void getData(String text, int position) {
        mListView = (ListView) findViewById(R.id.list_view_search);
        mListView.setOnItemClickListener(this);
        String query = URL + text;
        query = query.replaceAll(" ", "%20");
        new LoadJSONTask(this).execute(String.valueOf(query));

        //Toast.makeText(getApplicationContext(), text + ", position: " + position, Toast.LENGTH_SHORT).show();
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
        final String process_URI = Constants.WEB_SERVER + "user_details/" + user_id;
        final String arrival_URI = Constants.WEB_SERVER + "user_arrival/" + user_id;
        final String attendance_URI = Constants.WEB_SERVER + "attendance/" + user_id + "&";
        Intent sendStuff = new Intent(SearchActivity.this, UserCheckin.class);
        sendStuff.putExtra("key", process_URI);
        sendStuff.putExtra("user_arrival", arrival_URI);
        sendStuff.putExtra("user_attendance", attendance_URI);
        startActivity(sendStuff);

        //Toast.makeText(this, mAndroidMapList.get(i).get(KEY_NAME),Toast.LENGTH_SHORT).show();

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(SearchActivity.this, mAndroidMapList, R.layout.all_users_listitem,
                new String[] { KEY_COMMITTEE, KEY_NAME},
                new int[] { R.id.user_type_View, R.id.user_name_View});

        mListView.setAdapter(adapter);
    }

    protected void setSearchView() throws JSONException {
        mSearchView = (SearchView) findViewById(R.id.searchView);

        if (mSearchView != null) {
            mSearchView.setHint(R.string.search);
            mSearchView.setVoice(false);
            mSearchView.setShadow(false);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    getData(query, 0);
                    mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public boolean onOpen() {
                    return true;
                }

                @Override
                public boolean onClose() {
                    return true;
                }
            });
            final List<SearchItem> suggestionsList = new ArrayList<>();

            new HttpRequestTask(
                    new HttpRequest(Constants.WEB_SERVER + "all_names", HttpRequest.GET),
                    new HttpRequest.Handler() {

                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                JSONArray arr = null;
                                try {
                                    arr = new JSONArray(response.body);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                List<String> list = new ArrayList<String>();

                                for(int i = 0; i < arr.length(); i++){
                                    try {
                                        suggestionsList.add(new SearchItem(arr.getJSONObject(i).getString("name")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                String server_response = "Can't reach the server at the moment. Please try again later.";
                                Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }).execute();

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    getData(query, position);
                    mSearchView.close(false);
                }
            });
            mSearchView.setAdapter(searchAdapter);

        }
    }

}

