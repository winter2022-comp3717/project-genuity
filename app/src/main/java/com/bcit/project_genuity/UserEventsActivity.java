package com.bcit.project_genuity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserEventsActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private ArrayList<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

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

        setAnimation();

        FragmentActivity activity = (FragmentActivity) this;
        fStore = FirebaseFirestore.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(currentUser.getUid());
        DatabaseReference userEventsRef = userRef.child("events");
        userEventsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> results = (HashMap<String, String>) task.getResult().getValue();
                if (results != null) {
                    events = new ArrayList<>(results.values());
                    setupRecyclerView(activity);
                } else {
                    showEmptyEvent();
                }
            }
        });
    }

    public void showEmptyEvent() {
        TextView textView = findViewById(R.id.textView_userEvents_empty);
        textView.setText("Not registered in any events yet!");
    }

    public void setAnimation() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout_user_events);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        linearLayout.startAnimation(animation);
    }

    public void setupRecyclerView(FragmentActivity activity) {
        List<Event> eventsArraylist = new ArrayList<>();

        fStore.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Storing ID to pass when on click
                                String id = document.getId();
                                if (events.contains(id)) {
                                    String name = document.getData().get("name").toString();

                                    String host = document.getData().get("host").toString();

                                    //Converting Timestamp to Date object, then format to String
                                    Timestamp date = (Timestamp) document.getData().get("datetime");
                                    Date dateJava = date.toDate();
                                    SimpleDateFormat sfd = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
                                    String datetime = sfd.format(dateJava);

                                    String description = document.getData().get("description").toString();

                                    String location = document.getData().get("location").toString();

                                    String imgUrl = document.getData().get("imgUrl").toString();

                                    //Create and Event object from the data.
                                    Event event = new Event(id, name, host, datetime, imgUrl, description, location);

                                    eventsArraylist.add(event);
                                }
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }
                        Event[] events = eventsArraylist.toArray(new Event[eventsArraylist.size()]);
                        RecyclerView recyclerView = findViewById(R.id.recyclerView_user_events);
                        SearchEventsAdapter searchEventsAdapter = new SearchEventsAdapter(events);
                        recyclerView.setAdapter(searchEventsAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    }
                });
    }

}