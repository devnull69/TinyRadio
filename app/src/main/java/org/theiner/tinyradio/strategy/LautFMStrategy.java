package org.theiner.tinyradio.strategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class LautFMStrategy implements SongTitleStrategy {
    private String url = "http://api.laut.fm/station/{{senderName}}/current_song";

    private String senderName;

    public LautFMStrategy(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        url = url.replace("{{senderName}}", senderName);

        String mhJson = myHttpHelper.getHtmlFromUrl(url, "", false, false);

        try {
            JSONObject mhObj = new JSONObject(mhJson);

            String title = mhObj.getString("title");

            JSONObject artistObj = mhObj.getJSONObject("artist");

            String artist = artistObj.getString("name");

            ergebnis = artist + " - " + title;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
