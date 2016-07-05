package com.digzdigital.hebronradio.schedule_recycler;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Digz on 28/04/2016.
 */
public class scheduleLab {
    private static scheduleLab sScheduleLab;

    private ArrayList<Schedule> mSchedule;

    /*public static scheduleLab get(Context context) {
        if (sScheduleLab == null) {
            sScheduleLab = new scheduleLab(context);
        }
        return sScheduleLab;
    }

    private scheduleLab(Context context) {
        mSchedule = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Schedule schedule = new Schedule();
            String schedTitle = "0";

            switch (i) {
                case 0:
                    schedTitle = "Monday";
                    break;
                case 1:
                    schedTitle = "Tuesday";
                    break;
                case 2:
                    schedTitle = "Wednesday";
                    break;
                case 3:
                    schedTitle = "Thursday";
                    break;
                case 4:
                    schedTitle = "Friday";
                    break;
                case 5:
                    schedTitle = "Saturday";
                    break;
                case 6:
                    schedTitle = "Sunday";
                    break;
            }
            schedule.setTitle(schedTitle);
            mSchedule.add(schedule);
        }
    }


    public List<Schedule> getSchedules(){
        return mSchedule;
    }

    public Schedule getSchedule(UUID id){
        for (Schedule schedule : mSchedule){
            if (schedule.getId().equals(id)){
                return schedule;
            }
        }
        return null;
    }*/
}
