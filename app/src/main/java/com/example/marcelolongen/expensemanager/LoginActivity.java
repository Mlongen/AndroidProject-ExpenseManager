package com.example.marcelolongen.expensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private String userFromFacebook;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();



        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("User", "");
        if (!user.equals("")) {
            Intent intent = new Intent(getBaseContext(), Overview.class);
            intent.putExtra("user", user);
            startActivity(intent);
            toastSuccess("Successfully logged in.");
        }



        TextView forgotPassword = findViewById(R.id.forgotPassword);


        Drawable thumb = ContextCompat.getDrawable(getBaseContext(), R.drawable.bg);

        final Button submitButton = findViewById(R.id.submitButton);

        final Button createUser = findViewById(R.id.createUserButton);
        createUserClickListenet(submitButton, createUser);

        submitButtonLoginClickListener(submitButton);

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }


            @Override
            public void onCancel() {
                // App code
                Toasty.error(LoginActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toasty.error(LoginActivity.this, "Error. Try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void createUser(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (password.length() > 5) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        toastSuccess("User created. Please sign-in.");

                    } else {
                        // If sign in fails, display a message to the user.
                        Toasty.error(getApplicationContext(), "User already exists.",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toasty.error(getApplicationContext(), "Password is too short.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        db = Database.getInstance();
        db.readContentsFromFile(user.getUid());

        Toasty.success(getApplicationContext(), "Item objects size: " +  db.getItemObjects().size(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Overview.class);
        intent.putExtra("user", user.getUid());
        startActivity(intent);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }


    }

    private void createUserClickListenet(final Button submitButton, final Button createUser) {
        createUser.setText("Create user");
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText email = findViewById(R.id.username);
                final EditText password = findViewById(R.id.password);
                final EditText confirmPassword = findViewById(R.id.confirmPassword);
                confirmPassword.setVisibility(View.VISIBLE);
                submitButton.setText("Create");
                createUser.setText("Cancel");
                createUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmPassword.setVisibility(View.GONE);
                        submitButton.setText("Login");
                        submitButtonLoginClickListener(submitButton);
                        createUserClickListenet(submitButton,createUser);
                    }
                });
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!TextUtils.isEmpty(email.getText().toString().trim()) &&
                                !TextUtils.isEmpty(password.getText().toString().trim()) &&
                                !TextUtils.isEmpty(confirmPassword.getText().toString().trim()) &&
                                password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                            createUser(email.getText().toString().trim(), password.getText().toString().trim());
                            confirmPassword.setVisibility(View.GONE);
                            submitButton.setText("Login");
                            submitButtonLoginClickListener(submitButton);
                            createUserClickListenet(submitButton,createUser);
//

                        } else {
                            Toasty.error(LoginActivity.this, "Please fill fields correctly.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    private void submitButtonLoginClickListener(final Button submitButton) {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText email = findViewById(R.id.username);
                final EditText password = findViewById(R.id.password);

                if (!TextUtils.isEmpty(email.getText().toString().trim()) &&
                        !TextUtils.isEmpty(password.getText().toString().trim())) {
                    signInUser(email.getText().toString().trim(), password.getText().toString().trim());
                } else {
                    Toasty.error(LoginActivity.this, "Please input e-mail and password correctly.", Toast.LENGTH_SHORT).show();
                }
                


            }
        });
    }

    private void toastSuccess(String message) {
        Toasty.Config.getInstance().setInfoColor(getResources().getColor(R.color.colorPrimary)).apply();
        Toasty.success(this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toasty.error(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}
