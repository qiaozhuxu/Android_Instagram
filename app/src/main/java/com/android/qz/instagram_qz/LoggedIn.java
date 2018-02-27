package com.android.qz.instagram_qz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {
    TextView welcomtText;
    FirebaseAuth myAuth;
    DatabaseReference ref;
    FirebaseDatabase database;
    String lastName;
    String firstName;
    EditText lastNameText;
    EditText firstNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        Intent newIntent = getIntent();
        welcomtText = (TextView) findViewById(R.id.welcomeText);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = user.getDisplayName();
        welcomtText.setText("Welcome" + userName);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("user1/lastName");
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(LoggedIn.this, MainActivity.class));
    }
    public void addToDataBase(View view) {

    }
}
