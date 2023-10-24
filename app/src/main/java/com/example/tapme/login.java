package com.example.tapme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {
    Button login;
    EditText email;
    EditText password;
    String emailFormat = "[a-z0-9A-Z._-]+@[a-z]+\\.+[a-z]+";
    TextView signup;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.sgsignupbtn);
        email = findViewById(R.id.sgemail);
        password = findViewById(R.id.sgpass);
        signup = findViewById(R.id.signuptextview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtxt = email.getText().toString();
                String pass = password.getText().toString();

                if(emailtxt.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(login.this,"Enter the Email",Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(login.this,"Enter the Password",Toast.LENGTH_SHORT).show();
                }
                else if(!emailtxt.matches(emailFormat)){
                    progressDialog.dismiss();
                    email.setError("Incorrect Email Format");
                }
                else if(pass.length() < 6){
                    progressDialog.dismiss();
                    password.setError("Password should be more than 6 characters");
                }
                else{

                    firebaseAuth.signInWithEmailAndPassword(emailtxt,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.show();
                                try{
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                catch (Exception e){
                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(login.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });



    }
}