package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    EditText ime,godina,opis,zanr;
    Button azuriraj,izmeniSliku,izbrisi;
    private byte[] img;
    String imeF,godF,opisF,zanrF,idF;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        ime = findViewById(R.id.editTextAIme);
        godina = findViewById(R.id.editTextAGodina);
        zanr = findViewById(R.id.editTextAZanr);
        opis = findViewById(R.id.editTextAOpis);
        azuriraj = findViewById(R.id.buttonAzuriraj);
        izbrisi = findViewById(R.id.buttonIzbrisi);
        izmeniSliku = findViewById(R.id.buttonASlika);
        imageView = findViewById(R.id.ASlika);

        getAndSetIntentData();
        izmeniSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        azuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imeF = ime.getText().toString().trim();
                godF = godina.getText().toString().trim();
                opisF = opis.getText().toString().trim();
                zanrF = zanr.getText().toString().trim();
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);

                db.updateData(idF,imeF,godF,opisF,zanrF,img);

            }
        });
        izbrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData() {
        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();

        if (getIntent().hasExtra("id") &&
                (getIntent().hasExtra("ime")) &&
                (getIntent().hasExtra("godina")) &&
                (getIntent().hasExtra("opis")) &&
                (getIntent().hasExtra("zanr")) &&
                (extras.hasExtra("slike"))){

            idF =getIntent().getStringExtra("id");
            imeF = getIntent().getStringExtra("ime");
            godF = getIntent().getStringExtra("godina");
            opisF = getIntent().getStringExtra("opis");
            zanrF = getIntent().getStringExtra("zanr");
            img =(byte[]) extras.getExtra("slike");
            ime.setText(imeF);
            godina.setText(godF);
            opis.setText(opisF);
            zanr.setText(zanrF);
            //Toast.makeText(this, idF+imeF+godF+opisF+zanrF, Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(DbBitmapUtility.getImage(img));
        } else {
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show();
        }
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
                        imageView.setImageBitmap(
                                selectedImageBitmap);

                        img = DbBitmapUtility.getBytes(selectedImageBitmap);
                    }
                }
            });

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Obrisati " + imeF);
        builder.setMessage("Da li zelite da obrisete " + imeF + " ?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(UpdateActivity.this);

                db.deleteRow(idF);

                db.deleteData("USER_FILM","F_ID",idF);

                finish();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();
    }

}