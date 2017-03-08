package org.theiner.tinyradio.context;

import android.app.Application;

import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.strategy.RadioDEStrategy;
import org.theiner.tinyradio.strategy.EinsliveStrategy;
import org.theiner.tinyradio.strategy.FFHStrategy;
import org.theiner.tinyradio.strategy.LautFMStrategy;
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

        rs = new RadioStation("Metal Hammer", "http://stream1.laut.fm/metal-hammer");
        rs.setStrategy(new LautFMStrategy("metal-hammer"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Up the Irons", "http://stream1.laut.fm/up_the_irons");
        rs.setStrategy(new LautFMStrategy("up_the_irons"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Metal Invasion", "http://streaming.radionomy.com/Metal-Invasion-Radio");
        rs.setStrategy(new RadioDEStrategy("27038"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Metal Only", "http://server1.blitz-stream.de:4400/;080794759699378stream.nsv");
        rs.setStrategy(new RadioDEStrategy("4696"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Stahlradio", "http://streamplus52.leonex.de:22810/;940438803932481stream.nsv");
        rs.setStrategy(new RadioDEStrategy("10416"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Rockerportal", "http://stream1.laut.fm/rockerportal");
        rs.setStrategy(new LautFMStrategy("rockerportal"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Motorbreath", "http://stream1.laut.fm/motorbreath");
        rs.setStrategy(new LautFMStrategy("motorbreath"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Metalstation", "http://stream1.laut.fm/metalstation");
        rs.setStrategy(new LautFMStrategy("metalstation"));
        rs.setRadioKategorie(RadioKategorie.Metal);
        stations.add(rs);

        rs = new RadioStation("Metal FM", "http://stream1.laut.fm/metal-fm-com");
        rs.setStrategy(new LautFMStrategy("metal-fm-com"));
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

        rs = new RadioStation("SWR 3", "http://swr-mp3-m-swr3.akacast.akamaistream.net/7/720/137136/v1/gnl.akacast.akamaistream.net/swr-mp3-m-swr3");
        rs.setStrategy(new RadioDEStrategy("2275"));
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("NDR 2", "http://ndr-ndr2-nds-mp3.akacast.akamaistream.net/7/400/252763/v1/gnl.akacast.akamaistream.net/ndr_ndr2_nds_mp3");
        rs.setStrategy(new RadioDEStrategy("2262"));
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("Bayern 3", "http://br-mp3-bayern3-s.akacast.akamaistream.net/7/464/142692/v1/gnl.akacast.akamaistream.net/br_mp3_bayern3_s");
        rs.setStrategy(new RadioDEStrategy("2247"));
        rs.setRadioKategorie(RadioKategorie.Sender);
        stations.add(rs);

        rs = new RadioStation("R.SH 80er", "http://regiocast.hoerradar.de/rsh-80er-mp3-hq");
        rs.setStrategy(new RSHStrategy(6));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Antenne Bayern 80er", "http://mp3channels.webradio.antenne.de/80er-kulthits");
        rs.setStrategy(new RadioDEStrategy("9123"));
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

        rs = new RadioStation("Laut FM 80er", "http://stream1.laut.fm/80er");
        rs.setStrategy(new LautFMStrategy("80er"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Best of 80s - Jede Stunde ein anderes Jahr", "http://stream1.laut.fm/best_of_80s");
        rs.setStrategy(new LautFMStrategy("best_of_80s"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("0-24 80er Pop Rock", "http://stream1.laut.fm/0-24_80er_pop_rock");
        rs.setStrategy(new LautFMStrategy("0-24_80er_pop_rock"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Move On", "http://stream1.laut.fm/move_on");
        rs.setStrategy(new LautFMStrategy("move_on"));
        rs.setRadioKategorie(RadioKategorie.Achtziger);
        stations.add(rs);

        rs = new RadioStation("Just 80s", "http://stream1.laut.fm/just80s");
        rs.setStrategy(new LautFMStrategy("just80s"));
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

        rs = new RadioStation("Best of 90s", "http://stream1.laut.fm/best_of_90s");
        rs.setStrategy(new LautFMStrategy("best_of_90s"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("Germanradio 90er", "http://germanradio.info:13300/;481065546845065stream.nsv");
        rs.setStrategy(new RadioDEStrategy("33218"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("Wunschradio FM", "http://server74.radiostreamserver.de/wunschradio-90er.mp3");
        rs.setStrategy(new RadioDEStrategy("21468"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("Tuner M1 90er", "http://tuner.m1.fm/90er.mp3");
        rs.setStrategy(new RadioDEStrategy("40894"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

        rs = new RadioStation("Vienna.AT", "http://webradio.vienna.at/vie-90er");
        rs.setStrategy(new RadioDEStrategy("35386"));
        rs.setRadioKategorie(RadioKategorie.Neunziger);
        stations.add(rs);

    }

    public List<RadioStation> getStations() {
        return stations;
    }
}
