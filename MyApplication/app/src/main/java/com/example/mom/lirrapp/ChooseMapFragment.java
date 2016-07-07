package com.example.mom.lirrapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Ra on 7/7/16.
 */
public class ChooseMapFragment extends DialogFragment {

    public ChooseMapFragment(){

    }

    public static ChooseMapFragment newInstance(String title) {
        ChooseMapFragment fragment = new ChooseMapFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.choose_map_layout, container);

        ImageButton lirrButton= (ImageButton) fragment.findViewById(R.id.lirr_logo);
        lirrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),LIRRMap.class);
                startActivity(intent);
            }
        });

        ImageButton metroNorthButton= (ImageButton)fragment.findViewById(R.id.metro_north_logo);
        metroNorthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),MetroNorthMap.class);
                startActivity(intent);
            }
        });

        return fragment;
    }
}
