package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReceptAdapter extends RecyclerView.Adapter<ReceptAdapter.ReceptViewHolder>{
    private List<Recept> localDataSet;
    private Context context;
    public String korisnik, isAdminLogged;

    public ReceptAdapter(List<Recept> dataSet, Context ctx, String usr, String isAdmin) {
        localDataSet = dataSet;
        context = ctx;
        korisnik = usr;
        isAdminLogged = isAdmin;
    }
    View view;

    @NonNull
    @Override
    public ReceptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recept_holder_layout,parent, false);
        return new ReceptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceptViewHolder holder, int position) {
        if (localDataSet.size() != 0) {
            localDataSet.get(position);
            holder.naslov.setText(localDataSet.get(position).naslov);
            Glide.with(view).load(String.valueOf(localDataSet.get(position).getSlika())).into(holder.fotka);

        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kategorija predmet = new Kategorija();
                predmet.setIme(localDataSet.get(holder.getAdapterPosition()).kategorija);
                Intent intent = new Intent(context, MainActivity3.class);
                intent.putExtra("ime", predmet.ime);
                intent.putExtra("naslov", localDataSet.get(holder.getAdapterPosition()).naslov);
                intent.putExtra("autor", localDataSet.get(holder.getAdapterPosition()).autor);
                intent.putExtra("priprema", localDataSet.get(holder.getAdapterPosition()).priprema);
                intent.putExtra("sastojci", localDataSet.get(holder.getAdapterPosition()).sastojci);
                intent.putExtra("ocijenilo", String.valueOf(localDataSet.get(holder.getAdapterPosition()).ocijenilo));
                intent.putExtra("ocjena", localDataSet.get(holder.getAdapterPosition()).ocjena);
                intent.putExtra("slika", localDataSet.get(holder.getAdapterPosition()).slika);
                intent.putExtra("logiraniKorisnik", korisnik);
                intent.putExtra("index", localDataSet.get(holder.getAdapterPosition()).index);
                intent.putExtra("admin", isAdminLogged);
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

    public class ReceptViewHolder extends RecyclerView.ViewHolder{
        public TextView naslov;
        public LinearLayout parentLayout;
        public ImageView fotka;

        public ReceptViewHolder(@NonNull View itemView) {
            super(itemView);
            this.naslov = (TextView)itemView.findViewById(R.id.nameTextView);
            this.parentLayout = itemView.findViewById(R.id.linearLyout2);
            this.fotka = itemView.findViewById(R.id.imageView2);
        }
    }

}
