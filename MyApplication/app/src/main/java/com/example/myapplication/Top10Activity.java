package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Top10Activity extends AppCompatActivity {

    public Button odjava, natrag;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public RecyclerView recyclerView;
    public List<Recept> dataSet;
    public ReceptAdapter adapter;
    public FirebaseAuth mAuth;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);

        odjava = findViewById(R.id.odjavaTop10);
        natrag = findViewById(R.id.natragBtn);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti");
        dataSet = new ArrayList<Recept>();
        adapter = new ReceptAdapter(dataSet, this, "", "no");
        recyclerView = (RecyclerView) findViewById(R.id.recycle100);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Top10Activity.this));
        recyclerView.requestFocus();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(MainActivity2.this, kategorija, Toast.LENGTH_SHORT).show();
                dataSet.clear();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    if (!s.getKey().equals("admins")) {
                        String kategorija = s.getKey();
                        for (DataSnapshot snap2 : dataSnapshot.getChildren()) {
                            if(!snap2.getKey().equals("MaxId")) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    Toast.makeText(Top10Activity.this, snap.toString(), Toast.LENGTH_SHORT).show();
                                    Recept recept = new Recept();
                                    /*recept.setKategorija(kategorija);
                                    recept.setNaslov(snap.child("naslov").getValue().toString());
                                    recept.setOcjena(Float.parseFloat(snap.child("ocjena").getValue().toString()));
                                    recept.setPriprema(snap.child("priprema").getValue().toString());
                                    recept.setSlika(snap.child("slika").getValue().toString());
                                    recept.setAutor(snap.child("autor").getValue().toString());
                                    recept.setSastojci(snap.child("sastojci").getValue().toString());
                                    recept.setOcijenilo(Integer.parseInt(snap.child("ocijenilo").getValue().toString()));
                                    recept.setOcjenjivaci(snap.child("ocjenjivaci").getValue().toString());
                                    recept.setIndex(snap.getKey());*/
                                    dataSet.add(recept);
                                    Toast.makeText(Top10Activity.this, snap.child("naslov").getValue().toString(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }

                }

                Recept tmp;
                for (int i = 0; i < dataSet.size(); i++) {
                    for (int j = i ; j < dataSet.size(); j++) {
                        if (dataSet.get(i).getOcjena() > dataSet.get(j).getOcjena()) {
                            tmp = dataSet.get(j);
                            dataSet.set(j, dataSet.get(i));
                            dataSet.set(i, tmp);
                        }
                    }
                }
                ArrayList<Recept> al2 = new ArrayList<Recept>(dataSet.subList(0, 5));
                dataSet = al2;

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ReceptAdapter(dataSet, Top10Activity.this, "", "no");
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Top10Activity.this, LoginActivity.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
            }
        });
        natrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Top10Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}