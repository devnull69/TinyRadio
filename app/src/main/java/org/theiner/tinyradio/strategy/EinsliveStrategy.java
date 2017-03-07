package org.theiner.tinyradio.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.theiner.tinyradio.util.HTTPHelper;

import java.util.Date;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class EinsliveStrategy implements SongTitleStrategy {

    private final String url = "http://www.wdr.de/radio/radiotext/streamtitle_1live.txt";

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "";

        ergebnis = myHttpHelper.getHtmlFromUrl(url + "?t=" + (new Date()).getTime(), "", false, true);

        if(ergebnis.contains("1LIVE")) {
            Document einsliveDoc = myHttpHelper.getDocumentFromUrl("http://www1.wdr.de/radio/1live/on-air/1live-playlist?t=" + (new Date()).getTime(), "", false);
            Element playlist = einsliveDoc.getElementById("searchPlaylistResult");
            Element currentSongRow = playlist.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0);

            String title = currentSongRow.getElementsByTag("td").get(0).text();
            String artist = currentSongRow.getElementsByTag("td").get(1).text();

            ergebnis = artist + " - " + title;
        }

        return ergebnis;
    }
}
