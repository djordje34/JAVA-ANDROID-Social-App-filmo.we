package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IzmeniKorisnikaActivity extends AppCompatActivity {
    EditText ime,pw;
    String un,psw,id;
    Button promeni,obrisi;
    Intent temp;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmeni_korisnika);
        temp = getIntent();
        db = new DatabaseHelper(IzmeniKorisnikaActivity.this);
        ime = findViewById(R.id.editTextKUsername);
        pw = findViewById(R.id.editTextKPassword);
        promeni = findViewById(R.id.buttonPromeniKorisnikProfil);
        obrisi = findViewById(R.id.buttonObrisiKorisnika);
        id = temp.getStringExtra("id");
        String[] vals = getData();
        ime.setText(vals[0]);
        pw.setText(vals[1]);


        promeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nime = ime.getText().toString();
                String npw = pw.getText().toString();

                db.updateUser(id,nime,npw);
                Intent temp = getIntent();
                String id = temp.getStringExtra("id");
                Intent intent = new Intent(IzmeniKorisnikaActivity.this, IzmeniKorisnikaActivity.class);//ovde
                intent.putExtra("id", String.valueOf(temp.getStringExtra("id")));
                startActivity(intent);
            }
        });

        obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteUser(id);
                Toast.makeText(IzmeniKorisnikaActivity.this, "Korisnik uspešno obrisan.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(IzmeniKorisnikaActivity.this, AdminUserActivity.class);//ovde
                startActivity(intent);
            }
        });
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
                Intent i = new Intent(IzmeniKorisnikaActivity.this, AdminActivity.class);
                startActivity(i);
                break;
            case R.id.admin_users:
                Intent intnt = new Intent(IzmeniKorisnikaActivity.this, AdminUserActivity.class);
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


    public String[] getData(){
        String tu="",tp="";
        String nId = temp.getStringExtra("id");
        String query = "ID = "+nId;
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
        return new String[]{tu,tp};
    }

}