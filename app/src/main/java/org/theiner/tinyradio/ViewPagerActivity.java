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
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.theiner.tinyradio.adapter.MyPagerAdapter;
import org.theiner.tinyradio.async.GetCurrentSong;
import org.theiner.tinyradio.context.TinyRadioApplication;
import org.theiner.tinyradio.data.RadioKategorie;
import org.theiner.tinyradio.data.RadioStation;
import org.theiner.tinyradio.fragment.AchtzigerFragment;
import org.theiner.tinyradio.fragment.AlleStationenFragment;
import org.theiner.tinyradio.fragment.MetalFragment;
import org.theiner.tinyradio.fragment.NeunzigerFragment;
import org.theiner.tinyradio.fragment.SenderFragment;

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
            mNotificationManager.cancel(0);
            mNotificationManager.cancelAll();
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

                        // Notify fragments that data set has changed
                        notifyDataSetChanged();
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
            progress = new ProgressDialog(ViewPagerActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
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
    }
}