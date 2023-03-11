package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AdminActivity extends AppCompatActivity {
    private RecyclerView rv;
    private FloatingActionButton btnAdd;
    DatabaseHelper db;
    CustomAdapter customAdapter;
    ArrayList<String> film_id,ime,godina,opis,zanr;
    ArrayList<byte[]> slike;
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        rv = findViewById(R.id.rv_baza);
        btnAdd = findViewById(R.id.buttonDodajFilm);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddActivity.class);
                startActivity(intent);

            }
        });

        db = new DatabaseHelper(AdminActivity.this);
        film_id = new ArrayList<>();
        ime = new ArrayList<>();
        godina = new ArrayList<>();
        opis = new ArrayList<>();
        zanr = new ArrayList<>();
        slike = new ArrayList<>();
        getVals();
        customAdapter = new CustomAdapter(this, film_id, ime, zanr, godina, opis,slike, AdminActivity.this);
        rv.setAdapter(customAdapter);
        rv.setLayoutManager(new LinearLayoutManager(AdminActivity.this));

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
                Intent i = new Intent(AdminActivity.this, AdminActivity.class);
                startActivity(i);
                break;
            case R.id.admin_users:
                Intent intnt = new Intent(AdminActivity.this, AdminUserActivity.class);
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

        void getVals(){
            Cursor cursor = db.getAllData("Filmovi");
            if (cursor.getCount() == 0) {
                Toast.makeText(AdminActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
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