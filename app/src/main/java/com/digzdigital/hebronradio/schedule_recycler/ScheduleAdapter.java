package com.digzdigital.hebronradio.schedule_recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.digzdigital.hebronradio.R;

import java.util.List;

/**
 * Created by Digz on 24/04/2016.uj
 */
public class ScheduleAdapter extends ExpandableRecyclerAdapter<ScheduleParentViewHolder, ScheduleChildViewHolder>{

    private LayoutInflater mInflater;

    public ScheduleAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ScheduleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.layoutlist_item_schedule_parent, viewGroup, false);
        return new ScheduleParentViewHolder(view);
    }

    @Override
    public ScheduleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.layoutlist_item_schedule_child, viewGroup, false);
        return new ScheduleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ScheduleParentViewHolder scheduleParentViewHolder, int i, ParentListItem parentListItem) {
        Schedule schedule = (Schedule) parentListItem;
        scheduleParentViewHolder.bind(schedule);
    }

    @Override
    public void onBindChildViewHolder(ScheduleChildViewHolder scheduleChildViewHolder, int i, Object childListItem) {
        ScheduleChild scheduleChild = (ScheduleChild) childListItem;
        scheduleChildViewHolder.bind(scheduleChild);
    }
}
