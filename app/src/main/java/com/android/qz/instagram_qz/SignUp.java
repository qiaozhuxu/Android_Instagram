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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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
    }

    public void checkEmailHasExisted(View view) {
        email = view_email.getText().toString();
        createUser(email,"dummyPass");
        deleteUSer();
    }

    private void deleteUSer() {
        user.delete();
    }

    public void signup(View view) {
        userName = view_name.getText().toString();
        email = view_email.getText().toString();
        password1 = view_password1.getText().toString();
        password2 = view_password2.getText().toString();

        if (passwordCheckPassed(password1, password2)) {
            createUser(email,password1);

            if(user != null) {
                if (userNameCheckPassed(userName)) {
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build();
                    user.updateProfile(profileUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        startActivity(new Intent( SignUp.this, TakePicture.class));
                                    } else {
                                        startActivity(new Intent( SignUp.this, MainActivity.class));
                                    }
                                }
                            });
                } else {
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                }
            }
        } else {
            btn_signup.setText("Required info is not correct, Try Again!");
        }
    }


    public void createUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            view_emailAvailable.setText("You can go on with this email ");
                            user = task.getResult().getUser();
                        } else {
                            if(task.getException().getMessage().equals("The email address is already in use by another account.")) {
                                view_emailAvailable.setText("This email ID already used by someone else");
                            }
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

    private boolean passwordCheckPassed(String password1, String password2) {
        if((!password1.equals(password2)) || password1 == null) {
            view_info.setText("password should be same");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }
}
