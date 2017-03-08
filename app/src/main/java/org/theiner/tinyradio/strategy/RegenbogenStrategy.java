package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.theiner.tinyradio.util.HTTPHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class RegenbogenStrategy implements SongTitleStrategy {
    private String url = "http://www.regenbogen.de/sites/default/files/nocache/{{streamName}}/{{date}}.json";

    private String streamName;

    public RegenbogenStrategy(String streamName) {
        this.streamName = streamName;
    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        Date jetzt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH");
        String theDate = sdf.format(jetzt);

        url = url.replace("{{streamName}}", streamName);
        url = url.replace("{{date}}", theDate);

        String regenbogenJson = myHttpHelper.getHtmlFromUrl(url, "", false, false);

        try {
            JSONObject regenbogenObj = new JSONObject(regenbogenJson);

            JSONArray hour = regenbogenObj.getJSONArray("hour");

            // Richtigen Eintrag finden
            // Es ist der letzte Eintrag, wenn dessen Uhrzeit kleiner ist als die aktuelle Zeit, ansonsten der vorletzte
            SimpleDateFormat uhrzeitFormat = new SimpleDateFormat("HH:mm");
            String currentTime = uhrzeitFormat.format(jetzt);

            JSONObject current = hour.getJSONObject(hour.length() - 1);
            if(hour.length() > 1) {

                String songTime = current.getString("date").substring(11, 16);

                if(songTime.compareTo(currentTime) > 0)
                    current = hour.getJSONObject(hour.length() - 2);
            }

            String artist = current.getString("artist");
            String title = current.getString("title");

            ergebnis = artist + " - " + title;

            ergebnis = WordUtils.capitalizeFully(ergebnis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
