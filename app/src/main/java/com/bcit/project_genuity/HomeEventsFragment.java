package com.bcit.project_genuity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeEventsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private Event event;

    public HomeEventsFragment() {
        // Required empty public constructor
    }

    public static HomeEventsFragment newInstance(Event param1) {
        HomeEventsFragment fragment = new HomeEventsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardView = view.findViewById(R.id.card_event_fragment);
        TextView name = view.findViewById(R.id.home_event_title);
        TextView host = view.findViewById(R.id.home_event_host);
        TextView datetime = view.findViewById(R.id.home_event_datetime);
        TextView description = view.findViewById(R.id.home_event_description);

        name.setText(event.getName());
        host.setText(event.getHost());
        datetime.setText(event.getDatetime());
        description.setText(event.getDescription());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EventPageActivity.class);
                intent.putExtra("Event", event);
                view.getContext().startActivity(intent);
            }
        });
    }
}