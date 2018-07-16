package com.example.marcelolongen.expensemanager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference ref_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref_users = database.getReference("users");

        Drawable thumb = ContextCompat.getDrawable(getBaseContext(), R.drawable.bg);

        final Button submitButton = findViewById(R.id.submitButton);

        final Button createUser = findViewById(R.id.createUserButton);
        createUserClickListenet(submitButton, createUser);

        submitButtonLoginClickListener(submitButton);
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
                        // creating unique user id
                       final String id = ref_users.push().getKey();

                        if (!TextUtils.isEmpty(email.getText().toString().trim()) &&
                                !TextUtils.isEmpty(password.getText().toString().trim()) &&
                                !TextUtils.isEmpty(confirmPassword.getText().toString().trim()) &&
                                password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference users = root.child("users");
                            users.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (!snapshot.child(email.getText().toString().trim()).exists()) {
                                        User user = new User(id, email.getText().toString().trim(), password.getText().toString().trim());

                                        //add the user as a child of "ref_users"
                                        ref_users.child(email.getText().toString().trim()).setValue(user);

                                        Toast.makeText(MainActivity.this, "User successfully created", Toast.LENGTH_SHORT).show();
                                        confirmPassword.setVisibility(View.GONE);
                                        submitButton.setText("Login");
                                        submitButtonLoginClickListener(submitButton);
                                        createUserClickListenet(submitButton,createUser);

                                    }else{
                                        Toast.makeText(MainActivity.this, "User already exists.", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        } else {
                            Toast.makeText(MainActivity.this, "Please fill fields correctly.", Toast.LENGTH_SHORT).show();
                        }
                        //creating user object


                    }
                });
            }
        });
    }

    private void submitButtonLoginClickListener(Button submitButton) {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.password);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                if (username.getText().toString().equals("marcelo") && password.getText().toString().equals("123")) {
                    Intent intent = new Intent(getBaseContext(), Overview.class);
                    startActivity(intent);
                } else {
                    toast("Incorrect username/password");
                }
            }
        });
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
