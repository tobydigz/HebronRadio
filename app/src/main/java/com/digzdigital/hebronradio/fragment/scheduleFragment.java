package com.digzdigital.hebronradio.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.adapter.ScheduleAdapter;

/**
 * Created by Digz on 19/02/2016. ds
 */
public class scheduleFragment extends Fragment {
    private RecyclerView scheduleRecycler;
    private ScheduleAdapter adapter;

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


        scheduleRecycler = (RecyclerView) view.findViewById(R.id.weeklay);

        return view;

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



}