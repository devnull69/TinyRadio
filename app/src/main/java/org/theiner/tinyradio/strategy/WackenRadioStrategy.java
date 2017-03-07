package org.theiner.tinyradio.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class WackenRadioStrategy implements SongTitleStrategy {
    private final String url = "http://www.rautemusik.fm/wackenradio/";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        Document wackenDoc = myHttpHelper.getDocumentFromUrl(url, "", false);

        if(wackenDoc != null) {
            String title = wackenDoc.getElementsByClass("title").get(0).text();
            String artist = wackenDoc.getElementsByClass("artist").get(0).text();

            ergebnis = artist + " - " + title;
        }

        return ergebnis;
    }
}
