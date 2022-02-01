package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    public RatingBar ratingBar;
    public TextView naslov, sastojci, sastojciSadrzaj, priprema, pripremaSadrzaj, kategorija, autor, ocjena;
    public ImageView fotkaRecepta;
    public Button urediBtn, ukloniBtn, ocijeniBtn;
    public FirebaseDatabase db;
    public DatabaseReference ref, ref2;
    public TextView userName;
    public Button logout;
    public FirebaseAuth mAuth;
    public String ocjenjivaci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalji_recepta);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(0);
        mAuth = FirebaseAuth.getInstance();
        ratingBar.setIsIndicator(false);
        naslov = findViewById(R.id.naslovVieww);
        autor = findViewById(R.id.autorVieww);
        ocjena = findViewById(R.id.ocjenaInfoVieww);
        kategorija = findViewById(R.id.kategorijaVieww);
        priprema = findViewById(R.id.priprema);
        sastojci = findViewById(R.id.sastojciView);
        userName = findViewById(R.id.textView3);
        userName.setText("");
        logout = findViewById(R.id.loginDetaljiBtn);
        urediBtn = findViewById(R.id.urediDetaljeBtn);
        ukloniBtn = findViewById(R.id.ukloniDetaljiBtn);
        ocijeniBtn = findViewById(R.id.ocijeniDetaljiBtn);
        urediBtn.setVisibility(View.GONE);
        ukloniBtn.setVisibility(View.GONE);
        ocijeniBtn.setVisibility(View.GONE);
        sastojciSadrzaj = findViewById(R.id.sastojciSadrzaj);
        pripremaSadrzaj = findViewById(R.id.pripremaSadrzaj);
        //urediBtn = findViewById(R.id.editBtn);
        fotkaRecepta = findViewById(R.id.imageView3);
        ratingBar.setIsIndicator(true);
        Intent intent = getIntent();
        String kateg = intent.getStringExtra("ime");
        String index = intent.getStringExtra("index");
        kategorija.setText(kateg + ", ");
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti").child(kateg).child(index);
        ref2 = db.getReference("recepti").child(kateg).child(index);
        //Toast.makeText(MainActivity3.this, intent.getStringExtra("logiraniKorisnik"), Toast.LENGTH_SHORT).show();
        userName.setText(intent.getStringExtra("logiraniKorisnik"));
        if(intent.getStringExtra("admin").equals("yes")) {
            urediBtn.setVisibility(View.VISIBLE);
            ukloniBtn.setVisibility(View.VISIBLE);
        }

        if (userName.getText().toString().equals("")) {
            logout.setText("PRIJAVA");
        } else {
            logout.setText("ODJAVA");
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ocjena.setText(dataSnapshot.child("ocijenilo").getValue().toString());
                Glide.with(MainActivity3.this).load(String.valueOf(dataSnapshot.child("slika").getValue().toString())).into(fotkaRecepta);
                naslov.setText(dataSnapshot.child("naslov").getValue().toString());
                autor.setText("Autor: " + dataSnapshot.child("autor").getValue().toString());
                ocjena.setText("Ocijenilo korisnika: " + dataSnapshot.child("ocijenilo").getValue().toString());
                sastojciSadrzaj.setText(dataSnapshot.child("sastojci").getValue().toString().replace("#","\n"));
                pripremaSadrzaj.setText(dataSnapshot.child("priprema").getValue().toString().replace("#","\n\n"));
                ratingBar.setRating(Float.parseFloat(dataSnapshot.child("ocjena").getValue().toString()));
                ocjenjivaci = dataSnapshot.child("ocjenjivaci").getValue().toString();

                if(dataSnapshot.child("autor").getValue().toString().equals(userName.getText().toString()) || userName.getText().toString().equals("")) {
                    ocijeniBtn.setVisibility(View.GONE);
                }
                else {
                    ocijeniBtn.setVisibility(View.VISIBLE);
                }

                ocijeniBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(MainActivity3.this, userName.getText().toString() + kateg + index + ocjenjivaci + ocjena.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(MainActivity3.this, OcijeniActivity.class);
                        intent2.putExtra("ocjenjivac", userName.getText().toString());
                        intent2.putExtra("kategorija", kateg);
                        intent2.putExtra("index", index);
                        intent2.putExtra("ocjenjivaci", ocjenjivaci);
                        intent2.putExtra("ukupnoocjena", dataSnapshot.child("ocijenilo").getValue().toString());
                        startActivity(intent2);

                    }
                });

                urediBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity3.this, AddRecipeActivity.class);
                        intent.putExtra("kat", kateg);
                        intent.putExtra("type", "edit");
                        intent.putExtra("user", userName.getText().toString());
                        intent.putExtra("index", index);
                        startActivity(intent);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ukloniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity3.this)
                        .setTitle("Potvrda brisanja")
                        .setMessage("Jeste li sigurni da želite izbrisati recept?")
                        .setNegativeButton("OTKAŽI", null)
                        .setPositiveButton("POTVRDI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ref2.removeValue();
                                Intent newIntent = new Intent();
                                startActivity(newIntent);
                            }
                        })
                        .show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, LoginActivity.class);
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