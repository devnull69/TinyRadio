package org.theiner.tinyradio.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class WDR2Strategy implements SongTitleStrategy {
    private final String url = "http://www1.wdr.de/radio/wdr2/titelsuche100.html";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "";

        Document wdr2Doc = myHttpHelper.getDocumentFromUrl(url, "", false);
        if(wdr2Doc != null) {
            Element playlist = wdr2Doc.getElementById("searchPlaylistResult");
            Element currentSongRow = playlist.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0);

            String title = currentSongRow.getElementsByTag("td").get(0).text();
            String artist = currentSongRow.getElementsByTag("td").get(1).text();

            ergebnis = artist + " - " + title;
        }

        return ergebnis;
    }
}
