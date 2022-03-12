package com.example.telhaimarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {
    private TextView welcome;
    private Button login,logout, register,editProfile,creatPost,myPosts;
    private FirebaseAuth auth;//a
    private boolean loggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        loggedIn = false;
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        welcome = (TextView)findViewById(R.id.welcomeMessage);
        login = (Button)findViewById(R.id.login_page_button);
        logout = (Button)findViewById(R.id.log_out);
        register = (Button)findViewById(R.id.register_page_button);
        editProfile = (Button)findViewById(R.id.edit_profile_button);
        creatPost = (Button)findViewById(R.id.create_post_page_button);
        myPosts = (Button)findViewById(R.id.my_posts_page_button);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null){
            loggedIn = false;
            welcome.setText("Welcome Guest");
        }
        else {
            loggedIn = true;
            FirebaseUser firebaseUser = auth.getCurrentUser();
            FirebaseUser user = auth.getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRf =  database.getReference("users");
            userRf.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot dataSnapshot;
                            for (DataSnapshot postsnap: snapshot.getChildren()) {
                                User temp = postsnap.getValue(User.class);
                                if (temp.getUid().equals(auth.getUid())){
                                    welcome.setText("Welcome " + temp.getFullName());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loggedIn){
                    Toast.makeText(ProfilePage.this, "You all ready logged in",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(ProfilePage.this, Login.class));
                }
            }

        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loggedIn){
                    Toast.makeText(ProfilePage.this, "You all ready logged in",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(ProfilePage.this, Register.class));
                }
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loggedIn){
                    Toast.makeText(ProfilePage.this, "You are not logged in",Toast.LENGTH_SHORT).show();

                }
                else {
                    startActivity(new Intent(ProfilePage.this, ProfileEditPage.class));
                }
            }
        });
        creatPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loggedIn){
                    Toast.makeText(ProfilePage.this, "You are not logged in",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(ProfilePage.this, NewPost.class));
                }
            }
        });
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loggedIn){
                    Toast.makeText(ProfilePage.this, "You are not logged in",Toast.LENGTH_SHORT).show();

                }
                else {
                    startActivity(new Intent(ProfilePage.this, Login.class));
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loggedIn){
                    Toast.makeText(ProfilePage.this, "You are not logged in",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signOut();
                    startActivity(new Intent(ProfilePage.this, Login.class));
                    finish();

                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ProfilePage.this, MainActivity.class));

                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}