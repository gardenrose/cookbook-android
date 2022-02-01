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

public class MainActivity2 extends AppCompatActivity {

    public TextView userName;
    public Button logout;
    public FloatingActionButton add;
    public FirebaseAuth mAuth;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public RecyclerView recyclerView;
    public List<Recept> dataSet;
    public ReceptAdapter adapter;
    public String kategorija;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepti_activity);

        userName = findViewById(R.id.loggedUserRecept);
        logout = findViewById(R.id.loginOutBtn);
        add = findViewById(R.id.addReceptBtn);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        userName.setText("");
        kategorija = intent.getStringExtra("kategorija");
        userName.setText(intent.getStringExtra("logiraniUser"));
        //Toast.makeText(MainActivity2.this, intent.getStringExtra("logiraniUser"), Toast.LENGTH_SHORT).show();
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti").child(kategorija);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);
        dataSet = new ArrayList<Recept>();
        String adm = intent.getStringExtra("admin");
        adapter = new ReceptAdapter(dataSet, this, userName.getText().toString(), intent.getStringExtra("admin"));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        recyclerView.requestFocus();

        if(userName.getText().toString().equals("")) {
            add.setVisibility(View.GONE);
        }else {
            add.setVisibility(View.VISIBLE);
        }


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(MainActivity2.this, kategorija, Toast.LENGTH_SHORT).show();
                dataSet.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if(!snap.getKey().equals("MaxId")) {
                        Recept recept = new Recept();
                        recept.setKategorija(kategorija);
                        recept.setNaslov(snap.child("naslov").getValue().toString());
                        recept.setOcjena(Float.parseFloat(snap.child("ocjena").getValue().toString()));
                        recept.setPriprema(snap.child("priprema").getValue().toString());
                        recept.setSlika(snap.child("slika").getValue().toString());
                        recept.setAutor(snap.child("autor").getValue().toString());
                        recept.setSastojci(snap.child("sastojci").getValue().toString());
                        recept.setOcijenilo(Integer.parseInt(snap.child("ocijenilo").getValue().toString()));
                        recept.setOcjenjivaci(snap.child("ocjenjivaci").getValue().toString());
                        recept.setIndex(snap.getKey());
                        dataSet.add(recept);
                        //Toast.makeText(MainActivity2.this, snap.child("naslov").getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ReceptAdapter(dataSet, MainActivity2.this, userName.getText().toString(), intent.getStringExtra("admin"));
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity2.this, AddRecipeActivity.class);
                        intent.putExtra("kat", kategorija);
                        intent.putExtra("max", dataSnapshot.child("MaxId").getValue().toString());
                        intent.putExtra("type", "add");
                        intent.putExtra("user", userName.getText().toString());
                        intent.putExtra("admin", adm);
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
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
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