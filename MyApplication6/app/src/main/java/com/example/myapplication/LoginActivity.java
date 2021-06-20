package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private Button prelazBtn, prelazBtn2;
    EditText user, pass;

    private EditText emailEditText;
    private EditText passEditText;

    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);
        TextView txt = findViewById(R.id.textView33);
        databaseHelper = new DatabaseHelper(this);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GuestActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkLogin(View arg0) {
        final String email = emailEditText.getText().toString();
        final String pass = passEditText.getText().toString();

        if(email.equals("admin") && pass.equals("admin")) {
            Intent intent = new Intent(LoginActivity.this, ProbaActivity.class);
            startActivity(intent);
        } else {
            Cursor data = databaseHelper.getWorkerData();
            while(data.moveToNext()){
                String tempUser = data.getString(3);
                String tempPass = data.getString(4);

                if(tempUser.equals(email)) {
                    if(tempPass.equals(pass)){
                        Intent intent = new Intent(LoginActivity.this, PretragaMagacinaActivity.class);
                        startActivity(intent);
                    } else {
                        passEditText.setError("Pogrešna sifra");
                    }
                } else {
                    emailEditText.setError("Pogrešan unos");
                }
            }
        }
    }
}