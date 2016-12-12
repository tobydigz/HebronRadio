package com.digzdigital.hebronradio.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.adapter.ScheduleAdapter;
import com.digzdigital.hebronradio.fragment.viewpager.MyPagerAdapter;
import com.digzdigital.hebronradio.fragment.viewpager.ZoomOutPageTransformer;
import com.digzdigital.hebronradio.model.ScheduleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Digz on 19/02/2016. ds
 */
public class ScheduleFragment extends Fragment {
    private static final int NUM_PAGES = 7;
    private RecyclerView scheduleRecycler;
    private ScheduleAdapter adapter;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public ScheduleFragment() {

    }


    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        View view = inflater.inflate(R.layout.fragment_schedule_parent, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.scheduleContentFrame);
        viewPager.setAdapter(buildAdapter());

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        return view;

    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity(), getChildFragmentManager());
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("mondaySchedule.json")), "MONDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("tuesdaySchedule.json")), "TUESDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("wednesdaySchedule.json")), "WEDNESDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("thursdaySchedule.json")), "THURSDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("fridaySchedule.json")), "FRIDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("saturdaySchedule.json")), "SATURDAY");
        adapter.addFragment(ScheduleDetails.newInstance(loadSchedule("sundaySchedule.json")), "SUNDAY");
        viewPager.setAdapter(adapter);
    }



    private PagerAdapter buildAdapter() {
        return (new MyPagerAdapter(getActivity(), getChildFragmentManager()));
    }

    private String readAssetsFile(String filePath) {
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = getActivity().getAssets().open(filePath);
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    private ArrayList<ScheduleItem> loadSchedule(String filepath) {
        ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(readAssetsFile(filepath));
            JSONObject day = jsonObject.getJSONObject("Day");
            JSONArray items = day.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject object = items.getJSONObject(i);
                String name = object.getString("event");
                String time = object.getString("time");
                ScheduleItem scheduleItem = new ScheduleItem(i, name, time);
                scheduleItems.add(scheduleItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return scheduleItems;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}