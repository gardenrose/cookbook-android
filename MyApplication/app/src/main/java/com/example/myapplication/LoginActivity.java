package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    Button login, register, noLog;
    EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.registerBtn);
        noLog = findViewById(R.id.noLoggedBtn);
        email = findViewById(R.id.emailTextView);
        password = findViewById(R.id.passTextView);
        email.setText("");
        password.setText("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);
        mAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(email.getText().toString(), password.getText().toString());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(email.getText().toString(), password.getText().toString());
            }
        });

        noLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("information", "");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }

    void createAccount(String userEmail, String userPassword){
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "authentification successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, user.toString(),Toast.LENGTH_LONG).show();}
                        else{
                            Toast.makeText(LoginActivity.this, "authentication failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    void loginUser(String userEmail, String userPassword){
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            redirect(user);
                            Toast.makeText(LoginActivity.this, "successful login",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "login failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    void redirect(FirebaseUser user){
        String email = user.getEmail();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("information", email);
        startActivity(intent);
    }

    // ovo je ako se odjavi s drugog activity-a, a ne s kategorija activity-a
    // da se i na prvom activity-u izbrise prijavljeni korisnik..
    // npr odjavimo se sa activity-a gdje su svi recepti, pojavi se ovaj ekran
    // kliknemo izlaz, i otvori se activity s kategorijama, bez ovoga bi ostao zadnji prijavljeni user
    // a ovako se brise, jer je odjavljen s drugog activity-a.
    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("information", "");
        startActivity(intent);
        finish();
    }
}