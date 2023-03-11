package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView rv;
    DatabaseHelper db;
    PrikazAdapter prikazAdapter;
    ArrayList<String> film_id, ime, godina, opis, zanr;
    ArrayList<byte[]> slike;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    ImageButton pretraga;
    EditText sta;
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        rv = findViewById(R.id.rv_korisnik);
        pretraga = findViewById(R.id.buttonPretrazi);
        sta = findViewById(R.id.editTextPretraga);
        db = new DatabaseHelper(MainActivity.this);
        film_id = new ArrayList<>();
        ime = new ArrayList<>();
        godina = new ArrayList<>();
        opis = new ArrayList<>();
        zanr = new ArrayList<>();
        slike = new ArrayList<>();
        getVals();
        prikazAdapter = new PrikazAdapter(this, film_id, ime, zanr, godina, opis,slike, MainActivity.this,true);
        rv.setAdapter(prikazAdapter);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button


        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        pretraga.setOnClickListener(new View.OnClickListener() {

            Intent i;
            @Override
            public void onClick(View view) {
                Intent temp = getIntent();
                i = new Intent(MainActivity.this, MainActivity.class);//.getText().toString();
                i.putExtra("pretraga", String.valueOf(sta.getText().toString()));
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;
        Intent temp = getIntent();
        switch (item.getItemId()) {
            case R.id.nav_svi_filmovi:

                i = new Intent(this, MainActivity.class);
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
                return true;
            case R.id.nav_moji_filmovi:
                i = new Intent(this, SacuvanActivity.class);
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
                return true;
            case R.id.nav_moj_profil:
                i = new Intent(this, ProfilActivity.class);
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
                return true;
//nav_objave
            case R.id.nav_zaprati:
                i = new Intent(this, PratiActivity.class);
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
                return true;
            case R.id.nav_objave:
                i = new Intent(this, ObjaveActivity.class);
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
                return true;

            case R.id.nav_logout:
                i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;

            default:
                //return super.onOptionsItemSelected(item);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                return super.onOptionsItemSelected(item);
            }


        return super.onOptionsItemSelected(item);
    }


    void getVals(){
        Intent temp = getIntent();
        String query;
        String id = temp.getStringExtra("id");
        if(temp.hasExtra("pretraga")){
            String pretraga = temp.getStringExtra("pretraga");
            query = "ID NOT IN (SELECT F_ID FROM USER_FILM WHERE U_ID="+id+") AND ( IME LIKE '%"+pretraga+"%' " +
                    " OR GODINA = '"+pretraga+"' " +
                    " OR ZANR LIKE '%"+pretraga+"%' " +
                    " OR OPIS LIKE '%"+pretraga+"%' )";
        }
        else{
            query = "ID NOT IN (SELECT F_ID FROM USER_FILM WHERE U_ID="+id+")";
        }

        Cursor cursor = db.getData("Filmovi",query);
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                film_id.add(cursor.getString(0));
                ime.add(cursor.getString(1));
                godina.add(cursor.getString(2));
                opis.add(cursor.getString(3));
                zanr.add(cursor.getString(4));
                slike.add(cursor.getBlob(5));
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }






}