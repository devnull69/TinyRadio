package org.theiner.tinyradio.data;

/**
 * Created by TTheiner on 10.11.2017.
 */

public class TrackData {
    private String artistName;
    private String trackName;
    private String stationName;

    public TrackData(String playlistEntry, String stationName) {
        String[] parts = playlistEntry.split(" - ");
        String artistName = parts[0];
        String trackName = "";
        for(int i=1; i<parts.length; i++) {
            trackName = trackName + " " + parts[i];
        }

        this.artistName = artistName;
        this.trackName = trackName;
        this.stationName = stationName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public boolean equals(TrackData td) {
        boolean result = false;
        if(this.artistName.equals(td.getArtistName()) && this.trackName.equals(td.getTrackName()))
            result = true;
        return result;
    }
}
