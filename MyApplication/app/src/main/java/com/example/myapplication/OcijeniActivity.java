package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OcijeniActivity extends AppCompatActivity {

    public TextView tekst;
    public RatingBar bar;
    public Button posalji, otkazi;
    public FirebaseDatabase db;
    public DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(OcijeniActivity.this, "BILI", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocijeni);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);

        tekst = findViewById(R.id.randomTextView);
        bar = findViewById(R.id.ratingBar2);
        otkazi = findViewById(R.id.otkaziOcjenjivanje);
        posalji = findViewById(R.id.posaljiOcjenu);

        Intent intent = getIntent();
        int ukupno = Integer.parseInt(intent.getStringExtra("ukupnoocjena"));
        String ocjenjivac = intent.getStringExtra("ocjenjivac");
        String ocjenjivaci = intent.getStringExtra("ocjenjivaci");
        String kategorija = intent.getStringExtra("kategorija");
        String index = intent.getStringExtra("index");
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti").child(kategorija).child(index);



        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRating(ocjenjivac, bar.getRating(), ocjenjivaci, ukupno);
                finish();
            }
        });


        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void updateRating(String user, float novaOcjena, String ocjenjivaciString, int total) {
        String[] listaKorisnikaIOcjena = ocjenjivaciString.split("#");
        List<Float> ocjene = new ArrayList<>();
        List<String> listakorisnikaIOcjenaArray = new ArrayList<>();
        boolean korisnikOcijenio = false;
        String noviString;
        float sum = 0, average;
        String tmp;
        if(ocjenjivaciString.equals("")) {
            noviString = user + ":" + String.valueOf(novaOcjena);
            total += 1;
            average = novaOcjena;
        }
        else {
            for (String s : listaKorisnikaIOcjena) {
                tmp = s;
                if (s.contains(user)) {
                    korisnikOcijenio = true;
                    tmp = s.replace(s.split(":")[1], String.valueOf(novaOcjena));
                }
                listakorisnikaIOcjenaArray.add(tmp);
                ocjene.add(Float.parseFloat(tmp.split(":")[1]));
                sum += Float.parseFloat(tmp.split(":")[1]);
            }
            if (!korisnikOcijenio) {
                total += 1;
                ocjene.add(novaOcjena);
                listakorisnikaIOcjenaArray.add(user + ":" + String.valueOf(novaOcjena));
                sum += novaOcjena;
            }
            noviString = String.join("#", listakorisnikaIOcjenaArray);
            average = sum/ocjene.size();
        }


        ref.child("ocijenilo").setValue(total);
        ref.child("ocjenjivaci").setValue(noviString);
        ref.child("ocjena").setValue(average);
    }
}