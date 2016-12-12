package com.digzdigital.hebronradio.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.digzdigital.hebronradio.MainActivity;
import com.digzdigital.hebronradio.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by Digz on 19/02/2016.
 */
public class listenFragment extends Fragment implements View.OnClickListener {

    private ToggleButton plysngbut;
    private Button stpsng;
    private AdView adView;
    private PulsatorLayout pulsatorLayout;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra("message");
            switch (command) {
                case "uncheck":
                    try {
                        setUnclicked();
                    } catch (Exception e) {
                        Log.e("DIGZ", "not working", e);
                    }
                    break;
            }
        }
    };


    public listenFragment() {

    }

    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        View view = inflater.inflate(R.layout.fragment_listen, container, false);
        plysngbut = (ToggleButton) view.findViewById(R.id.plysng);
        plysngbut.setOnClickListener(this);

        stpsng = (Button) view.findViewById(R.id.stpsng);
        stpsng.setOnClickListener(this);

        pulsatorLayout = (PulsatorLayout) view.findViewById(R.id.pulsator);
        MobileAds.initialize(getContext(), "ca-app-pub-6610707566113750/5691382024");

        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        return view;
    }

    @Override
    public void onClick(View v) {
        MainActivity digz = (MainActivity) getActivity();
        switch (v.getId()) {
            case R.id.plysng:
                if (plysngbut.isChecked()) {
                    digz.start();
                    pulsatorLayout.start();
                }
                else {
                    digz.pause();
                    pulsatorLayout.stop();
                }
                break;
            case R.id.stpsng:
                digz.stopClick();
                setUnclicked();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("digz-event"));
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    public void setUnclicked() {
        plysngbut.setChecked(false);
        pulsatorLayout.stop();
    }
}