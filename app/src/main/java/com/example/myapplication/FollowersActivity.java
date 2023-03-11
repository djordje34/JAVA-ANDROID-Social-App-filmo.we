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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView rv;
    DatabaseHelper db;
    KorisnikAdapter korisnikAdapter;
    ArrayList<String> id,un,br_f;
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
        setContentView(R.layout.activity_pratioci);


        rv = findViewById(R.id.rv_pratioci);
        pretraga = findViewById(R.id.buttonPretrazi);
        sta = findViewById(R.id.editTextPretraga);
        db = new DatabaseHelper(FollowersActivity.this);
        id = new ArrayList<>();
        un = new ArrayList<>();
        br_f = new ArrayList<>();

        getVals();
        korisnikAdapter = new KorisnikAdapter(this, id, un, br_f, FollowersActivity.this,2);
        rv.setAdapter(korisnikAdapter);
        rv.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

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
                i = new Intent(FollowersActivity.this, PratiociActivity.class);//.getText().toString();
                i.putExtra("pretraga", String.valueOf(sta.getText().toString()));
                i.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(i);
            }
        });
    }

    void getVals() {

        Intent temp = getIntent();
        String query;
        if (temp.hasExtra("pretraga")) {
            String pretraga = temp.getStringExtra("pretraga");
            query = "(ID!='" + temp.getStringExtra("id") + "' AND USERNAME LIKE '%" + pretraga + "%') AND ID IN (SELECT U_ID2 FROM Korisnik_Prati WHERE U_ID1 = '" + temp.getStringExtra("id") + "')";
        } else {
            query = " (ID!= " + temp.getStringExtra("id") + " AND ID IN (SELECT U_ID2 FROM Korisnik_Prati WHERE U_ID1 = " + temp.getStringExtra("id") + "))";
        }

        Cursor cursor = db.getData("User", query);
        if (cursor.getCount() == 0) {
            Toast.makeText(FollowersActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                un.add(cursor.getString(1));
                String tQ = "U_ID = " + cursor.getString(0);
                Cursor c = db.getData("USER_FILM", tQ);
                br_f.add(Integer.toString(c.getCount()) + " favorita");
                c.close();
            }
        }
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


}