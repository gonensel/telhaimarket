package com.example.telhaimarket;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.telhaimarket.databinding.ActivityNewPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewPost extends AppCompatActivity {
    EditText title, dec, price;
    Button publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getSupportActionBar().setTitle("Create New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        title = (EditText)findViewById(R.id.title_new_post_edit);
        dec = (EditText)findViewById(R.id.editDec);
        price = (EditText)findViewById(R.id.price_edit);
        publish = (Button)findViewById(R.id.publish_button);

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_title = title.getText().toString();
                String txt_dec = dec.getText().toString();
                String txt_price = price.getText().toString();
                if (TextUtils.isEmpty(txt_title) || TextUtils.isEmpty(txt_dec) || TextUtils.isEmpty(txt_price)){
                    Toast.makeText(NewPost.this, "Some fields are empty please fill all",Toast.LENGTH_SHORT).show();
                }
                else {
                    publish(txt_dec, txt_title, txt_price);

                }
            }
        });


    }


    private void publish(String dec,String title, String price) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Post post = new Post(dec,title,price,auth.getUid());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("posts"); //users is a node in your Firebase Database.
        posts.push().setValue(post).addOnCompleteListener(NewPost.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NewPost.this, "publish new post" , Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()){
                    Toast.makeText(NewPost.this, "publish new post failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(NewPost.this, NewPost.class));// TODO go to mainActivity
//                    posts.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            ArrayList <Post> lst= new ArrayList<Post>();
//                            DataSnapshot dataSnapshot;
//                            for (DataSnapshot postsnap: snapshot.getChildren()) {
//
//                                Post post = postsnap.getValue(Post.class);
//                                lst.add(post) ;
//
//
//
//                            }
//                            System.out.println(lst.get(0).getDescription());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                    finish();
                }
            }
        });


    }
}