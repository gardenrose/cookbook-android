package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// POCETNI AKTIVITY, SA KATEGORIJAMA
public class MainActivity extends AppCompatActivity {

    public TextView userName;
    public Button logout;
    public Button top10;
    public FloatingActionButton add;
    public FloatingActionButton remove;
    public FirebaseAuth mAuth;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public RecyclerView recyclerView;
    public List<Kategorija> dataSet;
    public KategorijaAdapter adapter;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
    public boolean isAdminLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorije_activity);

        userName = findViewById(R.id.loggedUser);
        logout = findViewById(R.id.logoutBtn);
        top10 = findViewById(R.id.top10btn);
        add = findViewById(R.id.addKategorijaBtn);
        remove = findViewById(R.id.removeKategorijaBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti");
        userName.setText("");
        dataSet = new ArrayList<Kategorija>();
        adapter = new KategorijaAdapter(dataSet, this, userName.getText().toString(), "no");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.requestFocus();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);
        add.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        Intent intent = getIntent();
        String usr = intent.getStringExtra("information");
        userName.setText(usr);

        if(userName.getText().toString().equals("")) {
            logout.setText("Prijava/registracija");
            top10.setVisibility(View.GONE);
        } else {
            logout.setText("Odjava");
            top10.setVisibility(View.VISIBLE);
        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSet.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if(!snap.getKey().equals("admins")) {
                        Kategorija kat = new Kategorija();
                        kat.setIme(" \uD83C\uDF72" + "\uD83D\uDC08 " + snap.getKey());
                        dataSet.add(kat);
                    }
                    else {
                        for (DataSnapshot snap2 : snap.getChildren()) {
                            if (snap2.getValue().toString().equals(userName.getText().toString())) {
                                add.setVisibility(View.VISIBLE);
                                remove.setVisibility(View.VISIBLE);
                                adapter.isAdmin = "yes";
                                break;
                            }
                        }
                    }
                }
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new KategorijaAdapter(dataSet, MainActivity.this, userName.getText().toString(), adapter.isAdmin);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddKategorijaActivity.class);
                        startActivity(intent);
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, RemoveKategorijaActivity.class);
                        startActivity(intent);
                    }
                });
                top10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Top10Activity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(userName.getText().toString().equals("")) {
            logout.setText("Prijava");
        } else {
            logout.setText("Odjava");
        }
    }


}