package com.digzdigital.hebronradio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digzdigital.hebronradio.R;
import com.digzdigital.hebronradio.model.ScheduleItem;

import java.util.ArrayList;

/**
 * Created by Digz on 12/05 /2016. f
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private static MyClickListener myClickListener;
    private ArrayList<ScheduleItem> scheduleItems;

    public ScheduleAdapter(ArrayList<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    public ScheduleItem getItem(int position) {
        return scheduleItems.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_schedule, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ScheduleItem scheduleItem = getItem(position);

        holder.scheduleItemName.setText(scheduleItem.getName());
        holder.scheduleItemTime.setText(scheduleItem.getTime());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView scheduleItemName, scheduleItemTime;

        ViewHolder(View itemView) {
            super(itemView);
            scheduleItemName = (TextView) itemView.findViewById(R.id.scheduleItemName);
            scheduleItemTime = (TextView) itemView.findViewById(R.id.scheduleItemTime);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);

        }


    }
}
