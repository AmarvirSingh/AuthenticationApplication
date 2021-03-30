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

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnLogin;
    private ProgressBar progressBarLogin;
    private TextView createAccountTextVie, forgetPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        createAccountTextVie =findViewById(R.id.createAccountTextVie);


        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString().trim();
                String password = etPasswordLogin.getText().toString().trim();

                if (email.isEmpty()){
                    etEmailLogin.setError("Please enter email");
                    etEmailLogin.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmailLogin.setError("Please provide valid email address");
                    etEmailLogin.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    etPasswordLogin.setError("please enter password");
                    etPasswordLogin.requestFocus();
                    return;
                }

                progressBarLogin.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // redirect to user profile
                            startActivity(new Intent(LoginActivity.this,UserActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "failed to logfin", Toast.LENGTH_SHORT).show();
                            progressBarLogin.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        createAccountTextVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}