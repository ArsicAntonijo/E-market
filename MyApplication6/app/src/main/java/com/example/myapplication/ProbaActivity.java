package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


public class ProbaActivity extends AppCompatActivity {
    private Button insert, delete, update;
    DatabaseHelper databaseHelper;
    private EditText textIme, textPrezime, textKorIme, textLozinka, textID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle("Admin");
        setContentView(R.layout.activity_proba);

        databaseHelper = new DatabaseHelper(this);
        insert = findViewById(R.id.buttonUserNovi);
        delete = findViewById(R.id.buttonUserObrisi);
        update = findViewById(R.id.buttonUserIzmenjeni);
        textIme = findViewById(R.id.editUserIme);
        textPrezime = findViewById(R.id.editUserPrezime);
        textKorIme = findViewById(R.id.editUserKorisnickoIme);
        textLozinka = findViewById(R.id.editUserLozinka);
        textID = findViewById(R.id.editUserID);

        insert.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItCorrect()){
                    boolean oo = databaseHelper.insertNewWorker(textIme.getText().toString(), textPrezime.getText().toString(),
                            textKorIme.getText().toString(), textLozinka.getText().toString());
                    if(oo) {
                        toastMessage("Uspesno ubačen!");
                        clear2();
                        databaseHelper.displayData3();
                    } else {
                        toastMessage("Doslo je do greske!");
                    }
                }
            }
        });

        update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isItCorrect() && !textID.getText().toString().equals("")){
                    Cursor data = databaseHelper.containsWorker(Integer.parseInt(textID.getText().toString()));
                    if(data.getCount() > 0) {
                        databaseHelper.updateWorkerData(Integer.parseInt(textID.getText().toString()),
                                textIme.getText().toString(), textPrezime.getText().toString(),
                                textKorIme.getText().toString(), textLozinka.getText().toString());
                        toastMessage("Uspesno ubačen!");
                        clear2();
                        databaseHelper.displayData3();
                    } else {
                        toastMessage("Nema radnik sa navedenim ID!");
                    }
                }
            }
        });

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textID.getText().toString().equals("")){
                    Cursor data = databaseHelper.containsWorker(Integer.parseInt(textID.getText().toString()));
                    if(data.getCount() > 0) {
                        boolean oo = databaseHelper.deleteWorker(textID.getText().toString());
                        if (oo) {
                            toastMessage("Uspesno obrisan!");
                            clear2();
                            databaseHelper.displayData3();
                        } else {
                            toastMessage("Doslo je do greske!");
                        }
                    } else {
                        toastMessage("Nema radnik sa tim ID!");
                    }
                }
            }
        });

        textID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    //toastMessage("jee");
                    Cursor data = databaseHelper.containsWorker(Integer.parseInt(textID.getText().toString()));
                    if(data.getCount() > 0) {
                        toastMessage("Pronadjen radnik!");
                        while(data.moveToNext()){
                            textIme.setText(data.getString(1));
                            textPrezime.setText(data.getString(2));
                            textKorIme.setText(data.getString(3));
                            textLozinka.setText(data.getString(4));
                        }
                    } else {
                        toastMessage("Nema radnika sa tim ID!");
                        clear();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void toastMessage(String mess) {
        Toast.makeText(ProbaActivity.this, mess, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guest_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                Intent intent4 = new Intent(ProbaActivity.this, MainActivity.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean isItCorrect() {
        if(textIme.getText().toString().equals("")) {
            textIme.setError("Polje mora biti popunjeno");
            return false;
        }
        if(textPrezime.getText().toString().equals("")) {
            textPrezime.setError("Polje mora biti popunjeno");
            return false;
        }
        if(textKorIme.getText().toString().equals("")) {
            textKorIme.setError("Polje mora biti popunjeno");
            return false;
        }
        if(textLozinka.getText().toString().equals("")) {
            textLozinka.setError("Polje mora biti popunjeno");
            return false;
        }
        return true;
    }
    private void clear() {
        textIme.setText("");
        textPrezime.setText("");
        textKorIme.setText("");
        textLozinka.setText("");
    }

    private void clear2() {
        textID.setText("");
        textIme.setText("");
        textPrezime.setText("");
        textKorIme.setText("");
        textLozinka.setText("");
    }
}