package com.bcit.project_genuity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EventPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Event event = (Event) getIntent().getSerializableExtra("Event");

        System.out.println(event.getDatetime());

        TextView eventName = findViewById(R.id.textView_eventPage_name);
        eventName.setText(event.getName());
        TextView datetime = findViewById(R.id.textView_eventPage_datetime);
        datetime.setText(event.getDatetime());
        TextView eventDetails = findViewById(R.id.textView_eventPage_eventDetails);
        eventDetails.setText(event.getDescription());
        TextView eventHostName = findViewById(R.id.textView_eventPage_hostName);
        eventHostName.setText(event.getHost());
        TextView eventLocation = findViewById(R.id.textView_eventPage_locationDetails);
        eventLocation.setText(event.getLocation());


        Button button = findViewById(R.id.button_eventPage_join);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(EventPageActivity.this)
                        .setTitle("Title")
                        .setMessage("Your message goes here. Keep it short but clear.")
                        .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
    }
}