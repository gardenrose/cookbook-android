package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveKategorijaActivity extends AppCompatActivity {

    public TextView naslovRemove;
    public Button potvrdi, otkazi;
    public Spinner removeSpin;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public String selected;
    public ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_kategorija);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);

        naslovRemove = findViewById(R.id.textZaRemove);
        potvrdi = findViewById(R.id.potvrdiRemove);
        otkazi = findViewById(R.id.otkaziRemove);
        removeSpin = findViewById(R.id.spinnerZaRemove);
        data = new ArrayList<>();
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if(!snap.getKey().equals("admins")) {
                        data.add(snap.getKey());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveKategorijaActivity.this,android.R.layout.simple_spinner_item,data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                removeSpin.setAdapter(adapter);
                removeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                potvrdi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selected = removeSpin.getSelectedItem().toString();
                        //Toast.makeText(RemoveKategorijaActivity.this, selected, Toast.LENGTH_SHORT).show();
                        AlertDialog dialog = new AlertDialog.Builder(RemoveKategorijaActivity.this)
                                .setTitle("Potvrda brisanja")
                                .setMessage("Jeste li sigurni da želite izbrisati kategoriju?")
                                .setNegativeButton("OTKAŽI", null)
                                .setPositiveButton("POTVRDI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ref.child(selected).removeValue();
                                        finish();

                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}