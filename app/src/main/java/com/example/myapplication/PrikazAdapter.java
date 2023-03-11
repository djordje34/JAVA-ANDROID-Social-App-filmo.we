package com.example.myapplication;

import static androidx.core.content.ContextCompat.getDrawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PrikazAdapter extends RecyclerView.Adapter<PrikazAdapter.MyViewHolder>{

    private boolean which;
    // kontekst
    Context context;
    ImageButton sacuvaj;
    // nizovi za naslove i autore
    ArrayList<String> film_id, ime, godina,opis,zanr;
    ArrayList<byte[]> slike;
    Activity activity;

    // konstruktor klase CustomAdapter
    public PrikazAdapter(Context context, ArrayList film_id, ArrayList ime,ArrayList zanr, ArrayList godina,ArrayList opis,ArrayList slike, Activity activity,boolean which) {
        this.context = context;
        this.film_id = film_id;
        this.ime = ime;
        this.godina = godina;
        this.opis=opis;
        this.zanr = zanr;
        this.slike = slike;
        this.activity = activity;
        this.which = which;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.prikaz, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.filmIme.setText(String.valueOf(ime.get(position)));
        holder.filmGodina.setText(String.valueOf(godina.get(position)));
        holder.filmOpis.setText(String.valueOf(opis.get(position)));
        holder.filmZanrovi.setText(String.valueOf(zanr.get(position)));
        holder.filmSlika.setImageBitmap(DbBitmapUtility.getImage(slike.get(position)));
        if(this.which) {
            holder.obrisi.setVisibility(View.GONE);
        }
        else{
            holder.sacuvaj.setVisibility(View.GONE);
        }
        holder.sacuvaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent temp = ((Activity) context).getIntent();
                    DatabaseHelper db = new DatabaseHelper(context);
                    String id = temp.getStringExtra("id");
                    db.insertData(DatabaseHelper.USER_FILM,new String[]{film_id.get(position),id});
                    Intent intent = new Intent(context, MainActivity.class);//ovde
                    intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                    activity.startActivity(intent);
                }
        });
        holder.obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = ((Activity) context).getIntent();
                DatabaseHelper db = new DatabaseHelper(context);
                String id = temp.getStringExtra("id");
                db.deleteData(id,film_id.get(position));
                Intent intent = new Intent(context, SacuvanActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
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
        ImageButton sacuvaj,obrisi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            filmIme = itemView.findViewById(R.id.AIme);
            filmGodina = itemView.findViewById(R.id.AGodina);
            filmOpis = itemView.findViewById(R.id.AOpis);
            filmZanrovi = itemView.findViewById(R.id.AZanr);
            filmSlika = itemView.findViewById(R.id.slikaAFilma);
            sacuvaj = itemView.findViewById(R.id.buttonSacuvaj);
            obrisi = itemView.findViewById(R.id.buttonObrisiLike);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
