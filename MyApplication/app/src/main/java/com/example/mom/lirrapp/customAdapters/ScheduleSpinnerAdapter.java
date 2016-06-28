package com.example.mom.lirrapp.customAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nat on 6/28/16.
 */
public class ScheduleSpinnerAdapter extends ArrayAdapter<TrainStation> {

    ArrayList<TrainStation>mTrainStationArrayList;
    Context mContext=null;

    public ScheduleSpinnerAdapter(Context context) {
        super(context, -1);
        mContext=context;
        mTrainStationArrayList = new ArrayList<>();
    }

    public void addStation(TrainStation ts){
        mTrainStationArrayList.add(ts);
    }
    public int getCount(){
        return mTrainStationArrayList.size();
    }

    public TrainStation getItem(int position){
        return mTrainStationArrayList.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setText(mTrainStationArrayList.get(position).fullName);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setText(mTrainStationArrayList.get(position).fullName);
        return label;
    }
}
