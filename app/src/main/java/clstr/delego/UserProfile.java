package clstr.delego;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.json.Ason;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import butterknife.Bind;

public class UserProfile extends SlidingActivity {

    public String user_email = "";
    public String user_phone = "";
    public String attendance_URI;

    @Bind(R.id.phone_viewgroup)
    CardView phoneView;
    @Bind(R.id.email_viewgroup)
    CardView emailView;
    private View.OnClickListener userArrival = new View.OnClickListener() {
        public void onClick(View v) {
            Bundle b = getIntent().getExtras();
            String arrival_URI = b.getString("user_arrival");
            SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
            String type;
            type = prefs.getString("type", "owner");

            attendance_URI = b.getString("user_attendance");
            if (type.equals("owner")) {
                new HttpRequestTask(
                        new HttpRequest(arrival_URI, HttpRequest.GET),
                        new HttpRequest.Handler() {

                            @Override
                            public void response(HttpResponse response) {
                                if (response.code == 200) {
                                    String server_response = "The delegate has arrived!";
                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));


                                } else {
                                    String server_response = "Can't reach the server at the moment. Please try again later.";
                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        }).execute();
            } else if (type.equals("rapporteur")) {
                makeAttendanceDialog();
            }

        }
    };

    public void makeAttendanceDialog() {
        new MaterialStyledDialog.Builder(this)
                .setTitle("Set Attendance")
                .setDescription("Is the delegate present and voting, present or absent?")
                .setHeaderColor(R.color.dialog_header)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setPositiveText("Present and Voting")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    String final_attendance = attendance_URI + "Present%20And%20Voting";

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new HttpRequestTask(
                                new HttpRequest(final_attendance, HttpRequest.GET),
                                new HttpRequest.Handler() {

                                    @Override
                                    public void response(HttpResponse response) {
                                        if (response.code == 200) {
                                            String server_response = "The delegate is Present and Voting";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                            fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                            TextView attendance = (TextView) findViewById(R.id.attendanceView);
                                            attendance.setText("Present and Voting");


                                        } else {
                                            String server_response = "Can't reach the server at the moment. Please try again later.";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                }).execute();
                    }
                })
                .setNegativeText("Present")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    String final_attendance = attendance_URI + "Present";

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new HttpRequestTask(
                                new HttpRequest(final_attendance, HttpRequest.GET),
                                new HttpRequest.Handler() {

                                    @Override
                                    public void response(HttpResponse response) {
                                        if (response.code == 200) {
                                            String server_response = "The delegate has been marked Present";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                            fab.setBackgroundColor(Color.parseColor("#2E7D32"));

                                            TextView attendance = (TextView) findViewById(R.id.attendanceView);
                                            attendance.setText("Present");

                                        } else {
                                            String server_response = "Can't reach the server at the moment. Please try again later.";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                }).execute();
                    }
                })
                .setNeutralText("Absent")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    String final_attendance = attendance_URI + "Absent";

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new HttpRequestTask(
                                new HttpRequest(final_attendance, HttpRequest.GET),
                                new HttpRequest.Handler() {

                                    @Override
                                    public void response(HttpResponse response) {
                                        if (response.code == 200) {
                                            String server_response = "The delegate has been marked Absent";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                            fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                            TextView attendance = (TextView) findViewById(R.id.attendanceView);
                                            attendance.setText("Absent");
                                        } else {
                                            String server_response = "Can't reach the server at the moment. Please try again later.";
                                            Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                }).execute();
                    }
                })
                //.setCancelable(true)
                .setScrollable(true)
                .show();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        String process_URI = b.getString("key");
        enableFullscreen();
        setFab(getResources().getColor(R.color.colorAccent), R.drawable.check, userArrival);
        new HttpRequestTask(
                new HttpRequest(process_URI, HttpRequest.GET),
                new HttpRequest.Handler() {

                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            Ason ason = new Ason(response.body);
                            String user_name = ason.getString("Name");
                            setTitle(user_name);
                            String user_image = ason.getString("Image");
                            Log.d("TAG", user_image);
                            setUserImage(user_image);
                            String user_committee = ason.getString("Committee");
                            TextView committee = (TextView) findViewById(R.id.committeeView);
                            committee.setText(user_committee);
                            String current_attendance = ason.getString("attendance");
                            TextView attendance = (TextView) findViewById(R.id.attendanceView);
                            attendance.setText(current_attendance);
                            String user_type = ason.getString("Country");
                            TextView type = (TextView) findViewById(R.id.countryView);
                            type.setText(user_type);
                            String user_id = ason.getString("Numid");
                            TextView id = (TextView) findViewById(R.id.idView);
                            id.setText(user_id);
                            String user_role = ason.getString("Role");
                            TextView role = (TextView) findViewById(R.id.roleView);
                            role.setText(user_role);
                            user_phone = ason.getString("Phone");
                            TextView phone = (TextView) findViewById(R.id.phoneView);
                            phone.setText(user_phone);
                            user_email = ason.getString("Email");
                            TextView email = (TextView) findViewById(R.id.emailView);
                            email.setText(user_email);
                        } else {
                            String ai_response = "Can't reach the server at the moment. Please try again later.";
                            TextView textView = (TextView) findViewById(R.id.idView);
                            textView.setText(ai_response);
                        }
                    }
                }).execute();
        setContent(R.layout.content_user_checkin);

    }

    public void callUser(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user_phone));
        if (ActivityCompat.checkSelfPermission(UserProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    public void mailUser(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "delego@clstr.tech");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public void setUserImage(String url) {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        setImage(resource);
                    }
                });
    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }


}
