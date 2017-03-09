package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 09.03.2017.
 */

public class RPRStrategy implements SongTitleStrategy {
    public final String url = "http://www.rpr1.de/sites/default/files/nocache/current_tracks.json";

    private String streamName;

    public RPRStrategy(String streamName) {
        this.streamName = streamName;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        String rprJson = myHttpHelper.getHtmlFromUrl(url, "", false, false);

        try {
            JSONObject rprObj = new JSONObject(rprJson);

            JSONObject streamObj = rprObj.getJSONObject(streamName);

            String artist = streamObj.getString("artist");
            String title = streamObj.getString("title");

            ergebnis = artist + " - " + title;
            ergebnis = WordUtils.capitalizeFully(ergebnis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
