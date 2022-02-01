package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {
    public Button dodaj, otkazi;
    public TextView view1, view2, view3, view4, view5;
    public Spinner spn;
    public EditText naslovEdit, slikaEdit, pripremaEdit, sastojciEdit;
    public FirebaseDatabase db;
    public DatabaseReference ref, ref2;
    public ArrayList<String> data;
    public String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);

        dodaj = findViewById(R.id.dodajButton);
        otkazi = findViewById(R.id.otkazivanjeButton);
        spn = findViewById(R.id.spinnerKategorija);
        naslovEdit = findViewById(R.id.unosNovogNaslova);
        slikaEdit = findViewById(R.id.poveznicaEdit);
        pripremaEdit = findViewById(R.id.pripremaEdittxxt);
        sastojciEdit = findViewById(R.id.sastojciEdittxt);
        view1 = findViewById(R.id.naslovVieww);
        view2 = findViewById(R.id.kategorijaTxxt);
        view3 = findViewById(R.id.poveznicaTxt);
        view4 = findViewById(R.id.unosSastojakaTxt);
        view5 = findViewById(R.id.pripremaTxxtView);
        Intent intent = getIntent();
        String kateg = intent.getStringExtra("kat");
        String type = intent.getStringExtra("type");
        String user = intent.getStringExtra("user");
        String adm = intent.getStringExtra("admin");
        String index = intent.getStringExtra("index");
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti");
        ref2 = db.getReference("recepti");
        data = new ArrayList<>();

        otkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if(!snap.getKey().equals("admins")) {
                        data.add(snap.getKey());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRecipeActivity.this,android.R.layout.simple_spinner_item,data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn.setAdapter(adapter);
                spn.setSelection(adapter.getPosition(kateg));
                if (type.equals("edit")) {
                    dodaj.setText("Spremi");
                    spn.setEnabled(false);
                    naslovEdit.setText(snapshot.child(kateg).child(index).child("naslov").getValue().toString());
                    sastojciEdit.setText(snapshot.child(kateg).child(index).child("sastojci").getValue().toString().replace("#",","));
                    pripremaEdit.setText(snapshot.child(kateg).child(index).child("priprema").getValue().toString().replace("#","\n\n"));
                    slikaEdit.setText(snapshot.child(kateg).child(index).child("slika").getValue().toString());
                }
                spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d("selektirano", spn.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                dodaj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (naslovEdit.getText().toString().equals("") || pripremaEdit.getText().toString().equals("")
                                || sastojciEdit.getText().toString().equals("") || slikaEdit.getText().toString().equals("")) {
                            Toast.makeText(AddRecipeActivity.this, "Nijedno polje ne smije biti prazno", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(type.equals("edit")) {
                                Toast.makeText(AddRecipeActivity.this, snapshot.child(kateg).child(index).child("autor").getValue().toString(), Toast.LENGTH_SHORT).show();
                                ref2.child(kateg).child(String.valueOf(index)).child("naslov").setValue(naslovEdit.getText().toString());
                                ref2.child(kateg).child(String.valueOf(index)).child("sastojci").setValue(sastojciEdit.getText().toString().replace(",","#"));
                                ref2.child(kateg).child(String.valueOf(index)).child("priprema").setValue(pripremaEdit.getText().toString().replace("\n\n", "#"));
                                ref2.child(kateg).child(String.valueOf(index)).child("slika").setValue(slikaEdit.getText().toString());
                                finish();
                            }
                            else if (type.equals("add")) {
                                extracted(snapshot, user, adm);
                                finish();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void extracted(@NonNull DataSnapshot snapshot, String user, String adm) {
        String novaKategorija = spn.getSelectedItem().toString();
        int max = Integer.parseInt(snapshot.child(novaKategorija).child("MaxId").getValue().toString());
        String nasl = naslovEdit.getText().toString();
        String autor = user;
        String slikaa = slikaEdit.getText().toString();
        String prip = pripremaEdit.getText().toString().replace("\n\n","#");
        String sast = sastojciEdit.getText().toString().replace(",","#");
        Recept r = new Recept(novaKategorija, nasl, slikaa, prip, 0, 0, autor, sast, "", String.valueOf(max));
        ref2.child(novaKategorija).child(String.valueOf(max)).setValue(r);
        ref2.child(novaKategorija).child("MaxId").setValue(max+1);
        ref2.child(novaKategorija).child(String.valueOf(max)).child("index").removeValue();
        ref2.child(novaKategorija).child(String.valueOf(max)).child("kategorija").removeValue();
        Intent intent007 = new Intent(AddRecipeActivity.this, MainActivity2.class);
        intent007.putExtra("logiraniUser", user);
        intent007.putExtra("kategorija", novaKategorija);
        intent007.putExtra("admin", adm);
        startActivity(intent007);
        finish();
    }
}