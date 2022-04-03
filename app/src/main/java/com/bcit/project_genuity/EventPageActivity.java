package com.bcit.project_genuity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class EventPageActivity extends AppCompatActivity {

    FirebaseFirestore fs;
    String userID;
    DatabaseReference userRef;
    DatabaseReference eventArrayRef;
    DocumentReference eventRef;
    boolean isCurrentlyRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        fs = FirebaseFirestore.getInstance();

        if (currentUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setAnimation();

        Event event = (Event) getIntent().getSerializableExtra("Event");
        setUpPage(event);

        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);
        this.eventArrayRef = userRef.child("events");
        this.eventRef = fs.collection("events").document(event.getId());

        Button joinButton = findViewById(R.id.button_eventPage_join);
        Button cancelButton = findViewById(R.id.button_eventPage_cancel);

        checkRegistrationStatus(event);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(EventPageActivity.this)
                        .setTitle("Join Event")
                        .setMessage("Do you want to join " + event.getName() + "?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getEventsList(event);
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(EventPageActivity.this)
                        .setTitle("Cancel")
                        .setMessage("Are you sure you want to cancel this event?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelEvent(event);
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

    private void checkRegistrationStatus(Event event) {
        eventArrayRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<Object, Object> results = (HashMap<Object, Object>) task.getResult().getValue();
                if (results != null && results.containsValue(event.getId())) {
                    isCurrentlyRegistered = true;
                    findViewById(R.id.button_eventPage_cancel).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getEventsList(Event event) {
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                eventRef.update("registeredUsers", FieldValue.arrayUnion(userID));

                ArrayList<String> registeredUsers = ((ArrayList<String>) task.getResult().getData().get("registeredUsers"));
                int currNumOfUsers = 0;
                if (registeredUsers != null) {
                    currNumOfUsers = registeredUsers.size();
                }
                int capacity = Math.toIntExact((Long) task.getResult().getData().get("capacity"));

                String key = eventArrayRef.push().getKey();

                HashMap<String, Object> newEvent = new HashMap<>();

                int finalCurrNumOfUsers = currNumOfUsers;

                newEvent.put(key, event.getId());
                if (finalCurrNumOfUsers == capacity) {
                    showFullyBooked();
                    return;
                } else if (!isCurrentlyRegistered) {
                    eventArrayRef.updateChildren(newEvent);
                    showRegisteredToast();
                } else {
                    showAlreadyJoined();
                    return;
                }
                showHomePage();
            }
        });
    }

    private void cancelEvent(Event event) {
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                eventRef.update("registeredUsers", FieldValue.arrayRemove(userID));
                eventArrayRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        HashMap<String, Object> results = (HashMap<String, Object>) task.getResult().getValue();
                        for (HashMap.Entry<String, Object> entry : results.entrySet()) {
                            if (entry.getValue().equals(event.getId())) {
                                eventArrayRef.child(entry.getKey()).removeValue();
                            }
                        }
                    }
                });

            }
        });
        finish();
        showHomePage();
    }

    private void showFullyBooked() {
        Toast.makeText(EventPageActivity.this,
                "Event is fully booked!",
                Toast.LENGTH_LONG).show();
    }

    private void showRegisteredToast() {
        Toast.makeText(EventPageActivity.this,
                "Registered for event!",
                Toast.LENGTH_LONG).show();
    }

    private void showAlreadyJoined() {
        new MaterialAlertDialogBuilder(EventPageActivity.this)
                .setTitle("Event Already Joined")
                .setMessage("You are already registered for this event!")
                .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void showHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void setAnimation() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout_event);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        linearLayout.startAnimation(animation);
    }

    public void setUpPage(Event event) {
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

        TextView currRegistered = findViewById(R.id.textView_eventPage_currRegistered);
        TextView capacity = findViewById(R.id.textView_eventPage_capacity);
        currRegistered.setText(String.valueOf(event.getNumberOfUsers()));
        capacity.setText(String.valueOf(event.getCapacity()));
    }
}