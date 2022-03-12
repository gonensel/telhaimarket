package com.example.telhaimarket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyPosts extends AppCompatActivity {

    private RecyclerView posts_feed;
    private DatabaseReference PostsRef;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        posts_feed = (RecyclerView)findViewById(R.id.my_feed_posts);
        posts_feed.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        posts_feed.setLayoutManager(llm);

        getSupportActionBar().setTitle("My Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("posts");
        query = PostsRef.orderByChild("ownerUid").equalTo(user.getUid());
        DisplayAllPosts();
    }

    private void DisplayAllPosts(){
        FirebaseRecyclerAdapter<Post, MainActivity.PostHolder> fra = null;

        FirebaseRecyclerOptions<Post> options = new
                FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class).build();

        fra = new FirebaseRecyclerAdapter<Post, MainActivity.PostHolder>(options)
        {
            @Override
            protected void onBindViewHolder(MainActivity.PostHolder holder, int position, Post model) {
                holder.setTitle(model.getTitle());
                holder.setDescription(model.getDescription());
                holder.setPrice(model.getPrice());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRf =  database.getReference("users");
                userRf.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot dataSnapshot;
                        for (DataSnapshot postsnap: snapshot.getChildren()) {
                            User temp = postsnap.getValue(User.class);
                            if (temp.getUid().equals(model.getOwnerUid())){
                                holder.setFullName(temp.getFullName());
                                holder.setPhoneNumber(temp.getPhoneNumber());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                holder.tempView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyPosts.this);
                        builder.setCancelable(true);
                        builder.setTitle("Do you want to delete that post?");
                        builder.setMessage("Delete post");
                        builder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PostsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot dataSnapshot;
                                        for (DataSnapshot postsnap: snapshot.getChildren()) {

                                            Post post = postsnap.getValue(Post.class);
                                            if (post.getKeyNode().equals(model.getKeyNode())){
                                                postsnap.getRef().removeValue();
                                                break;
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        builder.show();
                    }

                });
            }

            @NonNull
            @Override
            public MainActivity.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_layout, parent, false);
                MainActivity.PostHolder viewHolder = new MainActivity.PostHolder(view);
                return viewHolder;
            }
        };
        posts_feed.setAdapter(fra);
        fra.startListening();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}