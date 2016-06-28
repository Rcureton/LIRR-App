package com.example.mom.lirrapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

import static com.example.mom.lirrapp.Constants.SENDER_ID;

/**
 * Created by MOM on 5/18/16.
 */
public class ReportIconsFragment extends DialogFragment {

    public ReportIconsFragment() {

    }

    public static ReportIconsFragment newInstance(String title) {
        ReportIconsFragment fragment = new ReportIconsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.custom_fragment_layout, container);

        FloatingActionButton policeButton = (FloatingActionButton) fragment.findViewById(R.id.policeIcon);
        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policeMessageUpstream();
            }
        });
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton trafficButton = (FloatingActionButton) view.findViewById(R.id.trafficIcon);
        FloatingActionButton issueButton = (FloatingActionButton) view.findViewById(R.id.issueIcon);

    }

    private void policeMessageUpstream() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        AtomicInteger msgId = new AtomicInteger();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "There is police activity at the current location")
                .addData("my_action", "Police Activity")
                .build());
    }

    private void trafficIssueUpstream() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        AtomicInteger msgId = new AtomicInteger();
        fm.send(new RemoteMessage.Builder(R.string.gcm_defaultSenderId + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "There is traffic at the current location")
                .addData("my_action", "Traffic Activity")
                .build());
    }

    private void issueButtonUpstream() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        AtomicInteger msgId = new AtomicInteger();
        fm.send(new RemoteMessage.Builder(R.string.gcm_defaultSenderId + "@gcm.googleapis.com")
                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                .addData("my_message", "There is issue at the current location")
                .addData("my_action", "Issue Activity")
                .build());
    }
}
