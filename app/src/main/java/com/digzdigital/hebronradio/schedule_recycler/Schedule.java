package com.digzdigital.hebronradio.schedule_recycler;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;
import java.util.UUID;


public class Schedule implements ParentListItem {

    private List<ScheduleChild> mChildItemList;
    private String mTitle;
    private List<ScheduleChild> mScheduleChild;
    private UUID mId;


    @Override
    public List<?> getChildItemList() {
        return mChildItemList;
    }


    public void setChildItemList(List<ScheduleChild> list){
        mChildItemList = list;
    }

    public Schedule(String name, List<ScheduleChild> scheduleChildren) {
        mTitle = name;
        mScheduleChild = scheduleChildren;

    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

}
