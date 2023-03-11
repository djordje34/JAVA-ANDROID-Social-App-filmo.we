package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KorisnikAdapter extends RecyclerView.Adapter<KorisnikAdapter.MyViewHolder>{

        public int which;
        // kontekst
        Context context;
        ImageButton zaprati;
        // nizovi za naslove i autore
        ArrayList<String> id, un,br_f;
        Activity activity;

// konstruktor klase CustomAdapter
public KorisnikAdapter(Context context, ArrayList id, ArrayList un,ArrayList br_f, Activity activity,int which) {
        this.context = context;
        this.id = id;
        this.un = un;
        this.br_f = br_f;
        this.activity = activity;
        this.which = which;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.korisnici, parent, false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.kUsername.setText(String.valueOf(un.get(position)));
        holder.kBrojF.setText(String.valueOf(br_f.get(position)));
        if(this.which==0) { //kad je samo pretraga korisnika
            holder.izbaci.setVisibility(View.GONE);
            //holder.prikazi.setVisibility(View.GONE);
        }
        else if(this.which==1){ //kada se gledaju korisnici koje prati korisnik
            holder.zaprati.setVisibility(View.GONE);
    }
    else if(this.which==2) {//kada se gledaju korisnici koji prate korisnika
            Intent temp = ((Activity) context).getIntent();
            String idK = temp.getStringExtra("id");
            String idP = id.get(position);
            DatabaseHelper db = new DatabaseHelper(context);
            boolean isIt = db.checkIfFollowing(idK, idP);
            if (isIt) {
                holder.zaprati.setVisibility(View.GONE);
            }
            else{
                holder.izbaci.setVisibility(View.GONE);
            }
        }
        else if(which==3){
            holder.izbaci.setVisibility(View.GONE);
        }
        else if(which == 4){
            holder.zaprati.setVisibility(View.GONE);
            holder.izbaci.setVisibility(View.GONE);
            holder.prikazi.setVisibility(View.GONE);
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idK = id.get(position);
                    Intent intent = new Intent(context, IzmeniKorisnikaActivity.class);
                    intent.putExtra("id", idK);
                    activity.startActivity(intent);
                }
            });
        }

        holder.izbaci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp = ((Activity) context).getIntent();
                DatabaseHelper db = new DatabaseHelper(context);
                String idK = temp.getStringExtra("id");
                db.deleteFollowing(idK,id.get(position));
                Toast.makeText(context, "Uspešno obrisano praćenje.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PratiociActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                activity.startActivity(intent);
            }
        });
        holder.zaprati.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent temp = ((Activity) context).getIntent();
        DatabaseHelper db = new DatabaseHelper(context);
        String idK = temp.getStringExtra("id");
        db.insertData(DatabaseHelper.KORISNIK_PRATI,new String[]{idK,id.get(position)});
    Toast.makeText(context, "Uspešno izvršen zahtev praćenja korisnika.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, PratiActivity.class);//ovde
        intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
        activity.startActivity(intent);
        }
        });

        holder.prikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp = ((Activity) context).getIntent();
                String idK = temp.getStringExtra("id");
                String idP = id.get(position);
                Intent intent = new Intent(context, PrijateljProfilActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                intent.putExtra("idP", String.valueOf(idP));
                activity.startActivity(intent);
            }
        });


        }

@Override
public int getItemCount() {
        return un.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView kUsername, kBrojF;
    LinearLayout mainLayout;
    ImageButton zaprati,izbaci,prikazi;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);


        kUsername = itemView.findViewById(R.id.imeKorisnika);
        kBrojF = itemView.findViewById(R.id.brojFilmova);
        zaprati = itemView.findViewById(R.id.buttonZaprati);
        izbaci = itemView.findViewById(R.id.buttonIzbaci);
        prikazi = itemView.findViewById(R.id.buttonPrikaziProfil);
        mainLayout = itemView.findViewById(R.id.mainLayout);
    }
}
}