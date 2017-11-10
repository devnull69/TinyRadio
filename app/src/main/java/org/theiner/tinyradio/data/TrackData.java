package org.theiner.tinyradio.data;

/**
 * Created by TTheiner on 10.11.2017.
 */

public class TrackData {
    private String artistName;
    private String trackName;
    private String stationName;

    public TrackData(String playlistEntry, String stationName) {
        String[] parts = playlistEntry.split("-");
        String artistName = parts[0];
        String trackName = parts[1];

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
}
