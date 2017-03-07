package org.theiner.tinyradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.theiner.tinyradio.adapter.RadioStationAdapter;
import org.theiner.tinyradio.async.GetCurrentSong;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.strategy.AntenneBayernStrategy;
import org.theiner.tinyradio.strategy.EinsliveStrategy;
import org.theiner.tinyradio.strategy.RSHStrategy;
import org.theiner.tinyradio.strategy.Radio912Strategy;
import org.theiner.tinyradio.strategy.RockAntenneStrategy;
import org.theiner.tinyradio.strategy.WDR2Strategy;
import org.theiner.tinyradio.strategy.WackenRadioStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private List<RadioStation> stations = new ArrayList<>();
    private RadioStation currentStation = null;

    private ListView lvStations = null;
    private BaseAdapter adapter = null;

    private Handler mHandler;
    private int mInterval = 40000;
    private Notification currentNotification;

    private Context me;

    private Bitmap largeNotificationIcon = null;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        lvStations = (ListView) findViewById(R.id.lvStations);

        initRadioStations();

        adapter = new RadioStationAdapter(this, stations);
        lvStations.setAdapter(adapter);

        lvStations.setOnItemClickListener(clicklistener);

        largeNotificationIcon = getLargeIcon();

        mHandler = new Handler();
        startRepeatingTask();

        me = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);    }

    private void initRadioStations() {

        RadioStation rs;

        rs = new RadioStation("Rock Antenne Heavy Metal", "https://mp3channels.webradio.antenne.de/heavy-metal");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new RockAntenneStrategy());
        stations.add(rs);

        rs = new RadioStation("Wacken Radio", "http://138.201.248.93/wackenradio");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new WackenRadioStrategy());
        stations.add(rs);

        rs = new RadioStation("Radio 91.2", "http://server2.lokalradioserver.de:8000/xstream");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new Radio912Strategy());
        stations.add(rs);

        rs = new RadioStation("R.SH", "http://regiocast.hoerradar.de/rsh128");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new RSHStrategy(1));
        stations.add(rs);

        rs = new RadioStation("Einslive", "http://1live.akacast.akamaistream.net/7/706/119434/v1/gnl.akacast.akamaistream.net/1live");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new EinsliveStrategy());
        stations.add(rs);

        rs = new RadioStation("WDR 2 Rhein/Ruhr", "http://wdr-mp3-m-wdr2-duesseldorf.akacast.akamaistream.net/7/371/119456/v1/gnl.akacast.akamaistream.net/wdr-mp3-m-wdr2-duesseldorf");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new WDR2Strategy());
        stations.add(rs);

        rs = new RadioStation("R.SH 80er", "http://regiocast.hoerradar.de/rsh-80er-mp3-mq");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new RSHStrategy(6));
        stations.add(rs);

        rs = new RadioStation("Antenne Bayern 80er", "http://chip.digidip.net/visit?url=http%3A%2F%2Fmp3channels.webradio.antenne.de%2F80er-kulthits&ppref=https%3A%2F%2Fwww.google.de%2F");
        rs.setTitle("Unbekannt");
        rs.setStrategy(new AntenneBayernStrategy());
        stations.add(rs);
    }


    Runnable mGetSongTitle = new Runnable() {
        @Override
        public void run() {
            try {
                // Infos aller RadioStationen holen
                for(RadioStation station: stations) {
                    GetCurrentSong.CheckCompleteListener ccl = new GetCurrentSong.CheckCompleteListener() {
                        @Override
                        public void onCheckComplete(String result) {
                        adapter.notifyDataSetChanged();
                        // show notification
                        if(currentStation != null) {
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(me)
                                    .setSmallIcon(R.mipmap.smallicon) // notification icon
                                    .setLargeIcon(largeNotificationIcon)
                                    .setContentTitle(currentStation.getTitle()) // title for notification
                                    .setContentText(currentStation.getName()) // message for notification
                                    .setAutoCancel(false)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // clear notification after click
                            Intent intent = new Intent(me, MainActivity.class);
                            PendingIntent pi = PendingIntent.getActivity(me, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setContentIntent(pi);
                            currentNotification = mBuilder.build();
                            currentNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
                            mNotificationManager.notify(0, currentNotification);
                        }
                        }
                    };

                    GetCurrentSong myTask = new GetCurrentSong(ccl);
                    myTask.execute(station);
                }
            } finally {
                mHandler.postDelayed(mGetSongTitle, mInterval);
            }
        }
    };

    private void startRepeatingTask() {
        mGetSongTitle.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mGetSongTitle);
    }

    private AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> listview, View view, int position, long l) {
            RadioStation selected = (RadioStation) listview.getItemAtPosition(position);

            // Alten Stream stoppen
            if(currentStation != null && selected != currentStation && !currentStation.isInitialState()) {
                currentStation.setInitialState(true);
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();

                currentStation.setPlaying(false);
                selected.setPlaying(false);
            }

            currentStation = selected;

            if (!currentStation.isPlaying()) {
                currentStation.setPlaying(true);
                if (currentStation.isInitialState())
                    new Player()
                            .execute(currentStation.getUrl());
                else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                }
            } else {
                currentStation.setPlaying(false);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
            }

            adapter.notifyDataSetChanged();

            // show notification
            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(me)
                    .setSmallIcon(R.mipmap.smallicon) // notification icon
                    .setLargeIcon(largeNotificationIcon)
                    .setContentTitle(currentStation.getTitle()) // title for notification
                    .setContentText(currentStation.getName()) // message for notification
                    .setAutoCancel(false)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // clear notification after click
            Intent intent = new Intent(me, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(me, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            currentNotification = mBuilder.build();
            currentNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            mNotificationManager.notify(0, currentNotification);
        }
    };

    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     * @author piyush
     *
     */

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        currentStation.setInitialState(true);
                        currentStation.setPlaying(false);
                        mediaPlayer.stop();
                        mediaPlayer.reset();

                        adapter.notifyDataSetChanged();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();

            currentStation.setInitialState(false);
        }

        public Player() {
            progress = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();

        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(0);
        mNotificationManager.cancelAll();
        stopRepeatingTask();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_close) {
            mNotificationManager.cancel(0);
            mNotificationManager.cancelAll();
            stopRepeatingTask();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap getLargeIcon() {
        Bitmap myLargeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float multiplier = metrics.density/3f;   // Bitmap liegt mit 480dpi vor (density Faktor 3), die Bildschirmauflösung kann aber geringer sein
        myLargeIcon = Bitmap.createScaledBitmap(myLargeIcon, (int)(myLargeIcon.getWidth()*multiplier), (int)(myLargeIcon.getHeight()*multiplier), false);

        return myLargeIcon;
    }
}
