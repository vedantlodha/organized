package com.example.todo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail;
    private Button buttonNext;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonNext = findViewById(R.id.buttonNext);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        buttonNext.setOnClickListener(this);



    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        builder.setMessage("Password reset link sent to registered email address")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ForgotPassword.this, Login.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onClick(View v) {
        email = editTextEmail.getText().toString().trim();
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    showAlert();
                    Toast.makeText(ForgotPassword.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPassword.this, "User does not exists", Toast.LENGTH_LONG).show();
                    editTextEmail.setText("");
                }
            }
        });
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }
}
