package com.digzdigital.hebronradio.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.adapter.ScheduleAdapter;
import com.digzdigital.hebronradio.model.ScheduleItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleDetails extends Fragment {
    private static final String ARG_PARAM1 = "schedule";

    private ArrayList<ScheduleItem> scheduleItems;
    private RecyclerView rv;
    private ScheduleAdapter scheduleAdapter;

    public ScheduleDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scheduleItems Parameter 1.
     * @return A new instance of fragment ScheduleDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleDetails newInstance(ArrayList<ScheduleItem> scheduleItems) {
        ScheduleDetails fragment = new ScheduleDetails();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, scheduleItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scheduleItems = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_details, container, false);
        rv = (RecyclerView) view.findViewById(R.id.weeklay);
        return view;
    }

    private void doRest() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        if (scheduleItems == null) return;
        if (scheduleItems.size() > 0) return;
        scheduleAdapter = new ScheduleAdapter(scheduleItems);
        rv.setAdapter(scheduleAdapter);

        scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });


    }
}
