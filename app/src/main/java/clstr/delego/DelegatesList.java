package clstr.delego;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clstr.delego.models.Delegate;

public class DelegatesList extends AppCompatActivity  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegates_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delegates_list, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements LoadJSONTask.Listener, AdapterView.OnItemClickListener {
        public static final String URL = Constants.WEB_SERVER + "user_sort/";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String KEY_TYPE = "Role";
        private static final String KEY_NAME = "Name";
        private static final String KEY_IMAGE = "Committee";
        private static final String KEY_USERID = "identifier";
        private ListView mListView;
        private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_delegates_list, container, false);

            mListView = (ListView) rootView.findViewById(R.id.list_view_sort);
            mListView.setAdapter(null);

            mListView.setOnItemClickListener(this);
            final String user_sort_type = getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER));
            int value = Integer.parseInt(user_sort_type.replaceAll("[^0-9]", ""));
            if (value == 1){
                final String final_uri = URL + "All";
                new LoadJSONTask(this).execute(final_uri);
            } else if (value == 2){
                final String final_uri = URL + "Owners";
                new LoadJSONTask(this).execute(final_uri);
            } else if (value == 3){
                final String final_uri = URL + "Hosts";
                new LoadJSONTask(this).execute(final_uri);
            } else if (value == 4){
                final String final_uri = URL + "Exhibitioners";
                new LoadJSONTask(this).execute(final_uri);
            } else {
                final String final_uri = URL + "Delegates";
                new LoadJSONTask(this).execute(final_uri);
            }

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final String user_id = mAndroidMapList.get(i).get(KEY_USERID);
            final String process_URI = Constants.WEB_SERVER + "user_details/" + user_id;
            final String arrival_URI = Constants.WEB_SERVER + "user_arrival/" + user_id;
            Intent sendStuff = new Intent(getActivity(), UserCheckin.class);
            sendStuff.putExtra("key", process_URI);
            sendStuff.putExtra("user_arrival", arrival_URI);
            startActivity(sendStuff);

            //Toast.makeText(getActivity(), mAndroidMapList.get(i).get(KEY_NAME),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoaded(List<Delegate> androidList) {

            for (Delegate android : androidList) {

                HashMap<String, String> map = new HashMap<>();

                map.put(KEY_TYPE, android.getType());
                map.put(KEY_NAME, android.getName());
                map.put(KEY_IMAGE, android.getImage());
                map.put(KEY_USERID, android.getID());

                mAndroidMapList.add(map);
            }

            loadListView();
        }


        @Override
        public void onError() {
            Toast.makeText(getActivity(), "Error !", Toast.LENGTH_SHORT).show();
        }

        private void loadListView() {

            ListAdapter adapter = new SimpleAdapter(getActivity(), mAndroidMapList, R.layout.all_users_listitem,
                    new String[] { KEY_TYPE, KEY_NAME},
                    new int[] { R.id.user_type_View, R.id.user_name_View});
            mListView.setAdapter(adapter);

        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Owners";
                case 2:
                    return "Hosts";
                case 3:
                    return "Exhibitioners";
                case 4:
                    return "Delegates";
            }
            return null;
        }
    }
}
