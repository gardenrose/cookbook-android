package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddKategorijaActivity extends AppCompatActivity {

    public EditText kategorijaTxt;
    public Button potvrdaBtn, otkaziBtn;
    public FirebaseDatabase db;
    public DatabaseReference ref;
    public boolean postoji;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kategorija);
        kategorijaTxt = findViewById(R.id.unosKategorijeEdit);
        potvrdaBtn = findViewById(R.id.dodajKategorijuBtn);
        otkaziBtn = findViewById(R.id.otkaziDodavanjeKat);
        db = FirebaseDatabase.getInstance("https://america-10016-default-rtdb.firebaseio.com/");
        ref = db.getReference("recepti");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_baseline_food_bank_24);
        postoji = false;

        potvrdaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postoji = false;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (kategorijaTxt.getText().toString().equals("")) {
                            Toast.makeText(AddKategorijaActivity.this, "Ime kategorije ne smije biti prazno", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snap.getKey().toString().equals(kategorijaTxt.getText().toString())) {
                                    Toast.makeText(AddKategorijaActivity.this, "Kategorija vec postoji !", Toast.LENGTH_SHORT).show();
                                    postoji = true;
                                    break;
                                }
                            }
                            if (!postoji) {
                                ref.child(kategorijaTxt.getText().toString()).child("MaxId").setValue(1);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });




        otkaziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}