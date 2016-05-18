package com.example.mom.lirrapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MOM on 5/18/16.
 */
public class ReportIconsFragment extends DialogFragment {


    public ReportIconsFragment() {

    }

    public static ReportIconsFragment newInstance(String title){
        ReportIconsFragment fragment= new ReportIconsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_fragment_layout,container);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton policeButton=  (FloatingActionButton)view.findViewById(R.id.policeIcon);
        FloatingActionButton trafficButton= (FloatingActionButton)view.findViewById(R.id.trafficIcon);
        FloatingActionButton issueButton= (FloatingActionButton)view.findViewById(R.id.issueIcon);

    }
}
