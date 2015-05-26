package io.palaima.debugdrawer.app;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private final Picasso mPicasso;

    public ImageAdapter(Context context, List<String> images, Picasso picasso) {
        super(context, 0, images);
        mPicasso = picasso;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false);
        }
        mPicasso.load(url).into((ImageView)convertView.findViewById(R.id.image));
        return convertView;
    }
}
