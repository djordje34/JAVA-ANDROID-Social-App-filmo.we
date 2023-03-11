package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button login,buttonRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editTextLUsername);
        password = findViewById(R.id.editTextLPassword);
        login = findViewById(R.id.buttonLogin);
        buttonRedirect = findViewById(R.id.buttonRedirectRegister);
        buttonRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(LoginActivity.this);
                String un = username.getText().toString();
                String pw = password.getText().toString();
                if (!un.equals("admin")) {
                    boolean passable = db.verifyUser(un, pw);
                    if (passable) {
                        Toast.makeText(LoginActivity.this, "Uspesno ste se ulogovali.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        Integer id = db.getIDOfUser(un);
                        intent.putExtra("id", String.valueOf(id));
                        startActivity(intent);
                    }
                }
                else{
                    if(pw.equals("admin")){
                        Toast.makeText(LoginActivity.this, "Uspesno ste se ulogovali kao administrator.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }
                }
            }
            });
    }
}