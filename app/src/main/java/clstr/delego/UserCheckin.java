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
import com.shawnlin.numberpicker.NumberPicker;

import butterknife.Bind;

public class UserCheckin extends SlidingActivity {

    public String user_email = "";
    public String user_phone = "";
    public String attendance_URI;
    public String user_committee;
    public String user_background = "";
    public String formalsURI;
    public String informalsURI;
    public int scrollValue;

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
            String committee = prefs.getString("user_committee", "None");
            if (committee.equals(user_committee) || committee.equals("all")) {
                attendance_URI = b.getString("user_attendance");
                if (type.equals("owner") || type.equals("host")) {
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
                                        TextView id = (TextView) findViewById(R.id.idView);
                                        id.setText(R.string.arrived);


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
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "You are not allowed to perform this action.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
    };

    public void startAttendance(View v) {
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type;
        type = prefs.getString("type", "owner");
        String committee = prefs.getString("user_committee", "None");

        if (committee.equals("all") || committee.equals(user_committee)) {
            if (type.equals("owner") || type.equals("rapporteur")) {
                makeAttendanceDialog();
            }
        }
    }
    public void makeAttendanceDialog() {
        new MaterialStyledDialog.Builder(this)
                .setTitle("Set Attendance")
                .setDescription("Is the delegate present and voting, present or absent?")
                .setHeaderColor(R.color.dialog_header)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setPositiveText("Present and Voting")
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    String final_attendance = attendance_URI + "Present%20And%20Voting" + "&session" + String.valueOf(scrollValue);

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
                    String final_attendance = attendance_URI + "Present" + "&session" + String.valueOf(scrollValue);

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
                    String final_attendance = attendance_URI + "Absent" + "&session" + String.valueOf(scrollValue);

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
        final String current_attendance = b.getString("user_check_attendance");
        String checkAttendance = current_attendance + "session" + String.valueOf(scrollValue + 1);
        enableFullscreen();
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String typeUser = prefs.getString("type", "user");
            /*SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
            String background = prefs.getString("user_background", "college");//"No name defined" is the default value.
            if(background.equals("school")){
                CardView informalsCard = (CardView) findViewById(R.id.informals_viewgroup);
                informalsCard.setVisibility(View.GONE);
            }*/
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
                            user_committee = ason.getString("Committee");
                            TextView committee = (TextView) findViewById(R.id.committeeView);
                            committee.setText(user_committee);
                            //String current_attendance = ason.getString("attendance");
                            //TextView attendance = (TextView) findViewById(R.id.attendanceView);
                            //attendance.setText(current_attendance);
                            String user_type = ason.getString("Country");
                            TextView type = (TextView) findViewById(R.id.countryView);
                            type.setText(user_type);
                            String user_rsvp = ason.getString("RSVP");
                            TextView id = (TextView) findViewById(R.id.idView);
                            id.setText(user_rsvp);
                            String user_role = ason.getString("Role");
                            TextView role = (TextView) findViewById(R.id.roleView);
                            role.setText(user_role);
                            user_phone = ason.getString("Phone");
                            TextView phone = (TextView) findViewById(R.id.phoneView);
                            phone.setText(user_phone);
                            user_email = ason.getString("Email");
                            TextView email = (TextView) findViewById(R.id.emailView);
                            email.setText(user_email);
                            String user_formals = ason.getString("formals");
                            TextView formals = (TextView) findViewById(R.id.formalsView);
                            formals.setText(user_formals);
                            String user_informals = ason.getString("informals");
                            TextView informals = (TextView) findViewById(R.id.informalView);
                            informals.setText(user_informals);
                            user_background = ason.getString("background");
                            if (user_background.equals("school")) {
                                CardView informalsCard = (CardView) findViewById(R.id.informals_viewgroup);
                                informalsCard.setVisibility(View.GONE);
                            }
                        } else {
                            String ai_response = "Can't reach the server at the moment. Please try again later.";
                            TextView textView = (TextView) findViewById(R.id.idView);
                            textView.setText(ai_response);
                        }
                    }
                }).execute();
        new HttpRequestTask(
                new HttpRequest(checkAttendance, HttpRequest.GET),
                new HttpRequest.Handler() {

                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            Ason ason = new Ason(response.body);
                            String current_attendance = ason.getString("attendance");
                            TextView attendance = (TextView) findViewById(R.id.attendanceView);
                            attendance.setText(current_attendance);

                        } else {
                            String ai_response = "Can't reach the server at the moment. Please try again later.";
                        }
                    }
                }).execute();

        setContent(R.layout.content_user_checkin);
            /*NumberPicker numberPicker = (NumberPicker) findViewById(R.id.np);
            numberPicker.setOnValueChangeListener(new OnValueChangeListener() {
                @Override
                public void onValueChanged(int newValue) {
                    scrollValue = newValue;
                }
            });*/
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        scrollValue = numberPicker.getValue();
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                scrollValue = newVal;
                String changeAttendance = current_attendance + "session" + String.valueOf(scrollValue);
                new HttpRequestTask(
                        new HttpRequest(changeAttendance, HttpRequest.GET),
                        new HttpRequest.Handler() {

                            @Override
                            public void response(HttpResponse response) {
                                if (response.code == 200) {
                                    Ason ason = new Ason(response.body);
                                    String current_attendance = ason.getString("attendance");
                                    TextView attendance = (TextView) findViewById(R.id.attendanceView);
                                    attendance.setText(current_attendance);

                                } else {
                                    String rep_response = "Can't reach the server at the moment. Please try again later.";
                                }
                            }
                        }).execute();
            }
        });
        if (typeUser.equals("host") || typeUser.equals("user")) {
            CardView pickerCard = (CardView) findViewById(R.id.session_viewgroup);
            pickerCard.setVisibility(View.GONE);
        }

    }

    public void callUser(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user_phone));
        if (ActivityCompat.checkSelfPermission(UserCheckin.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    public void formalsDialog(View v) {
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type;
        type = prefs.getString("type", "owner");
        String committee = prefs.getString("user_committee", "None");
        if (committee.equals("all")) {
            if (type.equals("owner")) {
                Bundle b = getIntent().getExtras();
                formalsURI = b.getString("user_formals");
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Formal Socials")
                        .setDescription("Is the delegate attending formal socials?")
                        .setHeaderColor(R.color.dialog_header)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("Attending and Paid")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = formalsURI + "Attending%20and%20Paid";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is Attending and has paid the amount";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                                    TextView attendance = (TextView) findViewById(R.id.formalsView);
                                                    attendance.setText("Attending and Paid");


                                                } else {
                                                    String server_response = "Can't reach the server at the moment. Please try again later.";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            }
                                        }).execute();
                            }
                        })
                        .setNegativeText("Attending")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = formalsURI + "Attending";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is attending";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));

                                                    TextView attendance = (TextView) findViewById(R.id.formalsView);
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
                        .setNeutralText("Not Attending")
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = formalsURI + "Not%20Attending";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is not attending";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                                    TextView attendance = (TextView) findViewById(R.id.formalsView);
                                                    attendance.setText("Not Attending");
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
        }
    }

    public void informalsDialog(View v) {
        SharedPreferences prefs = getSharedPreferences(Constants.USER_AUTH, MODE_PRIVATE);
        String type;
        type = prefs.getString("type", "owner");
        String committee = prefs.getString("user_committee", "None");
        if (committee.equals("all")) {
            if (type.equals("owner")) {
                Bundle b = getIntent().getExtras();
                informalsURI = b.getString("user_informals");
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Informal Socials")
                        .setDescription("Is the delegate attending informal socials?")
                        .setHeaderColor(R.color.dialog_header)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setPositiveText("Attending and Paid")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = informalsURI + "Attending%20and%20Paid";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is Attending and has paid the amount";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                                    TextView attendance = (TextView) findViewById(R.id.informalView);
                                                    attendance.setText("Attending and Paid");


                                                } else {
                                                    String server_response = "Can't reach the server at the moment. Please try again later.";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            }
                                        }).execute();
                            }
                        })
                        .setNegativeText("Attending")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = informalsURI + "Attending";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is attending";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));

                                                    TextView attendance = (TextView) findViewById(R.id.informalView);
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
                        .setNeutralText("Not Attending")
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            String final_attendance = informalsURI + "Not%20Attending";

                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new HttpRequestTask(
                                        new HttpRequest(final_attendance, HttpRequest.GET),
                                        new HttpRequest.Handler() {

                                            @Override
                                            public void response(HttpResponse response) {
                                                if (response.code == 200) {
                                                    String server_response = "The delegate is not attending";
                                                    Snackbar.make(getWindow().getDecorView().getRootView(), server_response, Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                    FloatingActionButton fab = (FloatingActionButton) getWindow().getDecorView().getRootView().findViewById(R.id.fab);
                                                    fab.setBackgroundColor(Color.parseColor("#2E7D32"));
                                                    TextView attendance = (TextView) findViewById(R.id.informalView);
                                                    attendance.setText("Not Attending");
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
        }
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
