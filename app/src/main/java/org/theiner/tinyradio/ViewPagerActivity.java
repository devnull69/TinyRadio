package org.theiner.tinyradio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.theiner.tinyradio.adapter.MyPagerAdapter;
import org.theiner.tinyradio.async.GetCurrentSong;
import org.theiner.tinyradio.context.TinyRadioApplication;
import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.data.TrackData;
import org.theiner.tinyradio.fragment.AchtzigerFragment;
import org.theiner.tinyradio.fragment.AlleStationenFragment;
import org.theiner.tinyradio.fragment.MetalFragment;
import org.theiner.tinyradio.fragment.NeunzigerFragment;
import org.theiner.tinyradio.fragment.SenderFragment;
import org.theiner.tinyradio.util.HTTPHelper;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by TTheiner on 08.03.2017.
 */

public class ViewPagerActivity extends AppCompatActivity {
    /** maintains the pager adapter*/
    private MyPagerAdapter mPagerAdapter;

    private ViewPager vpPager;
    private MetalFragment metalFragment;
    private SenderFragment senderFragment;
    private AchtzigerFragment achtzigerFragment;
    private NeunzigerFragment neunzigerFragment;
    private AlleStationenFragment alleStationenFragment;

    private TinyRadioApplication app;
    private List<RadioStation> stations;

    private MediaPlayer mediaPlayer;
    private RadioStation currentStation = null;

    private Handler mHandler;
    private int mInterval = 40000;
    private Notification currentNotification;

    private Bitmap largeNotificationIcon = null;

    private NotificationManager mNotificationManager;
    private boolean somethingPlaying = false;

    private boolean initialTitleGrab = true;
    private ProgressDialog initialTitleProgress;

    private AudioManager mAudioManager;
    private RemoteControlClient mRemoteControlClient;
    private MediaSession mMediaSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.view_pager_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(RadioKategorie.Metal.getDescription()));
        tabLayout.addTab(tabLayout.newTab().setText(RadioKategorie.Achtziger.getDescription()));
        tabLayout.addTab(tabLayout.newTab().setText(RadioKategorie.Neunziger.getDescription()));
        tabLayout.addTab(tabLayout.newTab().setText(RadioKategorie.Sender.getDescription()));
        tabLayout.addTab(tabLayout.newTab().setText("Alle"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Bei bestehender Netzwerkverbindung:
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            largeNotificationIcon = getLargeIcon();

            vpPager = (ViewPager) findViewById(R.id.vpPager);

            app = (TinyRadioApplication) getApplicationContext();
            app.initRadioStations();
            stations = app.getStations();

            //initialsie the pager
            this.initialisePaging();

            vpPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    vpPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            mHandler = new Handler();
            startRepeatingTask();

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Phone call listener
            PhoneCallListener callListener = new PhoneCallListener();
            TelephonyManager mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        else {
            //Keine Netzwerkverbindung

            Toast.makeText(this, "Es konnte keine Internetverbindung festgestellt werden.", Toast.LENGTH_LONG).show();
        }

        // Init bluetooth audio for metadata
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (mRemoteControlClient == null) {
                Log.d("init()", "API " + Build.VERSION.SDK_INT + " lower then " + Build.VERSION_CODES.LOLLIPOP);
                Log.d("init()", "Using RemoteControlClient API.");

                mRemoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this, 0, new Intent(Intent.ACTION_MEDIA_BUTTON), 0));
                mAudioManager.registerRemoteControlClient(mRemoteControlClient);
            }
        }
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        metalFragment = (MetalFragment )Fragment.instantiate(this, MetalFragment.class.getName());
        fragments.add(metalFragment);
        achtzigerFragment = (AchtzigerFragment ) Fragment.instantiate(this, AchtzigerFragment.class.getName());
        fragments.add(achtzigerFragment);
        neunzigerFragment = (NeunzigerFragment) Fragment.instantiate(this, NeunzigerFragment.class.getName());
        fragments.add(neunzigerFragment);
        senderFragment = (SenderFragment )Fragment.instantiate(this, SenderFragment.class.getName());
        fragments.add(senderFragment);
        alleStationenFragment = (AlleStationenFragment) Fragment.instantiate(this, AlleStationenFragment.class.getName());
        fragments.add(alleStationenFragment);
        this.mPagerAdapter  = new MyPagerAdapter(getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) findViewById(R.id.vpPager);
        pager.setAdapter(mPagerAdapter);

        pager.setOffscreenPageLimit(fragments.size());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(currentStation != null) {
            switch (currentStation.getRadioKategorie().getDescription()) {
                case ("Metal"):
                    vpPager.setCurrentItem(0, false);
                    metalFragment.scrollIntoView(currentStation);
                    break;
                case ("80er"):
                    vpPager.setCurrentItem(1, false);
                    achtzigerFragment.scrollIntoView(currentStation);
                    break;
                case ("90er"):
                    vpPager.setCurrentItem(2, false);
                    neunzigerFragment.scrollIntoView(currentStation);
                    break;
                case ("Sender"):
                    vpPager.setCurrentItem(3, false);
                    senderFragment.scrollIntoView(currentStation);
                    break;
                default:
                    vpPager.setCurrentItem(0, false);
            }
            if (somethingPlaying)
                renewNotification();
        }
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
            if(mNotificationManager != null) {
                mNotificationManager.cancel(0);
                mNotificationManager.cancelAll();
            }
