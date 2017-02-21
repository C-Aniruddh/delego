package clstr.delego;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

import clstr.delego.fragments.SlideFive;
import clstr.delego.fragments.SlideFour;
import clstr.delego.fragments.SlideOne;
import clstr.delego.fragments.SlideThree;
import clstr.delego.fragments.SlideTwo;

public class SetupActivity extends AppIntro {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        addSlide(SlideOne.newInstance(R.layout.slide_one));
        addSlide(SlideTwo.newInstance(R.layout.slide_two));
        addSlide(SlideThree.newInstance(R.layout.slide_three));
        addSlide(SlideFour.newInstance(R.layout.slide_four));
        addSlide(SlideFive.newInstance(R.layout.slide_five));
        askForPermissions(new String[]{Manifest.permission.CAMERA}, 2);
        askForPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 3);
        askForPermissions(new String[]{Manifest.permission.CALL_PHONE}, 4);
        //setBarColor(Color.parseColor("#2f4779"));
        setIndicatorColor(Color.parseColor("#2f4779"), Color.parseColor("#2f4779"));

        //setSeparatorColor(Color.parseColor("#2196F3"));
    }



    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent login = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(login);
    }
}
