package com.example.todo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRegister;
    private EditText editTextEmail,editTextPassword, editTextConfirmPassword;
    private String email, password, confirmPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // ActionBar actionBar = getSupportActionBar();
       // actionBar.hide();
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Registering, please wait");
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString();
            confirmPassword = editTextConfirmPassword.getText().toString();
            if (TextUtils.isEmpty(email)){
                Toast.makeText(getApplicationContext(), "Enter valid email", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password) ){
                Toast.makeText(getApplicationContext(), "Enter valid password", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.length() < 8){
                Toast.makeText(getApplicationContext(), "Password too short. Minimum 8 characters required", Toast.LENGTH_SHORT).show();
                editTextConfirmPassword.setText("");
                editTextPassword.setText("");
                return;
            }
            if(!TextUtils.equals(password, confirmPassword)){
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                editTextConfirmPassword.setText("");
                return;
            }
            register();
        }

    }
    private void register(){
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    nextActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(),"registration failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void nextActivity(){
        startActivity(new Intent(getApplicationContext(), Home.class));
    }
}
