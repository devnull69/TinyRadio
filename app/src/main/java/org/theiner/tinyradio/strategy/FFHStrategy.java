package org.theiner.tinyradio.strategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class FFHStrategy implements SongTitleStrategy {
    private final String url = "https://webradio.ffh.de/custom/getStationSonginfos.php?station=";

    private String senderName;

    public FFHStrategy(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        String ffhJson = myHttpHelper.getHtmlFromUrl(url + senderName, "", false, false);

        try {
            JSONObject ffhObj = new JSONObject(ffhJson);
            JSONObject current = ffhObj.getJSONObject("current");

            String artist = current.getString("artist");
            String title = current.getString("title");

            ergebnis = artist + " - " + title;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
