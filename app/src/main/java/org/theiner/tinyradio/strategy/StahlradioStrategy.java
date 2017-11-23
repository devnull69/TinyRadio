package org.theiner.tinyradio.strategy;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 07.03.2017.
 */

public class StahlradioStrategy implements SongTitleStrategy {
    private final String url = "http://streamboxgenerator.eu/w_p_streambox_to_go/stream_nachladen.php?send=29&steam_ip=streamplus52.leonex.de:22810&sid=&sid=1";

    public StahlradioStrategy() {

    }

    @Override
    public String getSongTitle(HTTPHelper myHttpHelper) {
        String ergebnis = "Unbekannt";

        Document stahlradioDoc = myHttpHelper.getDocumentFromUrl(url, "", false);

        try {
            Element marquee = stahlradioDoc.getElementsByTag("marquee").get(0);
            ergebnis = marquee.html();

            ergebnis = WordUtils.capitalizeFully(ergebnis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ergebnis;
    }
}
