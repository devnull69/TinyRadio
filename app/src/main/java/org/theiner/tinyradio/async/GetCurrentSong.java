package org.theiner.tinyradio.async;

import android.os.AsyncTask;

import org.theiner.tinyradio.data.RadioStation;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class GetCurrentSong extends AsyncTask<RadioStation, Void, String> {

    public static interface CheckCompleteListener {
        void onCheckComplete(String result);
    }

    private CheckCompleteListener ccl = null;

    public GetCurrentSong(CheckCompleteListener ccl) {
        this.ccl = ccl;
    }

    @Override
    protected String doInBackground(RadioStation... stations) {

        RadioStation rStation = stations[0];
        if(rStation.getStrategy() != null)
            rStation.setTitle(rStation.getStrategy().getSongTitle(rStation.getHttpHelper()));
        return "Ok";

    }

    @Override
    protected void onPostExecute(String result) {
        ccl.onCheckComplete(result);
    }
}
