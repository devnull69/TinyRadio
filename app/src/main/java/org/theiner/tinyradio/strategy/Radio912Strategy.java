package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class Radio912Strategy implements SongTitleStrategy {

    //private final String url = "http://radio912web.lokalradioservices.de/playlist/last5.php";
    private final String url = "https://api-prod.nrwlokalradios.com/playlist/current?station=6";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        String radio912Json = myHttpHelper.getHtmlFromUrl(url, "", false, false);

        try {
            JSONObject radio912Obj = new JSONObject(radio912Json);

            String artist = radio912Obj.getString("artist");
            String title = radio912Obj.getString("title");

            ergebnis = artist + " - " + title;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ergebnis;
    }
}
