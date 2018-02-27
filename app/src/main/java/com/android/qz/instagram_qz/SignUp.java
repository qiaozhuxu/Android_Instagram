package com.android.qz.instagram_qz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private TextView view_info;
    private EditText view_name;
    private EditText view_email;
    private EditText view_password1;
    private EditText view_password2;
    private Button btn_signup;
    private TextView view_emailAvailable;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private FirebaseDatabase database;

    String userName;
    String email;
    String password1;
    String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize the views
        view_email = (EditText) findViewById(R.id.emailText);
        view_info = (TextView) findViewById(R.id.infoText);
        view_name = (EditText) findViewById(R.id.nameText);
        view_password1 = (EditText) findViewById(R.id.passwordText);
        view_password2 = (EditText) findViewById(R.id.passwordConfirmText);
        btn_signup = (Button) findViewById(R.id.btn_SignUp);

        view_emailAvailable = (TextView) findViewById(R.id.emailAvailabilityText);
//        view_emailAvailable.setVisibility(View.INVISIBLE);

        //initialize the auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void checkEmailHasExisted(View view) {
        Log.i("clicked ", "email validation check");

        email = view_email.getText().toString();
//      view_emailAvailable.setText(email);

        mAuth.createUserWithEmailAndPassword(email, "dummypass")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            view_info.setText("You can go on with this email ");
//                            view_emailAvailable.setVisibility(View.VISIBLE);
                            Log.d("signup dummy user", "createUser:onComplete:" + task.isSuccessful());
                            user = task.getResult().getUser();
                        } else {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                Log.d("signup dummy user", "This email ID already used by someone else");
                                view_info.setText("Something went wrong with creation");
                            }
                        }
                    }
                });
    }

    public void signup(View view) {
        Log.i("sign up ", "is clicked");
        userName = view_name.getText().toString();
        email = view_email.getText().toString();
        password1 = view_password1.getText().toString();
        password2 = view_password2.getText().toString();

        if (userNameCheckPassed(userName) && emailCheckPassed(email)&& passwordCheckPassed(password1, password2))
        {
            Log.i("I am here at :", "before ref");
            ref = database.getReference("user");
            ref.orderByChild("username").equalTo(userName).addListenerForSingleValueEvent(
                    new ValueEventListener(){

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.i("checking duplication", "dataSnapshot value = " + dataSnapshot.getValue());

                            if (dataSnapshot.exists()) {
                                // User Exists
                                view_info.setText("Username already exists. Please try other username.");
                                Toast.makeText(getApplicationContext(), "Username already exists. Please try other username.", Toast.LENGTH_SHORT).show();
                            } else {
                                // User Not Yet Exists
//                                createUser(email, password1);
                                view_info.setText("No duplicate user.");
                                //something went wrong with firebase auth
//                                if(user != null) {
//                                    view_info.setText(String.format("Welcome %s to our instagram!",userName));
//                                    startActivity(new Intent(SignUp.this, MainActivity.class));
//                                } else {
//                                    view_info.setText("Something went wrong with firebase, please try later");
//                                }
                            }
                        }
                        @Override
                        public void onCancelled (DatabaseError databaseError){
                        }
                    }
            );
        } else {
            btn_signup.setText("Required info is not correct, Try Again!");
        }
    }

    private boolean noUserConflict(String userName) {

        return true;
    }

    public void createUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.i("sign up", "create user with email : success");
                    user = mAuth.getCurrentUser();
                    //            setDisplayName();
                } else {
                    view_info.setText("Cannot create user : " + task.getException());
                }
            }
        });
    }

    private boolean userNameCheckPassed(String userName) {
        if (TextUtils.isEmpty(userName) || userName.length() < 3) {
            view_info.setText("userName should has at least 3 characters");
            return false;
        } else {
            return true;
        }
    }

    private boolean emailCheckPassed(String email) {
        if (TextUtils.isEmpty(email) ) {
            view_info.setText("email should not be empty!");
            return false;
        } else if (emailNotFormatted(email)){
            view_info.setText("email is not well formatted!");
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordCheckPassed(String password1, String password2) {
        if((!password1.equals(password2)) || password1 == null) {
            view_info.setText("password should be same");
            return false;
        } else {
            return true;
        }
    }

    private boolean emailNotFormatted(String email) {
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }
}
