package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.nodes.Document;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class Radio912Strategy implements SongTitleStrategy {

    //private final String url = "http://radio912web.lokalradioservices.de/playlist/last5.php";
    private final String url = "http://www.radio912.de/";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        Document radio912Doc = myHttpHelper.getDocumentFromUrl(url, "", false);

        if(radio912Doc != null) {
//            Element playlist = radio912Doc.getElementById("playlist");
//            String artist = playlist.getElementsByClass("artist").get(0).text();
//            String title = playlist.getElementsByClass("title").get(0).text();

            String artist = radio912Doc.getElementsByClass("stichzeile").get(0).text();
            String title = radio912Doc.getElementsByClass("headline").get(0).text();

            ergebnis = artist + " - " + title;

            ergebnis = WordUtils.capitalizeFully(ergebnis);
        }

        return ergebnis;
    }
}
