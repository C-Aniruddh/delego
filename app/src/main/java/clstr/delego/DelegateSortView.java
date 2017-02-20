package clstr.delego;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import clstr.delego.adapters.ImageAdapter;
import clstr.delego.models.Delegate;

public class DelegateSortView extends AppCompatActivity {

    GridView grid;
    String[] web = {
            "UNSC", "DISEC", "UNSRM",
            "UNHRC", "GA Legal", "ECOFIN",
            "India-Pak", "India, Inc", "League of Nations",
            "Specpol", "UNEA"
    } ;
    int[] imageId = {
            R.drawable.unsc, R.drawable.disec,
            R.drawable.unsrm, R.drawable.unhrc,
            R.drawable.ga_legal, R.drawable.ecofin,
            R.drawable.league_of_nations, R.drawable.india_inc,
            R.drawable.league_of_nations, R.drawable.specpol,
            R.drawable.unea

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate_sort_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Choose your committee");

        // Instance of ImageAdapter Class
        /*gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Sending image id to FullScreenActivity
                Intent i = new Intent(getApplicationContext(), CommitteeWiseView.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });*/
        ImageAdapter adapter = new ImageAdapter(DelegateSortView.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), CommitteeWiseView.class);
                // passing array index
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
