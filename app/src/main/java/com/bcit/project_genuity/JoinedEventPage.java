package com.bcit.project_genuity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class JoinedEventPage extends AppCompatActivity {

    FirebaseFirestore fs;
    String userID;
    DatabaseReference userRef;
    DatabaseReference eventArrayRef;
    DocumentReference eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_event_page);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setAnimation();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        fs = FirebaseFirestore.getInstance();

        if (currentUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Event event = (Event) getIntent().getSerializableExtra("Event");
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userRef = FirebaseDatabase.getInstance().getReference("users").child(userID);
        this.eventArrayRef = userRef.child("events");
        this.eventRef = fs.collection("events").document(event.getId());

        setUpPage(event);

        Button cancelButton = findViewById(R.id.button_joined_event_leave_event);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(JoinedEventPage.this)
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

    public void setUpPage(Event event) {
        TextView eventName = findViewById(R.id.textView_joined_event_name);
        TextView eventHost = findViewById(R.id.textView_joined_event_host);
        TextView eventDetails =  findViewById(R.id.textView_joined_event_details);
        TextView eventLocation = findViewById(R.id.textView_joined_event_location);
        ImageView barcode = findViewById(R.id.imageView_joined_event_barcode);
        String url = "https://firebasestorage.googleapis.com/v0/b/project-genuity.appspot.com/o/qr.png?alt=media&token=556496d1-1544-43c3-9e1a-15b161ffb984";

        Picasso.get().load(url).into(barcode);
        eventName.setText(event.getName());
        eventHost.setText(event.getHost());
        eventDetails.setText(event.getDescription());
        eventLocation.setText(event.getLocation());
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

    private void showHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void setAnimation() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout_joined_event);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        linearLayout.startAnimation(animation);
    }
}