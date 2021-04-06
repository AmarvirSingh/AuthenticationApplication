package com.example.authenticationapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etAge;
    private Button btnRegister;
    private ProgressBar progressBar;
    private TextView signInTextView;

     private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        signInTextView = findViewById(R.id.signInTextView);



        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.VISIBLE);
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, UserActivity.class));
            progressBar.setVisibility(View.INVISIBLE);
            finish();
        }

        if (mAuth.getCurrentUser() == null){
            progressBar.setVisibility(View.INVISIBLE);
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String age = etAge.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (name.isEmpty()){
                    etName.setError("Please Enter Your Full Name");
                    etName.requestFocus();
                    return;
                }
                if (age.isEmpty()){
                    etAge.setError("Please Enter Your Age");
                    etAge.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    etEmail.setError("Please Enter Your Email address");
                    etEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Enter Valid email");
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    etPassword.setError("Enter Password please");
                    etPassword.requestFocus();
                    return;
                }
                if (password.length() < 6){
                    etPassword.setError("length should be greater than 6");
                    etPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    UserObject user = new UserObject(name,age, email);
                                    FirebaseDatabase.getInstance().getReference("User")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Personal")
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainActivity.this, "User registered successfully ", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Can not register, Try again in second else ", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Can not register, Try again later", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });



            }
        });



        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


    }
}