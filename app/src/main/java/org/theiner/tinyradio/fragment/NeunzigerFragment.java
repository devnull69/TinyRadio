package org.theiner.tinyradio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.theiner.tinyradio.R;
import org.theiner.tinyradio.ViewPagerActivity;
import org.theiner.tinyradio.adapter.RadioStationAdapter;
import org.theiner.tinyradio.context.TinyRadioApplication;
import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.util.Helper;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class NeunzigerFragment extends Fragment {
    private ListView lvStations = null;
    private BaseAdapter adapter = null;

    private ViewPagerActivity me = null;
    private TinyRadioApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        me = (ViewPagerActivity) this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_radiostation, null);

        lvStations = (ListView) layout.findViewById(R.id.lvStations);
        app = (TinyRadioApplication) me.getApplicationContext();

        adapter = new RadioStationAdapter(me, Helper.getFilteredStations(app.getStations(), RadioKategorie.Neunziger));
        lvStations.setAdapter(adapter);

        lvStations.setOnItemClickListener(clicklistener);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void notifyDataSetChanged() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> listview, View view, int position, long l) {
            RadioStation selected = (RadioStation) listview.getItemAtPosition(position);

            me.notifyStationClicked(selected);
        }
    };
}
