package org.theiner.tinyradio.data;

import org.theiner.tinyradio.strategy.SongTitleStrategy;
import org.theiner.tinyradio.util.HTTPHelper;

/**
 * Created by TTheiner on 06.03.2017.
 */

public class RadioStation {
    private String name;
    private String url;
    private String title = "";
    private boolean isPlaying = false;
    private boolean isInitialState = true;
    private SongTitleStrategy strategy = null;
    private HTTPHelper httpHelper = new HTTPHelper();

    public RadioStation(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public HTTPHelper getHttpHelper() {
        return httpHelper;
    }

    public SongTitleStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SongTitleStrategy strategy) {
        this.strategy = strategy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isInitialState() {
        return isInitialState;
    }

    public void setInitialState(boolean initialState) {
        isInitialState = initialState;
    }
}
