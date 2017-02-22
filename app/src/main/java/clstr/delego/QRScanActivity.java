package clstr.delego;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_qr_scan);

        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type = "";
        type = prefs.getString("type", "owner");
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_view);
        final String finalType = type;
        qrEader = new QREader.Builder(this, mySurfaceView, new QRDataListener() {

            @Override
            public void onDetected(final String data) {
                if (finalType.equals("owner")) {
                    Log.d("QR", "Value : " + data);
                    final String process_URI = Constants.WEB_SERVER + "user_details/" + data;
                    flag = 1;
                    final String arrival_URI = Constants.WEB_SERVER + "user_arrival/" + data;
                    Intent sendStuff = new Intent(QRScanActivity.this, UserCheckin.class);
                    sendStuff.putExtra("key", process_URI);
                    sendStuff.putExtra("user_arrival", arrival_URI);
                    startActivity(sendStuff);
                } else {
                    Log.d("QR", "Value : " + data);
                    final String process_URI = Constants.WEB_SERVER + "user_details/" + data;
                    flag = 1;
                    final String arrival_URI = Constants.WEB_SERVER + "user_arrival/" + data;
                    Intent sendStuff = new Intent(QRScanActivity.this, UserShare.class);
                    sendStuff.putExtra("key", process_URI);
                    sendStuff.putExtra("user_arrival", arrival_URI);
                    startActivity(sendStuff);
                }
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
        Log.d("TAG", "Call");
        if(flag == 1){
            Intent restart = new  Intent(QRScanActivity.this, MainActivity.class);
            startActivity(restart);
        }
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

