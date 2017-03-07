package org.theiner.tinyradio.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.theiner.tinyradio.R;
import org.theiner.tinyradio.data.RadioStation;

import java.util.List;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class RadioStationAdapter extends ArrayAdapter<RadioStation> {
    private int[] colors = new int[] { 0x50424242, 0x50212121 };

    public RadioStationAdapter(Context context, List<RadioStation> stations) {
        super(context, R.layout.stations_row_layout, stations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View myView = inflater.inflate(R.layout.stations_row_layout, parent, false);

        RadioStation currentResult = getItem(position);

        TextView txtName = (TextView) myView.findViewById(R.id.txtName);
        txtName.setText(currentResult.getName());

        TextView txtSongTitle = (TextView) myView.findViewById(R.id.txtSongTitle);
        txtSongTitle.setText(currentResult.getTitle());

        ImageView ivPlayPause = (ImageView) myView.findViewById(R.id.ivPlayPause);
        if(currentResult.isPlaying()) {
            ivPlayPause.setImageResource(R.drawable.pause);
        } else {
            ivPlayPause.setImageResource(R.drawable.play);
        }

        return myView;
    }
}
