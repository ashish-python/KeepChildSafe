package com.parentapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parentapp.activities.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, ArrayList<String> childList){
        super(context, 0, childList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bg_spinner, parent, false);
        }
        TextView firstName = convertView.findViewById(R.id.child_name);
        String currentName = getItem(position);
        firstName.setText(currentName);
        return convertView;
    }
}
