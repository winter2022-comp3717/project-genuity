package com.bcit.project_genuity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView imageView = findViewById(R.id.imageView_main);
        TextView textView = findViewById(R.id.textView_main);
        Button button = findViewById(R.id.button_main);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imageView.startAnimation(animation);
        button.startAnimation(animation);
        textView.startAnimation(animation);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}