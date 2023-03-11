package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class AddActivity extends AppCompatActivity {
    EditText imeF,godinaF,zanrF,opisF;
    Button dodaj,dodajSliku;
    private byte[] img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        imeF = findViewById(R.id.editTextDIme);
        godinaF = findViewById(R.id.editTextDGodina);
        zanrF = findViewById(R.id.editTextDZanrovi);
        opisF = findViewById(R.id.editTextDOpis);
        dodaj = findViewById(R.id.buttonDodajFilmNapokon);
        dodajSliku = findViewById(R.id.buttonDSlika);

        dodajSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(AddActivity.this);

                String ime = imeF.getText().toString().trim();
                String godina = godinaF.getText().toString().trim();
                String zanr = zanrF.getText().toString().trim();
                String opis = opisF.getText().toString().trim();
                db.insertFilm("Filmovi",new String[]{ime,godina,opis,zanr},img);
                Toast.makeText(AddActivity.this, "Uspesno ste dodali novi film.", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(AddActivity.this, AdminActivity.class);
                startActivity(i);
                break;
            case R.id.admin_users:
                Intent intnt = new Intent(AddActivity.this, AdminUserActivity.class);
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


    private void imageChooser()
    {
        String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
        Intent i = new Intent();
        i.setType("image/*").putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);;
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();

                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        ImageView imageView;
                        imageView = findViewById(R.id.DSlika);
                        if(DbBitmapUtility.getBytes(selectedImageBitmap).length<=800000) {
                            imageView.setImageBitmap(
                                    selectedImageBitmap);

                            img = DbBitmapUtility.getBytes(selectedImageBitmap);
                        }
                        else{

                        }
                    }
                }
            });


}