//            stopRepeatingTask();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void notifyStationClicked(RadioStation selected) {
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

        somethingPlaying = true;

        if (!currentStation.isPlaying()) {
            currentStation.setPlaying(true);
            // Notify fragments that data set has changed
            notifyDataSetChanged();

            if (currentStation.isInitialState())
                new Player()
                        .execute(currentStation.getUrl());
            else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
        } else {
            currentStation.setPlaying(false);
            // Notify fragments that data set has changed
            notifyDataSetChanged();

            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            somethingPlaying = false;
        }

        if(somethingPlaying) {
            renewNotification();
        } else {
            mNotificationManager.cancel(0);
            mNotificationManager.cancelAll();
        }
    }

    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     * @author piyush
     *
     */

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean prepared;
            try {

                HTTPHelper helper = new HTTPHelper();
                helper.checkConnectionOrThrow(params[0], 7000);

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        currentStation.setInitialState(true);
                        currentStation.setPlaying(false);
                        mediaPlayer.stop();
                        mediaPlayer.reset();

                        // reconnect after completion
                        notifyStationClicked(currentStation);
                    }
                });

                mediaPlayer.prepare();
                prepared = true;
            } catch (InterruptedException e) {
                prepared = false;
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Log.d("IllegalArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            if(result) {
                mediaPlayer.start();
                currentStation.setInitialState(false);
            } else {
                somethingPlaying = false;
                currentStation.setPlaying(false);
                notifyDataSetChanged();
                Toast.makeText(ViewPagerActivity.this, "Radio station does not respond!", Toast.LENGTH_LONG).show();
            }
        }

        public Player() {
            progress = new ProgressDialog(ViewPagerActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.setCancelable(false);
            this.progress.show();

        }

    }

    private Bitmap getLargeIcon() {
        Bitmap myLargeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float multiplier = metrics.density/3f;   // Bitmap liegt mit 480dpi vor (density Faktor 3), die Bildschirmauflösung kann aber geringer sein
        myLargeIcon = Bitmap.createScaledBitmap(myLargeIcon, (int)(myLargeIcon.getWidth()*multiplier), (int)(myLargeIcon.getHeight()*multiplier), false);

        return myLargeIcon;
    }

    private void notifyDataSetChanged() {
        metalFragment.notifyDataSetChanged();
        senderFragment.notifyDataSetChanged();
        achtzigerFragment.notifyDataSetChanged();
        neunzigerFragment.notifyDataSetChanged();
        alleStationenFragment.notifyDataSetChanged();
    }

    Runnable mGetSongTitle = new Runnable() {
        @Override
        public void run() {
            try {
                // Infos aller RadioStationen holen
                GetCurrentSong.CheckCompleteListener ccl = new GetCurrentSong.CheckCompleteListener() {
                    @Override
                    public void onCheckComplete(String result) {
                        if (initialTitleGrab && initialTitleProgress.isShowing()) {
                            initialTitleProgress.cancel();
                        }
                        initialTitleGrab = false;
                        notifyDataSetChanged();
                        // show notification
                        if(currentStation != null && somethingPlaying) {
                            renewNotification();
                        }
                    }
                };

                if(initialTitleGrab) {
                    initialTitleProgress = new ProgressDialog(ViewPagerActivity.this);
                    initialTitleProgress.setMessage("Getting song titles...");
                    initialTitleProgress.setCancelable(false);
                    initialTitleProgress.show();
                }

                GetCurrentSong myTask = new GetCurrentSong(ccl);
                myTask.execute(stations.toArray());
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

    private void renewNotification() {
        // show notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.smallicon) // notification icon
                .setLargeIcon(largeNotificationIcon)
                .setContentTitle(currentStation.getTitle()) // title for notification
                .setContentText(currentStation.getName()) // message for notification
                .setAutoCancel(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // clear notification after click
        Intent intent = new Intent(this, ViewPagerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        currentNotification = mBuilder.build();
        currentNotification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(0, currentNotification);

        // eventually send meta information to bluetooth device
        bluetoothNotifyChange(new TrackData(currentStation.getTitle(), currentStation.getName()));
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                isPhoneCalling = true;
                if(currentStation != null)
                    notifyStationClicked(currentStation);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                isPhoneCalling = true;
                if(somethingPlaying && currentStation != null)
                    notifyStationClicked(currentStation);
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {

                if (isPhoneCalling) {
                    if(currentStation != null)
                        notifyStationClicked(currentStation);
                    isPhoneCalling = false;
                }

            }


        }
    }

    private void bluetoothNotifyChange(TrackData td) {
        String title = td.getTrackName();
        String artist = td.getStationName();
        String album = td.getArtistName();
        long duration = 240000;
        long trackNumber = 1;
        long position = 1;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED, 0, 1.0f);

            RemoteControlClient.MetadataEditor ed = mRemoteControlClient.editMetadata(true);
            ed.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title);
            ed.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, artist);
            ed.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, album);
            ed.putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, duration);
            ed.putLong(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER, trackNumber);
            ed.apply();

            mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING, position, 1.0f);
        } else {

            if(mMediaSession != null)
                mMediaSession.release();
            mMediaSession = null;
            mMediaSession = new MediaSession(this, "PlayerServiceMediaSession");
            mMediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setActive(true);

            MediaMetadata metadata = new MediaMetadata.Builder()
                    .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                    .putString(MediaMetadata.METADATA_KEY_ALBUM, album)
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, duration)
                    .putLong(MediaMetadata.METADATA_KEY_TRACK_NUMBER, trackNumber)
                    .build();

            mMediaSession.setMetadata(metadata);

            PlaybackState state = new PlaybackState.Builder()
                    .setActions(PlaybackState.ACTION_PLAY)
                    .setState(PlaybackState.STATE_PLAYING, position, 1.0f, SystemClock.elapsedRealtime())
                    .build();

            mMediaSession.setPlaybackState(state);
        }
    }
}