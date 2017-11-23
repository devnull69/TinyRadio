package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

import java.util.Date;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class RSHStrategy implements SongTitleStrategy {
    private final String url = "http://stream-service.loverad.io/v3/rsh?_=";

    private int streamnumber = 1;

    public RSHStrategy(int streamnumber) {
        this.streamnumber = streamnumber;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        String rshJson = myHttpHelper.getHtmlFromUrl(url, "", false);

        try {
            JSONObject rshObj = new JSONObject(rshJson);
            JSONObject streamNumber = rshObj.getJSONObject(String.valueOf(this.streamnumber));

            String artist = streamNumber.getString("artist_name");
            String title = streamNumber.getString("song_title");

            ergebnis = artist + " - " + title;

            ergebnis = WordUtils.capitalizeFully(ergebnis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
