package org.theiner.tinyradio.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class RockAntenneStrategy implements SongTitleStrategy {

    private final String url = "https://api.antenne.de/1.0.0/webradio/metadata/details?&apikey=10fa9dee55182f9d1cd359c787aec98a&streamingChannel=heavy-metal";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";
        String rockAntenneJson = myHttpHelper.getHtmlFromUrl(url, "", false);

        try {
            JSONObject fullobj = new JSONObject(rockAntenneJson);
            JSONObject object = fullobj.getJSONObject("object");
            JSONObject now = object.getJSONObject("now");

            String artist = "";
            String title = "";

            if(now.getString("class").equals("music")) {
                artist = now.getString("artist");
                title = now.getString("title");
            } else {
                // in der History suchen
                JSONArray history = object.getJSONArray("history");
                int pos = 0;
                JSONObject historyEntry = history.getJSONObject(pos);
                while(!historyEntry.getString("class").equals("music")) {
                    pos++;
                    historyEntry = history.getJSONObject(pos);
                }
                artist = historyEntry.getString("artist");
                title = historyEntry.getString("title");
            }

            ergebnis = artist + " - " + title;
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
