package com.example.smarthomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText editTextEmailToReset;
    private Button resetPassword;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmailToReset = (EditText) findViewById(R.id.editTextEmailToReset);
        resetPassword = (Button) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = editTextEmailToReset.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmailToReset.setError("Email is required");
            editTextEmailToReset.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailToReset.setError("Please provide valid email");
            editTextEmailToReset.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                }else{
                    Toast.makeText(ForgotPassword.this, "Error occurred, try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}