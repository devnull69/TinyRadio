package org.theiner.tinyradio.util;

import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class Helper {
    public static List<RadioStation> getFilteredStations(List<RadioStation> liste, RadioKategorie filter) {
        List<RadioStation> ergebnis = new ArrayList<RadioStation>();

        for(RadioStation station: liste) {
            if(station.getRadioKategorie() == filter)
                ergebnis.add(station);
        }

        return ergebnis;
    }


}
