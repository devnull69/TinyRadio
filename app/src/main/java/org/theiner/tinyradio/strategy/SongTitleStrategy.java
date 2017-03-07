package org.theiner.tinyradio.strategy;

import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 06.03.2017.
 */

public interface SongTitleStrategy {
    public String getSongTitle(HTTPHelper myHttpHelper);
}
