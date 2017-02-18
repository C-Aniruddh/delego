package clstr.delego.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import clstr.delego.R;

/**
 * Created by aniruddhc on 17/2/17.
 */

public class SlideOne extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public SlideOne() {
    }

    public static SlideOne newInstance(int layoutResId) {
        SlideOne sampleSlide = new SlideOne();

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(bundleArgs);

        return sampleSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slide_one, container, false);


    }
}
