package com.example.hotelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityLogin extends AppCompatActivity {

    private FirebaseAuth auth;
    Button loginBTN, signupBTN, forTryBTN ; // forTry will be deleted
    private EditText loginEmailTXT, loginPassTXT ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        auth = FirebaseAuth.getInstance();
        loginEmailTXT = findViewById(R.id.loginEmailTXT);
        loginPassTXT = findViewById(R.id.loginPassTXT);

        loginBTN = findViewById(R.id.loginBTN);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // login activity code ...

                String email = loginEmailTXT.getText().toString();
                String pass = loginPassTXT.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches ()){
                    if (!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(MainActivityLogin.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivityLogin.this , homePage.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivityLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassTXT.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()){
                    loginEmailTXT.setError("Email cannot be empty");
                } else {
                    loginEmailTXT.setError("Please enter valid email");
                }

                //Intent intent= new Intent(MainActivityLogin.this, homePage.class);
                //startActivity(intent);

            }
        });

        signupBTN = findViewById(R.id.signupBTN);
        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLogin.this, signup.class);
                startActivity(intent);

            }
        });
/*
        forTryBTN = findViewById(R.id.forTryBTN); //this line will be deleted
        forTryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLogin.this, homePage.class);
                startActivity(intent);
            }
        }); */
    }
}