package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KategorijaAdapter extends RecyclerView.Adapter<KategorijaAdapter.KategorijaViewHolder>{
    private List<Kategorija> localDataSet;
    public String logiraniKorisnik, isAdmin;
    private Context context;
    //private NoteAdapter.OnClickListener EC_Listener;

    public KategorijaAdapter(List<Kategorija> dataSet, Context ctx, String usr, String admin) {
        localDataSet = dataSet;
        context = ctx;
        logiraniKorisnik = usr;
        isAdmin = admin;
    }

    View view;

    @NonNull
    @Override
    public KategorijaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kategorija_holder_layout,parent, false);
        return new KategorijaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategorijaViewHolder holder, int position) {
        if (localDataSet.size() != 0) {
            localDataSet.get(position);
            holder.ime.setText(localDataSet.get(position).ime);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kategorija predmet = new Kategorija();
                predmet.setIme(localDataSet.get(holder.getAdapterPosition()).ime);
                //Toast.makeText(context, predmet.ime, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity2.class);
                intent.putExtra("logiraniUser", logiraniKorisnik);
                intent.putExtra("kategorija", predmet.ime.replace(" \uD83C\uDF72" + "\uD83D\uDC08 ",""));
                intent.putExtra("admin", isAdmin);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("data_set_size", String.valueOf(localDataSet.size()));
        if (localDataSet != null) {
            return localDataSet.size();
        } else {
            return 0;
        }
    }

    public class KategorijaViewHolder extends RecyclerView.ViewHolder{
        public TextView ime;
        public LinearLayout parentLayout;

        public KategorijaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ime = (TextView)itemView.findViewById(R.id.nazivPredmetaView);
            this.parentLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

}
