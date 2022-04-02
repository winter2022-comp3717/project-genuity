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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EventPageActivity extends AppCompatActivity {

    FirebaseFirestore fs;

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

        Button button = findViewById(R.id.button_eventPage_join);
        button.setOnClickListener(new View.OnClickListener() {
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
    }

    private void getEventsList(Event event) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);
        DatabaseReference eventArrayRef = userRef.child("events");
        DocumentReference eventRef = fs.collection("events").document(event.getId());

        eventRef.update("registeredUsers", FieldValue.arrayUnion(userID));

        String key = eventArrayRef.push().getKey();

        HashMap<String, Object> newEvent = new HashMap<>();
        eventArrayRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<Object, Object> results = (HashMap<Object, Object>) task.getResult().getValue();
                newEvent.put(key, event.getId());
                if (results == null) {
                    eventArrayRef.setValue(newEvent);
                    showRegisteredToast();
                } else if (!results.containsValue(event.getId())) {
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