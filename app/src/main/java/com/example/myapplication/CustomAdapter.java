package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    // kontekst
    Context context;

    // nizovi za naslove i autore
    ArrayList<String> film_id, ime, godina,opis,zanr;
    ArrayList<byte[]> slike;
    Activity activity;

    // konstruktor klase CustomAdapter
    public CustomAdapter(Context context, ArrayList film_id, ArrayList ime,ArrayList zanr, ArrayList godina,ArrayList opis,ArrayList slike, Activity activity) {
        this.context = context;
        this.film_id = film_id;
        this.ime = ime;
        this.godina = godina;
        this.opis=opis;
        this.zanr = zanr;
        this.slike = slike;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.red, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.filmIme.setText(String.valueOf(ime.get(position)));
        holder.filmGodina.setText(String.valueOf(godina.get(position)));
        holder.filmOpis.setText(String.valueOf(opis.get(position)));
        holder.filmZanrovi.setText(String.valueOf(zanr.get(position)));
        holder.filmSlika.setImageBitmap(DbBitmapUtility.getImage(slike.get(position)));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);//ovde

                intent.putExtra("id", film_id.get(position));
                intent.putExtra("ime", String.valueOf(ime.get(position)));
                intent.putExtra("godina", String.valueOf(godina.get(position)));
                intent.putExtra("opis", String.valueOf(opis.get(position)));
                intent.putExtra("zanr", String.valueOf(zanr.get(position)));
                //intent.putExtra("slike", slike.get(position));
                ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
                extras.putExtra("slike", slike.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ime.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView filmIme, filmGodina,filmOpis,filmZanrovi;
        ImageView filmSlika;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            filmIme = itemView.findViewById(R.id.AIme);
            filmGodina = itemView.findViewById(R.id.AGodina);
            filmOpis = itemView.findViewById(R.id.AOpis);
            filmZanrovi = itemView.findViewById(R.id.AZanr);
            filmSlika = itemView.findViewById(R.id.slikaAFilma);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
