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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                    return;
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
        DatabaseReference posts = database.getReference("posts"); //users is a node in Firebase Database.
        DatabaseReference rf = posts.push();
        post.setKeyNode(rf.getKey());
        rf.setValue(post).addOnCompleteListener(NewPost.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NewPost.this, "publish new post" , Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()){
                    Toast.makeText(NewPost.this, "publish new post failed." + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(NewPost.this, MainActivity.class));
                }
            }
        });
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