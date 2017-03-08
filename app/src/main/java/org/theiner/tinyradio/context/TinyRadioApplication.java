package org.theiner.tinyradio.context;

import android.app.Application;

import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.strategy.AntenneBayernStrategy;
import org.theiner.tinyradio.strategy.EinsliveStrategy;
import org.theiner.tinyradio.strategy.FFHStrategy;
import org.theiner.tinyradio.strategy.RSHStrategy;
import org.theiner.tinyradio.strategy.Radio912Strategy;
import org.theiner.tinyradio.strategy.RegenbogenStrategy;
import org.theiner.tinyradio.strategy.RockAntenneStrategy;
import org.theiner.tinyradio.strategy.WDR2Strategy;
import org.theiner.tinyradio.strategy.WackenRadioStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class TinyRadioApplication extends Application {
    private List<RadioStation> stations = new ArrayList<RadioStation>();

    public void initRadioStations() {
        RadioStation rs;

        rs = new RadioStation("Rock Antenne Heavy Metal", "https://mp3channels.webradio.antenne.de/heavy-metal");
        rs.setStrategy(new RockAntenneStrategy());
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Wacken Radio", "http://138.201.248.93/wackenradio");
        rs.setStrategy(new WackenRadioStrategy());
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Radio 91.2", "http://server2.lokalradioserver.de:8000/xstream");
        rs.setStrategy(new Radio912Strategy());
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("R.SH", "http://regiocast.hoerradar.de/rsh128");
        rs.setStrategy(new RSHStrategy(1));
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("Einslive", "http://1live.akacast.akamaistream.net/7/706/119434/v1/gnl.akacast.akamaistream.net/1live");
        rs.setStrategy(new EinsliveStrategy());
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("WDR 2 Rhein/Ruhr", "http://wdr-mp3-m-wdr2-duesseldorf.akacast.akamaistream.net/7/371/119456/v1/gnl.akacast.akamaistream.net/wdr-mp3-m-wdr2-duesseldorf");
        rs.setStrategy(new WDR2Strategy());
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("R.SH 80er", "http://regiocast.hoerradar.de/rsh-80er-mp3-hq");
        rs.setStrategy(new RSHStrategy(6));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Antenne Bayern 80er", "http://mp3channels.webradio.antenne.de/80er-kulthits");
        rs.setStrategy(new AntenneBayernStrategy());
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Radio Regenbogen 80er", "http://streams.regenbogen.de/rr-80er-128-mp3");
        rs.setStrategy(new RegenbogenStrategy("80er"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("FFH 80er", "http://mp3.ffh.de/ffhchannels/hq80er.mp3");
        rs.setStrategy(new FFHStrategy("80er"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("R.SH 90er", "http://regiocast.hoerradar.de/rsh-90er-mp3-hq");
        rs.setStrategy(new RSHStrategy(7));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("Radio Regenbogen 90er", "http://streams.regenbogen.de/rr-90er-128-mp3");
        rs.setStrategy(new RegenbogenStrategy("90er"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("FFH 90er", "http://mp3.ffh.de/ffhchannels/hq90er.mp3");
        rs.setStrategy(new FFHStrategy("90er"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);
    }

    public List<RadioStation> getStations() {
        return stations;
    }
}
