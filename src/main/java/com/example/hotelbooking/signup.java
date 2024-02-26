package com.example.hotelbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signup extends AppCompatActivity {

    private Button upSignupBTN, upLoginBTN;
    private FirebaseAuth auth;
    private EditText signupEmailTXT, signupPassTXT, signupRepPassTXT, usernameTXT ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        usernameTXT = findViewById(R.id.usernameTXT);
        signupEmailTXT = findViewById(R.id.signupEmail);
        signupPassTXT = findViewById(R.id .signupPass);
        upSignupBTN = findViewById(R.id.upSignupBTN);

        upSignupBTN = findViewById(R.id.upSignupBTN);
        upSignupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the activity for sign up button...
                String username = usernameTXT.getText().toString().trim();
                String mail = signupEmailTXT.getText().toString().trim();
                String pass = signupPassTXT.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameTXT.setError("Username cannot be empty");
                    return;
                }
                if (mail . isEmpty ()){
                    signupEmailTXT.setError ("Email cannot be empty");
                }
                if (pass. isEmpty()) {
                    signupPassTXT.setError("Password cannot be empty");
                } else {
                    auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(signup.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(signup.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }



                                Toast.makeText(signup.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signup.this, MainActivityLogin.class));
                            } else {
                                Toast.makeText(signup.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        upLoginBTN = findViewById(R.id.upLoginBTN);
        upLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, MainActivityLogin.class);
                startActivity(intent);
            }
        });
    }
}