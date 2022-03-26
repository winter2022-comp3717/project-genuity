package com.bcit.project_genuity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();
            return;
        }

        LinearLayout linearLayout = findViewById(R.id.LinearLayout_signin);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        linearLayout.startAnimation(animation);

        Button buttonCrrAcc = findViewById(R.id.button_createAcc_signin);
        buttonCrrAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        Button buttonLogin = findViewById(R.id.button_logIn_signin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });

    }

    private void registerUser() {
        EditText eName = findViewById(R.id.editText_signin_Name);
        EditText eEmail = findViewById(R.id.editText_signin_Email);
        EditText ePhone = findViewById(R.id.editText_signin_Phnum);
        EditText ePassword = findViewById(R.id.editText_signin_Password);

        String name = eName.getText().toString().trim();
        String email = eEmail.getText().toString().trim();
        String phone = ePhone.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            long phoneNumber = Long.parseLong(phone);
            if (phoneNumber < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid phone number!", Toast.LENGTH_LONG).show();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(this, "Phone numbers must be 10 digits long!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(name, email, phone);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    showHomePage();
                                }
                            });
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void showHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void switchToLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}