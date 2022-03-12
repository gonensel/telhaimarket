package com.example.telhaimarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.HashMap;

public class ProfileEditPage extends AppCompatActivity {
    private EditText password;
    private EditText passwordver;
    private EditText fullname;
    private EditText  phone_number;
    Button updateInfo;
    User temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_page);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        password = (EditText)findViewById(R.id.password_editpage_edit);
        passwordver = (EditText)findViewById(R.id.re_password_editpage_edit);
        fullname = (EditText)findViewById(R.id.FullName_editpage_edit);
        phone_number = (EditText)findViewById(R.id.editPhoneNumber_editpage);
        updateInfo = (Button)findViewById(R.id.update_editpage_button);
        //to pull user data from db
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRf =  database.getReference("users");
        temp = new User();
        userRf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot dataSnapshot;
                for (DataSnapshot postsnap: snapshot.getChildren()) {
                    User tempy = postsnap.getValue(User.class);
                    if (tempy.getUid().equals(auth.getUid())){
                        temp = new User(tempy.getEmail(),tempy.getUid(),tempy.getFullName(),tempy.getPhoneNumber());
                        fullname.setText(temp.getFullName());
                        phone_number.setText(temp.getPhoneNumber());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRf =  database.getReference("users");
                String txt_password = password.getText().toString();
                String txt_fullname = fullname.getText().toString();
                String txt_phone_number = phone_number.getText().toString();
                String txt_passwordVer = passwordver.getText().toString();


                if (TextUtils.isEmpty(txt_password)|| TextUtils.isEmpty(txt_fullname)|| TextUtils.isEmpty(txt_phone_number)){
                    Toast.makeText(ProfileEditPage.this, "Some fields are empty ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txt_password.length() < 6 ) {
                    Toast.makeText(ProfileEditPage.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (!txt_password.equals(txt_passwordVer)){
                    Toast.makeText(ProfileEditPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (txt_phone_number.length() != 10){
                    Toast.makeText(ProfileEditPage.this, "Phone number is too short", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    temp.setFullName(txt_fullname);
                    temp.setPhoneNumber(txt_phone_number);
                    if(!updatePassword(user, txt_password, txt_passwordVer)){
                        return;
                    }
                    Toast.makeText(ProfileEditPage.this, "Update user is succeeded:", Toast.LENGTH_SHORT).show();
                    temp.setEmail(auth.getCurrentUser().getEmail());
                    temp.setUid(auth.getUid());
                    userRf.child(auth.getUid()).setValue(temp);
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(txt_fullname).build();
                    firebaseUser.updateProfile(profileUpdates);
                    startActivity(new Intent(ProfileEditPage.this, ProfilePage.class));
                }

            }
        });

    }

    private boolean updatePassword(FirebaseUser user,String newPassword,String newPasswordVer){
        if(newPassword.length() < 6 ) {
            Toast.makeText(ProfileEditPage.this, "Too short", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.equals(newPasswordVer)){
            Toast.makeText(ProfileEditPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;

        }
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileEditPage.this, "Passwords changed", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
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
