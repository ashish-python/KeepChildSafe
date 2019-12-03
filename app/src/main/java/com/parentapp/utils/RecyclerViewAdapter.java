package com.parentapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parentapp.activities.R;
import com.parentapp.geofencessdk.GeofenceEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder> {
    private ArrayList<GeofenceEvent> geofenceEventsList;

    public RecyclerViewAdapter(ArrayList<GeofenceEvent> geofenceEventsList) {
        this.geofenceEventsList = geofenceEventsList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        BaseViewHolder baseViewHolder = new BaseViewHolder(view);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        try {
            GeofenceEvent geofenceEvent = (GeofenceEvent) geofenceEventsList.get(position);
            holder.userIdTV.setText(geofenceEvent.getGeofenceName());
            String timestampUTCString = geofenceEvent.getTimestamp();
            DateTime timestampDateTimeUTC = TimeUtil.getFormattedDateTimeUTC(timestampUTCString);
            holder.dateTV.setText(TimeUtil.convertUTCToLocalDate(timestampDateTimeUTC));
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return geofenceEventsList.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        public TextView userIdTV;
        public TextView dateTV;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id);
            dateTV = itemView.findViewById(R.id.date_tv);
        }
    }
}
