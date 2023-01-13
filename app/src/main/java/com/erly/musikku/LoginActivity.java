package com.erly.musikku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editEmail = findViewById(R.id.emailL);
        editPassword = findViewById(R.id.passwordL);
        btnLogin = findViewById(R.id.btn_loginL);
        btnRegister = findViewById(R.id.btn_registerL);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Mohon Menunggu");
        progressDialog.setCancelable(false);

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
        btnLogin.setOnClickListener(v -> {
            if (editEmail.getText().length() <= 0 || editPassword.getText().length() <= 0) {
                Toast.makeText(getApplicationContext(),"Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                login(editEmail.getText().toString(), editPassword.getText().toString());
            }


        });
    }

    private void login(String  email, String password){
        // CODING LOGIN
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() || task.getResult()!=null){
                    if(task.getResult().getUser()!=null){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        reload();
                    }else{
                        Toast.makeText(getApplicationContext(),"Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Login Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}