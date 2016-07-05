package com.digzdigital.hebronradio.schedule_recycler;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.digzdigital.hebronradio.R;

public class ScheduleChildViewHolder extends ChildViewHolder{

    public TextView scheduleDetailsTextView;

    public ScheduleChildViewHolder(View itemView){
        super(itemView);

        scheduleDetailsTextView = (TextView) itemView.findViewById(R.id.child_list_item_schedule_details);
    }

    public void bind (ScheduleChild scheduleChild){
        scheduleDetailsTextView.setText(scheduleChild.getDetails());
    }
}
