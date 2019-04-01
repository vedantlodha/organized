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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private Button buttonLogin;
    private EditText editTextEmail,editTextPassword;
    private TextView textViewForgotPassword, textViewRegister;
    private String email, password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
//    private GoogleSignInOptions googleSignInOptions;
//    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        buttonLogin = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        textViewRegister = findViewById(R.id.textViewRegister);
        buttonLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
//        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if( currentUser != null){
            nextScreen();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin)
            login();
        else if(v == textViewForgotPassword){
            forgotPassword();
        }
        else if (v == textViewRegister){
            register();
        }

    }
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
    private void login(){
        progressDialog.setMessage("Logging in");
        progressDialog.show();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter valid password", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if ( task.isSuccessful()){
                            nextScreen();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void register(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    private void forgotPassword(){

    }
    private void nextScreen(){
//        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

    }
}
