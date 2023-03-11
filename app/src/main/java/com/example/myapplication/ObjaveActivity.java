package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ObjaveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView rv;
    DatabaseHelper db;
    PrikazAdapter prikazAdapter;
    ArrayList<String> film_id, ime, godina, opis, zanr,ime_k,id_k;
    ArrayList<byte[]> slike;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objave);


        rv = findViewById(R.id.rv_objave);
        db = new DatabaseHelper(ObjaveActivity.this);
        film_id = new ArrayList<>();
        ime = new ArrayList<>();
        godina = new ArrayList<>();
        opis = new ArrayList<>();
        zanr = new ArrayList<>();
        ime_k = new ArrayList<>();
        id_k = new ArrayList<>();
        slike = new ArrayList<>();
        getVals();
        ObjaveAdapter objaveAdapter = new ObjaveAdapter(this, film_id, ime, zanr, godina, opis,ime_k,id_k,slike, ObjaveActivity.this);
        rv.setAdapter(objaveAdapter);
        rv.setLayoutManager(new LinearLayoutManager(ObjaveActivity.this));
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

        query = "SELECT f.ID,f.IME,f.GODINA,f.OPIS,f.ZANR,u.ID,u.USERNAME,f.SLIKA" +
                " FROM Filmovi f, User u, USER_FILM uf, Korisnik_Prati kp WHERE kp.U_ID1='"+id+"' AND u.ID=kp.U_ID2 AND " +
                "uf.U_ID=kp.U_ID2 AND uf.F_ID=f.ID ";

        Cursor cursor = db.execRawQuery(query);
        if (cursor.getCount() == 0) {
            Toast.makeText(ObjaveActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                film_id.add(cursor.getString(0));
                ime.add(cursor.getString(1));
                godina.add(cursor.getString(2));
                opis.add(cursor.getString(3));
                zanr.add(cursor.getString(4));
                id_k.add(cursor.getString(5));
                ime_k.add(cursor.getString(6));
                slike.add(cursor.getBlob(7));
            }
        }

    }
}