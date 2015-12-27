package io.palaima.debugdrawer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<String> {

    private final Picasso picasso;

    public ImageAdapter(Context context, List<String> images, Picasso picasso) {
        super(context, 0, images);
        this.picasso = picasso;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false);
        }
        picasso.load(url).into((ImageView)convertView.findViewById(R.id.image));
        return convertView;
    }
}
