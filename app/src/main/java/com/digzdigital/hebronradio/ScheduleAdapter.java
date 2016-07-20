package com.digzdigital.hebronradio;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Digz on 16/07/2016.
 */
public class ScheduleAdapter extends ExpandableRecyclerAdapter<ScheduleAdapter.ScheduleListItem> {
    public static final int TYPE_SCHEDULE = 1001;
Resources resources;
    public ScheduleAdapter(Context context, Resources res) {
        super(context);
        resources = res;
        setItems(getSampleItems());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_SCHEDULE:
            default:
                return new ScheduleViewHolder(inflate(R.layout.item_schedule, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_SCHEDULE:
            default:
                ((ScheduleViewHolder) holder).bind(position);
                break;
        }
    }

    private List<ScheduleListItem> getSampleItems() {
        String scheduleItem;
        String[] monday = resources.getStringArray(R.array.mon_ar);
        String[] tuesday = resources.getStringArray(R.array.tue_ar);
        String[] wednesday = resources.getStringArray(R.array.wed_ar);
        String[] thursday= resources.getStringArray(R.array.thu_ar);
        String[] friday= resources.getStringArray(R.array.fri_ar);
        String[] saturday= resources.getStringArray(R.array.sat_ar);
        String[] sunday= resources.getStringArray(R.array.sun_ar);
        List<ScheduleListItem> items = new ArrayList<>();

        items.add(new ScheduleListItem("Monday"));
        for (String aMonday : monday) {
            scheduleItem = aMonday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Tuesday"));
        for (String aTuesday : tuesday) {
            scheduleItem = aTuesday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Wednesday"));
        for (String aWednesday : wednesday) {
            scheduleItem = aWednesday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Thursday"));
        for (String aThursday : thursday) {
            scheduleItem = aThursday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Friday"));
        for (String aFriday : friday) {
            scheduleItem = aFriday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Saturday"));
        for (String aSaturday : saturday) {
            scheduleItem = aSaturday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }
        items.add(new ScheduleListItem("Sunday"));
        for (String aSunday : sunday) {
            scheduleItem = aSunday;
            items.add(new ScheduleListItem(" " + scheduleItem, ""));
        }

        return items;
    }

    public static class ScheduleListItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;

        public ScheduleListItem(String group) {
            super(TYPE_HEADER);
            Text = group;
        }

        public ScheduleListItem(String event, String time) {
            super(TYPE_SCHEDULE);
            Text = event;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            name = (TextView) view.findViewById(R.id.item_header_name);
        }

        public void bind(int position) {
            super.bind(position);
            name.setText(visibleItems.get(position).Text);
        }
    }

    public class ScheduleViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView event;

        public ScheduleViewHolder(View view) {
            super(view);
            event = (TextView) view.findViewById(R.id.item_name);
        }

        public void bind(int position) {
            event.setText(visibleItems.get(position).Text);
        }
    }
}
