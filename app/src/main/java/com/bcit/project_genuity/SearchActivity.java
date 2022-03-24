package com.bcit.project_genuity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        fStore = FirebaseFirestore.getInstance();
        getEvents();

        Button button = findViewById(R.id.getData_search);


    }

    public void getEvents() {
        List<Event> eventsArraylist = new ArrayList<>();

        fStore.collection("events")
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Storing ID to pass when on click
                                String id = document.getId();

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

                                System.out.println(event.getId());
                                System.out.println(event.getDatetime());

                                eventsArraylist.add(event);
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }

                        Event[] events = eventsArraylist.toArray(new Event[eventsArraylist.size()]);
                        System.out.println("Events size -> " + events.length);
                    }
                });
    }
}