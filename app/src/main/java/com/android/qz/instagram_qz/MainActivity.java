package com.android.qz.instagram_qz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText emailView;
    private EditText passwordView;
    private Button btnLogin;

    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener myAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailView = (EditText) findViewById(R.id.editEmail);
        passwordView = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btn_login);
        textView = (TextView) findViewById(R.id.textView);

        //get singleton instance
        myAuth = FirebaseAuth.getInstance();

        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(myAuth.getCurrentUser() != null) {
                    if(myAuth.getCurrentUser().getDisplayName() == null ) {
                        startActivity(new Intent(MainActivity.this, Profile.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoggedIn.class));
                    }
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(myAuthListener);
    }

    public void login(View view) {
        String userEmail = emailView.getText().toString();
        String userPass = passwordView.getText().toString();

        if (TextUtils.isEmpty(userEmail) ) {
            textView.setText("Email should not be empty!");
            return;
        }
        if (TextUtils.isEmpty(userPass)) {
            textView.setText("Password should not be empty!");
            return;
        }

        myAuth.signInWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    textView.setText("Log in failed");
                }
            }
        });

    }

    public void signup(View view) {
        startActivity(new Intent (MainActivity.this, SignUp.class));
    }
}
