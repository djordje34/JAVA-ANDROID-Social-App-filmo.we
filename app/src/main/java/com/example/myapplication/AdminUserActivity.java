package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AdminUserActivity extends AppCompatActivity{
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
    protected void onCreate(Bundle savedInstanceState) {    //dodati prikaz korisnika iz adaptera KorisnikAdapter gde je
        // which == 3->dodaj da postoji which=3->sve zakljucano,
        // na klik layout->edit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        rv = findViewById(R.id.rv_pratioci);
        pretraga = findViewById(R.id.buttonPretrazi);
        sta = findViewById(R.id.editTextPretraga);
        db = new DatabaseHelper(AdminUserActivity.this);
        id = new ArrayList<>();
        un = new ArrayList<>();
        br_f = new ArrayList<>();

        getVals();
        korisnikAdapter = new KorisnikAdapter(this, id, un, br_f, AdminUserActivity.this,4);
        rv.setAdapter(korisnikAdapter);
        rv.setLayoutManager(new LinearLayoutManager(AdminUserActivity.this));
        drawerLayout = findViewById(R.id.my_drawer_layout);

        pretraga.setOnClickListener(new View.OnClickListener() {
            Intent i;
            @Override
            public void onClick(View view) {
                Intent temp = getIntent();
                i = new Intent(AdminUserActivity.this, AdminUserActivity.class);//.getText().toString();
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
            query = "( USERNAME LIKE '%" + pretraga + "%')";
        } else {
            query = "";
        }

        Cursor cursor = db.getData("User", query);
        if (cursor.getCount() == 0) {
            Toast.makeText(AdminUserActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                un.add(cursor.getString(1));
                String tQ = "U_ID = " + cursor.getString(0);
                Cursor c = db.getData("USER_FILM", tQ);
                br_f.add(Integer.toString(c.getCount()) + " favorita"); //HANDLE LAYOUT ONCLICK CRUD KORISNIK I DUGME ZA DODAVANJE NA OVOJ STRANICI
                c.close();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.bottom_navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admin_movies:
                Intent i = new Intent(AdminUserActivity.this, AdminActivity.class);
                startActivity(i);
                break;
            case R.id.admin_users:
                Intent intnt = new Intent(AdminUserActivity.this, AdminUserActivity.class);
                startActivity(intnt);
                break;

            case R.id.admin_logout:
                i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}