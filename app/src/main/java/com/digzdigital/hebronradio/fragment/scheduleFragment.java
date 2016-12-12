package com.digzdigital.hebronradio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.fragment.viewpager.MyPagerAdapter;
import com.digzdigital.hebronradio.fragment.viewpager.ZoomOutPageTransformer;

/**
 * Created by Digz on 19/02/2016. ds
 */
public class ScheduleFragment extends Fragment {
    private static final int NUM_PAGES = 7;
    private ViewPager viewPager;

    public ScheduleFragment() {

    }


    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        View view = inflater.inflate(R.layout.fragment_schedule_parent, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.scheduleContentFrame);

        return view;

    }

    @Override
    public void onStart(){
        super.onStart();
        setupViewPager(viewPager);
        viewPager.setAdapter(buildAdapter());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity(), getChildFragmentManager());
        adapter.addFragment(ScheduleDetails.newInstance(("mondaySchedule.json")), "MONDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("tuesdaySchedule.json")), "TUESDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("wednesdaySchedule.json")), "WEDNESDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("thursdaySchedule.json")), "THURSDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("fridaySchedule.json")), "FRIDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("saturdaySchedule.json")), "SATURDAY");
        adapter.addFragment(ScheduleDetails.newInstance(("sundaySchedule.json")), "SUNDAY");
        viewPager.setAdapter(adapter);
    }


    private PagerAdapter buildAdapter() {
        return (new MyPagerAdapter(getActivity(), getChildFragmentManager()));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}