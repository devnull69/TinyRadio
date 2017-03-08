package org.theiner.tinyradio.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class RadioDEStrategy implements SongTitleStrategy {
    private final String url = "https://api.radio.de/info/v2/search/nowplaying?_=1488891339000&apikey=28b7b71da75f78d5811ca9309703b0b12fa8f6bf&numberoftitles=1&station=";

    private String stationId;

    public RadioDEStrategy(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        String bayernJson = myHttpHelper.getHtmlFromUrl(url + stationId, "", false, false);

        try {
            JSONArray bayernArr = new JSONArray(bayernJson);
            JSONObject bayernObj = bayernArr.getJSONObject(0);
            ergebnis = bayernObj.getString("streamTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
