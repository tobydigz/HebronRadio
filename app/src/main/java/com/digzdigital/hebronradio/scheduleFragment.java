package com.digzdigital.hebronradio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.digzdigital.hebronradio.schedule_recycler.Schedule;
import com.digzdigital.hebronradio.schedule_recycler.ScheduleAdapter;
import com.digzdigital.hebronradio.schedule_recycler.ScheduleChild;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Digz on 19/02/2016. ds
 */
public class scheduleFragment extends Fragment {
    private RecyclerView scheduleRecycler;
    private ScheduleAdapter mScheduleAdapter;
    private List<Schedule> schedules;

    public scheduleFragment() {

    }

    public static Intent newIntent(Context context) {
        return new Intent(context, scheduleFragment.class);
    }


    @Override
    public void onCreate(Bundle a) {
        super.onCreate(a);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle a) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        generateSchedule();

        scheduleRecycler = (RecyclerView) view.findViewById(R.id.weeklay);


        mScheduleAdapter = new ScheduleAdapter(getActivity(), schedules);
        mScheduleAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                Schedule expandedSchedule = schedules.get(position);
            }

            @Override
            public void onListItemCollapsed(int position) {
                Schedule collapsedSchedule = schedules.get(position);

            }
        });

        scheduleRecycler.setAdapter(mScheduleAdapter);
        scheduleRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));



        /*ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(getActivity(), generateSchedule());
        mScheduleAdapter.onRestoreInstanceState(a);

        */
        return view;

    }

    /*private List<ParentListItem> generateSchedule() {
        scheduleLab mscheduleLab = scheduleLab.get(getActivity());
        List<Schedule> schedules = mscheduleLab.getSchedules();
        String dayArray = "0";
        String day = "nada";
        List<ParentListItem> parentListItems = new ArrayList<>();
        for (Schedule schedule : schedules) {
            List<ScheduleChild> childList = new ArrayList<>();
            day = schedule.getTitle();
            switch (day) {
                case "Monday":
                    dayArray = getResources().getString(R.string.mon_ar);
                    break;
                case "Tuesday":
                    dayArray = getResources().getString(R.string.tue_ar);
                    break;
                case "Wednesday":
                    dayArray = getResources().getString(R.string.wed_ar);
                    break;
                case "Thursday":
                    dayArray = getResources().getString(R.string.thu_ar);
                    break;
                case "Friday":
                    dayArray = getResources().getString(R.string.fri_ar);
                    break;
                case "Saturday":
                    dayArray = getResources().getString(R.string.sat_ar);
                    break;
                case "Sunday":
                    dayArray = getResources().getString(R.string.sun_ar);
                    break;
            }
            childList.add(new ScheduleChild(dayArray));
            schedule.setChildItemList(childList);
            parentListItems.add(schedule);
        }
        return parentListItems;
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((ScheduleAdapter) scheduleRecycler.getAdapter()).onSaveInstanceState(outState);
    }

    private void generateSchedule() {
        String monDet = getResources().getString(R.string.mon_ar);
        String tueDet = getResources().getString(R.string.tue_ar);
        String wedDet = getResources().getString(R.string.wed_ar);
        String thuDet = getResources().getString(R.string.thu_ar);
        String friDet = getResources().getString(R.string.fri_ar);
        String satDet = getResources().getString(R.string.sat_ar);
        String sunDet = getResources().getString(R.string.sun_ar);

        ScheduleChild monday = new ScheduleChild(monDet);
        ScheduleChild tuesday = new ScheduleChild(tueDet);
        ScheduleChild wednesday = new ScheduleChild(wedDet);
        ScheduleChild thursday = new ScheduleChild(thuDet);
        ScheduleChild friday = new ScheduleChild(friDet);
        ScheduleChild saturday = new ScheduleChild(satDet);
        ScheduleChild sunday = new ScheduleChild(sunDet);

        Schedule Monday = new Schedule("Monday", Collections.singletonList(monday));
        Schedule Tuesday = new Schedule("Tuesday", Collections.singletonList(tuesday));
        Schedule Wednesday = new Schedule("Wednesday", Collections.singletonList(wednesday));
        Schedule Thursday = new Schedule("Thursday", Collections.singletonList(thursday));
        Schedule Friday = new Schedule("Friday", Collections.singletonList(friday));
        Schedule Saturday = new Schedule("Saturday", Collections.singletonList(saturday));
        Schedule Sunday = new Schedule("Sunday", Collections.singletonList(sunday));

        schedules = Arrays.asList(Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday);
    }
}