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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digzdigital.hebronradio.MusicService.MusicBinder;
import com.digzdigital.hebronradio.fragment.AboutFragment;
import com.digzdigital.hebronradio.fragment.ListenFragment;
import com.digzdigital.hebronradio.fragment.ScheduleFragment;
import com.digzdigital.hebronradio.fragment.TwitterFragment;
import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final String JSON_URL = "http://hebronfm.azurewebsites.net/updhebron.php";
    private SharedPreferences mPrefs;
    private FragmentManager fragmentManager;
    private Fragment listenFragment, aboutFragment, scheduleFragment, twitterFragment;
    //Declare declarables
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean paused = false, playBackPaused = false;
    private ParseJSON pj;
    private String trackUri, trackUri2;
    private FirebaseAnalytics firebaseAnalytics;


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
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        listenFragment = new ListenFragment();
        startFragment(listenFragment);


        new bgWork().execute(getApplicationContext());
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void updateLink() {
        SharedPreferences.Editor edit2 = mPrefs.edit();
        edit2.putString("link1", trackUri);
        edit2.putString("link2", trackUri2);
        edit2.commit();
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

    private void useJSON(String json) {
        pj = new ParseJSON(json);
        pj.parseJSON();

        trackUri = "http://" + ParseJSON.trackUri + "/;stream.mp3";
        trackUri2 = "http://" + ParseJSON.trackUri2 + "/;stream.mp3";


    }

    // private void setupViewPager(ViewPager viewPager) {
    //     ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    //     adapter.addFragment(new ListenFragment(), "LISTEN NOW");
    //     adapter.addFragment(new ScheduleFragment(), "SCHEDULE");
    //     adapter.addFragment(new AboutFragment(), "ABOUT US");
    //     viewPager.setAdapter(adapter);
    // }

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
            case R.id.action_refresh:
                sendRequest();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void start() {
        musicSrv.playSong();
        playBackPaused = false;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Play Event");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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
        String phone = "08180696683";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestCallingPermission(MY_PERMISSIONS_REQUEST_CALL_PHONE);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            startActivity(callIntent);
            return;
        } else {
            startActivity(callIntent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void requestCallingPermission(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Display a dialog with rationale.
            PermissionUtils.RationaleDialog
                    .newInstance(requestCode, false).show(
                    getSupportFragmentManager(), "dialog");
        } else {
            // Location permission has not been granted yet, request it.
            PermissionUtils.requestPermission(this, requestCode,
                    Manifest.permission.CALL_PHONE, false);
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

    public void showAds() {
//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.nav_listen:
                if (listenFragment == null) listenFragment = new ListenFragment();
                startFragment(listenFragment);
                break;
            case R.id.nav_schedule:
                if (scheduleFragment == null) scheduleFragment = new ScheduleFragment();
                startFragment(scheduleFragment);
                break;
            case R.id.nav_about:
                if (aboutFragment == null) aboutFragment = new AboutFragment();
                startFragment(aboutFragment);
                break;
            case R.id.nav_twitter:
                if (twitterFragment == null) twitterFragment = new TwitterFragment();
                startFragment(twitterFragment);
                break;
            case R.id.nav_exit:
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startFragment(Fragment fragment){
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }
    /*class ViewPagerAdapter extends FragmentPagerAdapter {
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
    }*/


    private class bgWork extends AsyncTask<Context, Void, Void> {

        private Context mContext;

        @Override
        protected Void doInBackground(Context... params) {
            //Taking care of shared  Prefs
            mContext = params[0];
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

            return null;
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
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }
    }
}
