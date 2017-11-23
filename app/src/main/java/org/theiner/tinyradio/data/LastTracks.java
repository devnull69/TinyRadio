package org.theiner.tinyradio.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by TTheiner on 16.11.2017.
 */

public class LastTracks {
    private Queue<TrackData> trackList = new LinkedList<>();
    private int maxSize = 3;

    public LastTracks() {

    }

    public LastTracks(int maxsize) {
        this.maxSize = maxsize;
    }

    public boolean contains(TrackData td) {
        boolean result = false;
        for(TrackData current : trackList) {
            if(current.equals(td)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void enqueue(TrackData td) {
        if(trackList.size() == maxSize)
            trackList.poll();
        trackList.add(td);
    }

}
