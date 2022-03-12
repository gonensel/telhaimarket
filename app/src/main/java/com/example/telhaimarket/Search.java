package com.example.telhaimarket;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class Search extends AppCompatActivity {
    private EditText search;
    private ImageButton searchBTN;
    private RecyclerView posts_feed;
    private DatabaseReference PostsRef;
    private Query query;
    private Button deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        posts_feed = (RecyclerView)findViewById(R.id.search_posts);
        posts_feed.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        posts_feed.setLayoutManager(llm);

        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        search = (EditText) findViewById(R.id.search_edit);
        searchBTN = (ImageButton)findViewById(R.id.searchBTN);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_search = search.getText().toString();
                if (TextUtils.isEmpty(txt_search)){
                    Toast.makeText(Search.this, "What do you wont to search?", Toast.LENGTH_SHORT).show();
                    return;
                }
                PostsRef = FirebaseDatabase.getInstance().getReference().child("posts");
                query = PostsRef.orderByChild("description").startAt(txt_search).endAt(txt_search + "\uf8ff");
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                DisplayAllPosts();
            }
        });
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