package com.example.telhaimarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText passwordver;
    private Button register;
    private EditText fullname;
    private EditText  phone_number;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email = (EditText)findViewById(R.id.email_register_edit);
        password = (EditText)findViewById(R.id.password_register_edit);
        passwordver = (EditText)findViewById(R.id.re_password_register_edit);
        fullname = (EditText)findViewById(R.id.editFullName);
        phone_number = (EditText)findViewById(R.id.PhoneNumber_edit);
        register=(Button)findViewById(R.id.register_button);
        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_fullname = fullname.getText().toString();
                String txt_phone_number = phone_number.getText().toString();
                String txt_passwordVer = passwordver.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)|| TextUtils.isEmpty(txt_fullname)|| TextUtils.isEmpty(txt_phone_number)){
                    Toast.makeText(Register.this, "Some fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txt_password.length() < 6 ) {
                    Toast.makeText(Register.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!txt_password.equals(txt_passwordVer)){
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_phone_number.length() != 10){
                    Toast.makeText(Register.this, "Phone number is too short", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(txt_email,txt_password,txt_fullname,txt_phone_number);

            }
        });

    }

        private void registerUser(String email,String password,String fullname,String phone_number)
    {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            generateUser(email, auth.getUid(), fullname, phone_number);
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                            user.updateProfile(profileUpdates);
                            Toast.makeText(Register.this, "Create user is succeeded:", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }
    private void generateUser(String email,String uid,String fullname,String phone_number) {

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("users"); //users is a node in your Firebase Database.
        User user = new User(email, uid, fullname, phone_number); //ObjectClass for Users
        users.child(uid).setValue(user);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}