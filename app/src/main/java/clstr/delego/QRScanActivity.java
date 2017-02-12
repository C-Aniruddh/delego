package clstr.delego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;


public class QRScanActivity extends AppCompatActivity {

    private SurfaceView mySurfaceView;
    private QREader qrEader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_qr_scan);


        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);

        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {

            @Override
            public void onDetected(final String data) {
                Log.d("QR", "Value : " + data);
                final String process_URI = "http://192.168.0.106:5000/user_details/" + data;
                final String arrival_URI = "http://192.168.0.106:5000/user_arrival/" + data;
                Intent sendStuff = new Intent(QRScanActivity.this, UserCheckin.class);
                sendStuff.putExtra("key", process_URI);
                sendStuff.putExtra("user_arrival", arrival_URI);
                startActivity(sendStuff);
                /*text.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });*/
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mySurfaceView.getHeight())
                .width(mySurfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Init and Start with SurfaceView
        // -------------------------------
        qrEader.initAndStart(mySurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cleanup in onPause()
        // --------------------
        qrEader.releaseAndCleanup();
    }
}

