package com.digzdigital.hebronradio;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digzdigital.hebronradio.MusicService.MusicBinder;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "http://hebronfm.azurewebsites.net/updhebron.php";
    SharedPreferences mPrefs;
    //Declare declarables
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean paused = false, playBackPaused = false;
    private ToggleButton plysngbut;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ParseJSON pj;
    private String trackUri, trackUri2;
    private Tracker digzTracker;


    //Connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;

            //get service
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        //Taking care of shared  Prefs
        Context mContext = this.getApplicationContext();
        mPrefs = mContext.getSharedPreferences("hebronfm", 0);

        //Taking care of first run
        if (getFirstRun()) {
            setRunned();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("link1", "http://10.0.3.21:8000/;stream.mp3");
            editor.putString("link2", "http://80.248.0.231:8000/;stream.mp3");
            editor.commit();
        } else {

        }
        sendRequest();

        DigzAnalytics application = (DigzAnalytics) getApplication();
        digzTracker = application.getDefaultTracker();


    }

    private void updateLink() {
        SharedPreferences.Editor edit2 = mPrefs.edit();
        edit2.putString("link1", trackUri);
        edit2.putString("link2", trackUri2);
        edit2.commit();
    }

    public boolean getFirstRun() {
        return mPrefs.getBoolean("firstRun", true);
    }

    public void setRunned() {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean("firstRun", false);
        edit.commit();
    }

    private void useJSON(String json) {
        pj = new ParseJSON(json);
        pj.parseJSON();

        trackUri = "http://" + ParseJSON.trackUri + "/;stream.mp3";
        trackUri2 = "http://" + ParseJSON.trackUri2 + "/;stream.mp3";


    }

    private void sendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        useJSON(response);
                        updateLink();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new listenFragment(), "LISTEN NOW");
        adapter.addFragment(new scheduleFragment(), "SCHEDULE");
        adapter.addFragment(new aboutFragment(), "ABOUT US");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
        // Register mMessageReceiver to receive messages.

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_end:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
            case R.id.action_refresh:
                sendRequest();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start() {
        musicSrv.playSong();
        playBackPaused = false;

        digzTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action").setAction("Play").build());
    }

    public void pause() {
        playBackPaused = true;
        musicSrv.pausePlayer();
    }

    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    public boolean onKeyDown(int Keycode, KeyEvent event) {
        if (Keycode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(Keycode, event);
    }

    public void getTwitter() {
        Intent intent3 = null;
        try {
            // get the Twitter app if possible
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1575487230"));
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/hebronfm"));
        }
        this.startActivity(intent3);
    }

    public void getInstagram() {
        Uri uri = Uri.parse("http://instagram.com/_u/hebronfm959");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/hebronfm95.9")));
        }
    }


    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        String FACEBOOK_URL = "https://www.facebook.com/CUhebronFM";
        String FACEBOOK_PAGE_ID = "397402167047905";
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public void getFacebook() {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(this);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

    public void call() {
        String phone = String.valueOf(R.string.dialler);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            startActivity(callIntent);
            return;
        }

    }


    /*public void startClick() {
            start();
    }*/

    public void stopClick() {
        if (!playBackPaused) {
            musicSrv.stpPly();


        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
