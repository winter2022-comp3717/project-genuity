package com.bcit.project_genuity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
                        .setTitle("Join Event")
                        .setMessage("Do you want to go to " + event.getName() + " ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                ArrayList<String> events = new ArrayList<>();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);
                                DatabaseReference eventArrayRef = userRef.child("events");

//                                userRef.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        User user = snapshot.getValue(User.class);
//                                        if (user != null) {
//                                            if (user.events != null)
//                                                events.addAll(user.events);
//                                        }
//                                        events.add(event.getId());
//                                        String[] arr = {"0"};
//                                        eventArrayRef.setValue(Arrays.asList(arr)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                //showHomePage();
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
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

    private void showHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}