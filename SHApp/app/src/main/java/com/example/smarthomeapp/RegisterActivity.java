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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register2;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register2 = (Button) (findViewById(R.id.register2));
        register2.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail2);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword2);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register2:
                register2();
                break;
        }
    }

    private void register2(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Password has to be 6+ characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                        progressBar2.setVisibility(View.GONE);

                                        //return to login
//                                        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                    }else{
                                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });

    }

}