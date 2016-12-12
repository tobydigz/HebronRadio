package com.digzdigital.hebronradio.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Digz on 12/12/2016.
 */

public class ScheduleItem implements Parcelable{

    private int id;
    private String name;
    private String time;

    public ScheduleItem(int id, String name, String time){
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public ScheduleItem(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.time = in.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(time);
    }

    public static final Parcelable.Creator<ScheduleItem> CREATOR = new Parcelable.Creator<ScheduleItem>(){
        @Override
        public ScheduleItem createFromParcel(Parcel parcel) {
            return new ScheduleItem(parcel);
        }

        @Override
        public ScheduleItem[] newArray(int i) {
            return new ScheduleItem[i];
        }

    };
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof ScheduleItem){
            ScheduleItem toCompare = (ScheduleItem) obj;
            return (this.id == toCompare.getId());
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (this.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
