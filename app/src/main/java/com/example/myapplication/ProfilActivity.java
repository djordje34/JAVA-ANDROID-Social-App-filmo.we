package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ProfilActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    EditText ime,pw;
    Button promeni,br_fans,br_prat;
    DatabaseHelper db;
    Intent temp;
    String un,psw,id;
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
        setContentView(R.layout.activity_profil);
        temp = getIntent();
        db = new DatabaseHelper(ProfilActivity.this);
        ime = findViewById(R.id.editTextPUsername);
        pw = findViewById(R.id.editTextPPassword);
        promeni=findViewById(R.id.buttonPromeniProfil);
        br_fans = findViewById(R.id.buttonPrate);
        br_prat = findViewById(R.id.buttonPracenje);
        String[] vals = getData();
        ime.setText(vals[0]);
        pw.setText(vals[1]);
        br_fans.setText(vals[2]);
        br_prat.setText(vals[3]);

        br_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp = getIntent();
                String id = temp.getStringExtra("id");
                Intent intent = new Intent(ProfilActivity.this, PratiociActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));

                startActivity(intent);
            }
        });

        br_prat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent temp = getIntent();
                String id = temp.getStringExtra("id");
                Intent intent = new Intent(ProfilActivity.this, FollowersActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));

                startActivity(intent);
            }
        });

        promeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nime = ime.getText().toString();
                String npw = pw.getText().toString();

                db.updateUser(id,nime,npw);
                Intent temp = getIntent();
                String id = temp.getStringExtra("id");
                Intent intent = new Intent(ProfilActivity.this, ProfilActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(intent);
            }
        });


        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public String[] getData(){
        String tu="",tp="";
        id = temp.getStringExtra("id");
        String query = "ID = "+id;
        Cursor c =  db.getData("User",query);

        if (c.getCount() == 0) {
            //Toast.makeText(ProfilActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
            tu = "";
            tp = "";
            return new String[]{tu,tp};
        } else {
            if (c.moveToFirst()) {

                tu = c.getString(1);
                tp = c.getString(2);
            }
        }
        String n_flws = db.getNumOfFollowers(id);
        String n_fng = db.getNumOfFollowing(id);

        return new String[]{tu,tp,n_flws,n_fng};
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