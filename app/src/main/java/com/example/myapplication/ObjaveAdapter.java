package com.example.myapplication;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObjaveAdapter extends RecyclerView.Adapter<ObjaveAdapter.MyViewHolder>{

    private boolean which;
    // kontekst
    Context context;
    ImageButton sacuvaj;
    // nizovi za naslove i autore
    ArrayList<String> film_id, ime, godina,opis,zanr,ime_k,id_k;
    ArrayList<byte[]> slike;
    Activity activity;

    // konstruktor klase CustomAdapter
    public ObjaveAdapter(Context context, ArrayList film_id, ArrayList ime,ArrayList zanr, ArrayList godina,ArrayList opis,ArrayList ime_k,ArrayList id_k,ArrayList slike, Activity activity){
        this.context = context;
        this.film_id = film_id;
        this.ime = ime;
        this.godina = godina;
        this.opis=opis;
        this.zanr = zanr;
        this.ime_k = ime_k;
        this.id_k=id_k;
        this.slike = slike;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ObjaveAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.prikaz_objave, parent, false);
        return new ObjaveAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjaveAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.filmIme.setText(String.valueOf(ime.get(position)));
        holder.filmGodina.setText(String.valueOf(godina.get(position)));
        holder.filmOpis.setText(String.valueOf(opis.get(position)));
        holder.filmZanrovi.setText(String.valueOf(zanr.get(position)));
        holder.korisnikIme.setText("Korisnik "+String.valueOf(ime_k.get(position))+" označio film");
        holder.filmSlika.setImageBitmap(DbBitmapUtility.getImage(slike.get(position)));

        Intent temp = ((Activity) context).getIntent();
        String id = temp.getStringExtra("id");

        DatabaseHelper db = new DatabaseHelper(context);
        boolean isLiked = db.checkIfUserLikedMovie(id,film_id.get(position));

        if(isLiked){
            holder.sacuvaj.setVisibility(View.GONE);
        }
        else{
            holder.obrisi.setVisibility(View.GONE);
        }
        holder.sacuvaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = ((Activity) context).getIntent();
                DatabaseHelper db = new DatabaseHelper(context);
                String id = temp.getStringExtra("id");
                db.insertData(DatabaseHelper.USER_FILM,new String[]{film_id.get(position),id});
                Intent intent = new Intent(context, ObjaveActivity.class);//ovde
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
                Intent intent = new Intent(context, ObjaveActivity.class);//ovde
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
        TextView filmIme, filmGodina,filmOpis,filmZanrovi,korisnikIme;
        ImageView filmSlika;
        LinearLayout mainLayout;
        ImageButton sacuvaj,obrisi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            filmIme = itemView.findViewById(R.id.tudjiIme);
            filmGodina = itemView.findViewById(R.id.tudjiGodina);
            filmOpis = itemView.findViewById(R.id.tudjiOpis);
            filmZanrovi = itemView.findViewById(R.id.tudjiZanr);
            filmSlika = itemView.findViewById(R.id.tudjiSlika);
            korisnikIme = itemView.findViewById(R.id.objava_korisnik);

            sacuvaj = itemView.findViewById(R.id.buttonSacuvajTudji);
            obrisi = itemView.findViewById(R.id.buttonObrisiLike);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
