package com.digzdigital.hebronradio.schedule_recycler;


import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.digzdigital.hebronradio.R;

public class ScheduleParentViewHolder extends ParentViewHolder {

    public TextView scheduleDayTextView;
    public ImageButton mDropDownArrow;

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    public ScheduleParentViewHolder(View itemView) {
        super(itemView);

        scheduleDayTextView = (TextView) itemView.findViewById(R.id.parent_list_item_schedule_day);
        mDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_expand_arrow);
    }

    public void bind (Schedule schedule){
        scheduleDayTextView.setText(schedule.getTitle());
    }



}
