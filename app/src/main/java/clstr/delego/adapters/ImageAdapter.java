package clstr.delego.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import clstr.delego.R;

/**
 * Created by aniruddhc on 16/2/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.unsc, R.drawable.disec,
            R.drawable.unsrm, R.drawable.unhrc,
            R.drawable.ga_legal, R.drawable.ecofin,
            R.drawable.league_of_nations, R.drawable.india_inc,
            R.drawable.league_of_nations, R.drawable.specpol,
            R.drawable.unea
    };

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(256, 256));
        return imageView;
    }

}