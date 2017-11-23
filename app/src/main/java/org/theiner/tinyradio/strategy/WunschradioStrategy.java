package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class WunschradioStrategy implements SongTitleStrategy {
    private String url = "https://wunschradio.de/webplayer/{{streamname}}/php/songinfo.json";

    private String streamName;

    public WunschradioStrategy(String streamName) {
        this.streamName = streamName;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        url = url.replace("{{streamname}}", streamName);

        String wunschJson = myHttpHelper.getHtmlFromUrl(url, "", false, false);

        try {
            JSONObject wunschObj = new JSONObject(wunschJson);
            ergebnis = wunschObj.getString("aktartist") + " - " + wunschObj.getString("akttitle");

            ergebnis = WordUtils.capitalizeFully(ergebnis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
