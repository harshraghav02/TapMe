package com.example.tapme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    Button signupbtn;
    EditText username;
    EditText email;
    EditText password;
    EditText repassword;
    TextView login;
    CircleImageView profileImg;
    Uri uri;
    String imageuri;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    String emailFormat = "[a-z0-9A-Z._-]+@[a-z]+\\.+[a-z]+";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait!!!!");
        progressDialog.setCancelable(false);
        signupbtn = findViewById(R.id.sgsignupbtn);
        username = findViewById(R.id.sgusername);
        email = findViewById(R.id.sgemail);
        password = findViewById(R.id.sgpass);
        repassword = findViewById(R.id.sgrepass);
        login = findViewById(R.id.signuptextview);
        profileImg = findViewById(R.id.sgprofile);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,login.class);
                startActivity(intent);
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String emailId = email.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String status = "Hey! I am using TapMe!";

                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(emailId) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)){
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Please Enter Valid Information!", Toast.LENGTH_SHORT).show();
                }
                else if(!emailId.matches(emailFormat)){
                    progressDialog.dismiss();
                    email.setError("Type a Valid Email Id");
                }
                else if(pass.length()<6){
                    progressDialog.dismiss();
                    password.setError("Password must be of 6 Characters or more");
                }
                else if(!pass.equals(repass)){
                    progressDialog.dismiss();
                    password.setError("Password Doesn't match!");
                }
                else{

                    auth.createUserWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();

                                DatabaseReference databaseReference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if(uri != null){
                                    storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users userss = new Users(id,user,emailId,pass,imageuri,status);
                                                        databaseReference.setValue(userss).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else{
                                                                    Toast.makeText(SignUp.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                            else{
                                                Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    String status = "Hey! I am using TapMe!";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/tapme-1e569.appspot.com/o/defaultprofile.jpg?alt=media&token=52c80289-52fe-42d8-84c3-22260518bb4a";
                                    Users userss = new Users(id,user,emailId,pass,imageuri,status);
                                    databaseReference.setValue(userss).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.show();
                                                Intent intent = new Intent(SignUp.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(SignUp.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select profile"),10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data!= null){
                uri = data.getData();
                profileImg.setImageURI(uri);
            }
        }
    }
}