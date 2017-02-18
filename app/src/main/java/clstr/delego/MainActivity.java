package clstr.delego;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchHistoryTable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SearchHistoryTable mHistoryDatabase;
    private NavigationView mNavigation;
    private View mHeaderView;

    private TextView mDrawerHeaderTitle;
    private TextView mDrawerHeaderEmail;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, SetupActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String status = "";
        String name;
        String email = "";
        status = prefs.getString("user_status", "false");//"No name defined" is the default value.
        name = prefs.getString("name", "Delego");
        email = prefs.getString("email", "delego@clstr.tech");
        Toast toast = Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT);
        toast.show();
        if (status.equals("false")) {
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.nav_qr_scan) {
            Intent qr_scan = new Intent(MainActivity.this, QRScanActivity.class);
            startActivity(qr_scan);
            // Handle the camera action
        } else if (id == R.id.nav_all_members) {
            Intent all_members = new Intent(MainActivity.this, DelegateSortView.class);
            startActivity(all_members);
        } else if (id == R.id.nav_search) {
            Intent search = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(search);
        } else if (id == R.id.nav_new_delegate) {
            Intent addNew = new Intent(MainActivity.this, AddDelegate.class);
            startActivity(addNew);
        } else if (id == R.id.nav_share) {
            Intent setup = new Intent(MainActivity.this, SetupActivity.class);
            startActivity(setup);
        } else if (id == R.id.nav_send) {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);

        } else if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE).edit();
            editor.putString("user_status", "false");
            editor.commit();
            Intent restart = new Intent(MainActivity.this, MainActivity.class);
            startActivity(restart);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
