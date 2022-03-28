package com.bcit.project_genuity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView textView = findViewById(R.id.textView_greeting_home);
        Button buttonAccount = findViewById(R.id.button_account_home);
        Button buttonSearch = findViewById(R.id.button_search_home);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAccountPage();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchPage();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("name").getValue(String.class);
                if (userName != null) {
                    textView.setText("Welcome, " + userName.substring(0, 1).toUpperCase() + userName.substring(1) + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FragmentActivity activity = (FragmentActivity) this;
        fStore = FirebaseFirestore.getInstance();
        setupViewPager(activity);
    }

    private void showAccountPage() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void showSearchPage() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void setupViewPager(FragmentActivity activity) {
        List<Event> eventsArraylist = new ArrayList<>();

        fStore.collection("events")
                .limit(5)
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

                                eventsArraylist.add(event);
                            }
                        } else {
                            Log.w("Debug", "Error getting documents.", task.getException());
                        }
                        Event[] events = eventsArraylist.toArray(new Event[eventsArraylist.size()]);
                        ViewPager2 viewPager2 = findViewById(R.id.viewPager2_events_home);
                        ViewPagerEventsHome viewPagerEventsHome = new ViewPagerEventsHome(activity, events);
                        viewPager2.setAdapter(viewPagerEventsHome);
                    }
                });
    }
}