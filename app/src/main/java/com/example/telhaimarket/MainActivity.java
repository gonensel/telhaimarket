package com.example.telhaimarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addNewPost;
    private MenuItem menuItem;
    private RecyclerView posts_feed;
    private DatabaseReference PostsRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        posts_feed = (RecyclerView)  findViewById(R.id.all_feed_posts);
        posts_feed.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        posts_feed.setLayoutManager(llm);
        auth = FirebaseAuth.getInstance();
        PostsRef = FirebaseDatabase.getInstance().getReference().child("posts");

        DisplayAllPosts();
        addNewPost = (FloatingActionButton)findViewById(R.id.newPost);
        addNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this, ProfilePage.class));
                }
                startActivity(new Intent(MainActivity.this, NewPost.class));

            }
        });

    }

    private void DisplayAllPosts(){
        FirebaseRecyclerAdapter<Post, PostHolder> fra = null;

        FirebaseRecyclerOptions<Post> options = new
                FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(PostsRef, Post.class).build();

        fra = new FirebaseRecyclerAdapter<Post, PostHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(PostHolder holder, int position, Post model) {
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
                    }

                    @NonNull
                    @Override
                    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_layout, parent, false);
                        PostHolder viewHolder = new PostHolder(view);
                        return viewHolder;
                    }
                };
        posts_feed.setAdapter(fra);
        fra.startListening();
    }

    public static class PostHolder extends RecyclerView.ViewHolder{

        View tempView;

        public PostHolder(View itemView) {
            super(itemView);
            tempView = itemView;
        }

        public void setTitle(String title){
            TextView new_title = (TextView) tempView.findViewById(R.id.post_title);
            new_title.setText(title);
        }

        public void setDescription(String description){
            TextView new_desciption = (TextView) tempView.findViewById(R.id.post_description);
            new_desciption.setText(description);
        }

        public void setPrice(String price){
            TextView new_price = (TextView) tempView.findViewById(R.id.post_price);
            new_price.setText(price);
        }

        public void setFullName(String name){
            TextView new_full_name = (TextView) tempView.findViewById(R.id.post_full_name);
            new_full_name.setText(name);
        }

        public void setPhoneNumber(String phone_number){
            TextView new_phone_number = (TextView) tempView.findViewById(R.id.post_phone);
            new_phone_number.setText(phone_number);
        }
    }
}