package clstr.delego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchHistoryTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clstr.delego.models.Notification;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoadJSONTaskNotifications.Listener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String URL = Constants.WEB_SERVER + "notifications";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_IMAGE = "image";
    private SearchHistoryTable mHistoryDatabase;
    private NavigationView mNavigation;
    private View mHeaderView;
    private TextView mDrawerHeaderTitle;
    private TextView mDrawerHeaderEmail;
    private ListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();
    private String user_type;
    private String name;
    private String email;
    private String user_idenfier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigation = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = mNavigation.getHeaderView(0);

        mDrawerHeaderTitle = (TextView) mHeaderView.findViewById(R.id.userFullname);
        mDrawerHeaderEmail = (TextView) mHeaderView.findViewById(R.id.userEmail);

        mListView = (ListView) findViewById(R.id.list_view_notifications);
        mListView.setOnItemClickListener(this);
        new LoadJSONTaskNotifications(this).execute(URL);
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String status = "";
        status = prefs.getString("user_status", "false");//"No name defined" is the default value.
        name = prefs.getString("fullname", "Delego");
        email = prefs.getString("email_id", "delego@clstr.tech");
        user_type = prefs.getString("type", "user");
        user_idenfier = prefs.getString("user_identifier", "LOL");

        //  Declare a new thread to do a preference check
        final String finalStatus = status;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        refreshAction();
                                    }
                                }
        );*/

        //Toast toast = Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT);
        //toast.show();

        if (finalStatus.equals("false")) {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        } else {
            //TextView userFullname = (TextView) findViewById(R.id.userFullname);
            //TextView userEmail = (TextView) findViewById(R.id.userEmail);
            mDrawerHeaderTitle.setText(name);
            mDrawerHeaderEmail.setText(email);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        if (user_type.equals("owner")){
            menu.add(R.id.menu_item, 1, 100, "Scan QR Code").setIcon(R.drawable.qrcode_scan);
            menu.add(R.id.menu_item, 2, 200, "All members").setIcon(R.drawable.account_multiple);
            menu.add(R.id.menu_item, 3, 300, "Search").setIcon(R.drawable.account_search);
            menu.add(R.id.menu_item, 4, 400, "Add Delegate").setIcon(R.drawable.account_multiple_plus);
            menu.add(R.id.extra, 5, 500, "Logout").setIcon(R.drawable.logout);
            menu.setGroupCheckable(R.id.menu_item, true, true);
            menu.setGroupVisible(R.id.menu_item, true);
        } else if (user_type.equals("host")) {
            menu.add(R.id.menu_item, 1, 100, "Scan QR Code").setIcon(R.drawable.qrcode_scan);
            menu.add(R.id.menu_item, 2, 200, "My Committee").setIcon(R.drawable.account_multiple);
            menu.add(R.id.menu_item, 3, 300, "Search").setIcon(R.drawable.account_search);
            menu.add(R.id.menu_item, 4, 400, "Add Delegate").setIcon(R.drawable.account_multiple_plus);
            menu.add(R.id.extra, 5, 500, "Logout").setIcon(R.drawable.logout);
            menu.setGroupCheckable(R.id.menu_item, true, true);
            menu.setGroupVisible(R.id.menu_item, true);
        } else if(user_type.equals("user")){
            menu.add(R.id.menu_item, 1, 100, "Scan QR Code").setIcon(R.drawable.qrcode_scan);
            menu.add(R.id.menu_item, 2, 200, "Profile").setIcon(R.drawable.account_multiple);
            menu.add(R.id.menu_item, 3, 300, "Schedule").setIcon(R.drawable.timetable);
            menu.add(R.id.menu_item, 4, 400, "About Mun").setIcon(R.drawable.alert_circle);
            menu.add(R.id.extra, 5, 500, "Logout").setIcon(R.drawable.logout);
            menu.setGroupCheckable(R.id.menu_item, true, true);
            menu.setGroupVisible(R.id.menu_item, true);
        } else if (user_type.equals("rapporteur")) {
            menu.add(R.id.menu_item, 1, 100, "Scan QR Code").setIcon(R.drawable.qrcode_scan);
            menu.add(R.id.menu_item, 2, 200, "My Committee").setIcon(R.drawable.account_multiple);
            menu.add(R.id.menu_item, 3, 300, "Search").setIcon(R.drawable.account_search);
            menu.add(R.id.menu_item, 4, 400, "Add Delegate").setIcon(R.drawable.account_multiple_plus);
            menu.add(R.id.extra, 5, 500, "Logout").setIcon(R.drawable.logout);
            menu.setGroupCheckable(R.id.menu_item, true, true);
            menu.setGroupVisible(R.id.menu_item, true);

        }
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public void onLoaded(List<Notification> androidList) {

        for (Notification android : androidList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_TITLE, android.getTitle());
            map.put(KEY_CONTENT, android.getContent());
            //Toast.makeText(this, map.toString(), Toast.LENGTH_SHORT).show();
            mAndroidMapList.add(map);

        }

        loadListView();
    }

    public void onRefresh(){
        refreshAction();
    }
    public void refreshAction(){
        mAndroidMapList.clear();
        mListView.deferNotifyDataSetChanged();
        mListView.setOnItemClickListener(this);
        new LoadJSONTaskNotifications(this).execute(URL);
    }

    @Override
    public void onError() {

        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String title = mAndroidMapList.get(i).get(KEY_TITLE);
        final String content = mAndroidMapList.get(i).get(KEY_CONTENT);
        final String image = mAndroidMapList.get(i).get(KEY_IMAGE);
        Intent sendStuff = new Intent(MainActivity.this, NotificationView.class);
        sendStuff.putExtra("title", title);
        sendStuff.putExtra("content", content);
        sendStuff.putExtra("image", image);
        startActivity(sendStuff);

    }

    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, mAndroidMapList, R.layout.notifications_list_item,
                new String[] { KEY_TITLE, KEY_CONTENT},
                new int[] { R.id.notificationTitleView, R.id.notificationContentView});
        //Toast.makeText(this, "InsideLoad List View", Toast.LENGTH_SHORT).show();
        mListView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (user_type.equals("owner")){
            if (id == 1) {
                Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(qr_scan);
                // Handle the camera action
            } else if (id == 2){
                Intent all_members = new Intent(MainActivity.this, DelegateSortView.class);
                startActivity(all_members);
            } else if (id == 3){
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
            } else if (id == 4){
                Intent addNew = new Intent(MainActivity.this, AddDelegate.class);
                startActivity(addNew);
            } else if (id == 5){
                SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE).edit();
                editor.putString("user_status", "false");
                editor.commit();
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                startActivity(restart);
            }
        } else if (user_type.equals("host")) {
            if (id == 1) {
                Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(qr_scan);
                // Handle the camera action
            } else if (id == 2) {
                Intent all_members = new Intent(MainActivity.this, MyCommitteeView.class);
                startActivity(all_members);
            } else if (id == 3) {
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
            } else if (id == 4) {
                Intent addNew = new Intent(MainActivity.this, AddDelegate.class);
                startActivity(addNew);
            } else if (id == 5) {
                SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE).edit();
                editor.putString("user_status", "false");
                editor.commit();
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                startActivity(restart);
            }
        } else if (user_type.equals("rapporteur")) {
            if (id == 1) {
                Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(qr_scan);
                // Handle the camera action
            } else if (id == 2) {
                Intent all_members = new Intent(MainActivity.this, MyCommitteeView.class);
                startActivity(all_members);
            } else if (id == 3) {
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
            } else if (id == 4) {
                Intent addNew = new Intent(MainActivity.this, AddDelegate.class);
                startActivity(addNew);
            } else if (id == 5) {
                SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE).edit();
                editor.putString("user_status", "false");
                editor.commit();
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                startActivity(restart);
            }
        } else if (user_type.equals("user")) {
            if (id == 1) {
                Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
                startActivity(qr_scan);
                // Handle the camera action
            } else if (id == 2){
                Intent profile = new Intent(MainActivity.this, UserProfile.class);
                final String formals_URI = Constants.WEB_SERVER + "formals/" + user_idenfier + "&";
                final String informals_URI = Constants.WEB_SERVER + "informals/" + user_idenfier + "&";
                final String process_URI = Constants.WEB_SERVER + "user_details/" + user_idenfier;
                final String attendance_URI = Constants.WEB_SERVER + "attendance/" + user_idenfier + "&";
                final String checkAttendance = Constants.WEB_SERVER + "current_attendance/" + user_idenfier + "&";
                profile.putExtra("user_attendance", attendance_URI);
                profile.putExtra("user_check_attendance", checkAttendance);
                profile.putExtra("user_formals", formals_URI);
                profile.putExtra("user_informals", informals_URI);
                profile.putExtra("key", process_URI);
                startActivity(profile);
            } else if (id == 3){
                Snackbar.make(getWindow().getDecorView().getRootView(), "Schedule", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (id == 4){
                Snackbar.make(getWindow().getDecorView().getRootView(), "About", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (id == 5){
                SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE).edit();
                editor.putString("user_status", "false");
                editor.commit();
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                startActivity(restart);
            }
        }
        if (id == 1) {
            Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
            startActivity(qr_scan);
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
