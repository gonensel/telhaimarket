package com.example.telhaimarket;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.telhaimarket.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Login extends AppCompatActivity {
    // Creating EditText.
    EditText email, password ;
    Button login;
    // Creating FirebaseAuth object.
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email = (EditText)findViewById(R.id.email_login_edit);
        password = (EditText)findViewById(R.id.password_login_edit);
        login = (Button)findViewById(R.id.login_button);
        firebaseAuth = FirebaseAuth.getInstance();

        // Checking if user already logged in before and not logged out properly.
//        if(firebaseAuth.getCurrentUser() != null){
//
//            // Finishing current Login Activity.
//            finish();
//
//            // Opening UserProfileActivity .
//            Intent intent = new Intent(Login.this, Login.class);
//            startActivity(intent);
//        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(Login.this, "Please Fill All the Fields", Toast.LENGTH_LONG).show();

                }
                else {
                    LoginFunction(txt_email, txt_password);

                }
            }
        });


    }

    public void LoginFunction(String email, String password){


        // Calling  signInWithEmailAndPassword function with firebase object and passing EmailHolder and PasswordHolder inside it.
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If task done Successful.
                        Dialog progressDialog;
                        if(task.isSuccessful()){


                            // Closing the current Login Activity.
                            finish();


                            // Opening the UserProfileActivity.
                            Intent intent = new Intent(Login.this, NewPost.class);
                            startActivity(intent);
                        }
                        else {

                            // Showing toast message when email or password not found in Firebase Online database.
                            Toast.makeText(Login.this, "Email or Password Not found, Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}