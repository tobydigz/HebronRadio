package com.digzdigital.hebronradio.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digzdigital.hebronradio.R;
import com.mopub.nativeads.MoPubAdAdapter;
import com.twitter.sdk.android.mopub.TwitterMoPubAdAdapter;
import com.twitter.sdk.android.mopub.TwitterStaticNativeAdRenderer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFragment extends ListFragment {

    private MoPubAdAdapter moPubAdAdapter;

    public TwitterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);
        initTwitterTimeline();
        return view;
    }

    private void initTwitterTimeline() {
        final UserTimeline userTimeline = new UserTimeline.Builder().screenName("HEBRONFM").build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .build();

        moPubAdAdapter = new TwitterMoPubAdAdapter(getActivity(), adapter);
        final TwitterStaticNativeAdRenderer adRenderer = new TwitterStaticNativeAdRenderer();
        moPubAdAdapter.registerAdRenderer(adRenderer);
        moPubAdAdapter.loadAds("8eab1de41d0c4fa5a57b245cc86b0c9c");

        setListAdapter(moPubAdAdapter);
    }

    @Override
    public void onDestroy() {
        // You must call this or the ad adapter may cause a memory leak
        moPubAdAdapter.destroy();
        super.onDestroy();
    }
}
