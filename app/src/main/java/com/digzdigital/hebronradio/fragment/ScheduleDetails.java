package com.digzdigital.hebronradio.fragment;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digzdigital.hebronradio.MainActivity;
import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.adapter.ScheduleAdapter;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleDetails extends Fragment {
    private static final String ARG_PARAM1 = "schedule";
    private static final String ARG_PARAM2 = "day";

    private ArrayList<ScheduleItem> scheduleItems;
    private String filePath;
    private String day;
    private RecyclerView rv;
    private ScheduleAdapter scheduleAdapter;
    private TextView dayText;

    public ScheduleDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filepath Parameter 1.
     * @param day Parameter 2.
     * @return A new instance of fragment ScheduleDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleDetails newInstance(String filepath, String day) {
        ScheduleDetails fragment = new ScheduleDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, filepath);
        args.putString(ARG_PARAM2, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filePath = getArguments().getString(ARG_PARAM1);
            day = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        rv = (RecyclerView) view.findViewById(R.id.weeklay);
        dayText = (TextView) view.findViewById(R.id.scheduleDay);
        dayText.setText(day);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        scheduleItems = loadSchedule(filePath);
        doRest();
    }

    private void doRest() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        if (scheduleItems == null) return;
        if (scheduleItems.size() == 0) return;
        scheduleAdapter = new ScheduleAdapter(scheduleItems);
        rv.setAdapter(scheduleAdapter);

        scheduleAdapter.setOnItemClickListener(new ScheduleAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });


    }


    private String readAssetsFile(String filePath) {
        AssetManager assetManager = getActivity().getAssets();
        InputStream inputStream;
        String text = null;
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = assetManager.open(filePath);
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
        String output = buf.toString();
        Log.d("DIGZ:HEBRON", output);
        return output;


    }

    private ArrayList<ScheduleItem> loadSchedule(String filepath) {
        Log.d("DIGZ:HEBRON", "I got here");
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
}
