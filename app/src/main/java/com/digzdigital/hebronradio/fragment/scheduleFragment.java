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
    private MyPagerAdapter pagerAdapter;

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

        pagerAdapter = buildAdapter();
        setupViewPager(viewPager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter.addFragment(ScheduleDetails.newInstance("mondaySchedule.json", "Monday's Schedule"), "MONDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("tuesdaySchedule.json", "Tuesday's Schedule"), "TUESDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("wednesdaySchedule.json", "Wednesday's Schedule"), "WEDNESDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("thursdaySchedule.json", "Thursday's Schedule"), "THURSDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("fridaySchedule.json", "Friday's Schedule"), "FRIDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("saturdaySchedule.json", "Saturday's Schedule"), "SATURDAY");
        pagerAdapter.addFragment(ScheduleDetails.newInstance("sundaySchedule.json", "Sunday's Schedule"), "SUNDAY");
        viewPager.setAdapter(pagerAdapter);
    }


    private MyPagerAdapter buildAdapter() {
        return (new MyPagerAdapter(getActivity(), getChildFragmentManager()));
    }

    

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}