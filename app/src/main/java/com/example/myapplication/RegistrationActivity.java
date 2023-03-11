package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {
    private EditText username,password,repeatPassword;
    private Button buttonRegister,buttonRedirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        repeatPassword = findViewById(R.id.editTextRepeatPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRedirect = findViewById(R.id.buttonRedirectToLogin);
        buttonRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DatabaseHelper db = new DatabaseHelper(RegistrationActivity.this);
                String un = username.getText().toString();
                String pw = password.getText().toString();
                String pwRepeat= repeatPassword.getText().toString();
                String[] vals = new String[]{un,pw};
                if(pw.equals(pwRepeat) && un.length()>=1 && pw.length()>=1) {

                    boolean upisan = db.insertData(db.USER, vals);
                     if (upisan == true) {
                        Toast.makeText(RegistrationActivity.this, "Uspesno ste se registrovali.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Greska u registraciji.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "Lozinke se ne poklapaju.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}