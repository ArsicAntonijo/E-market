package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText editNaziv, editFirma, editKolicina, editCena;
    DatabaseHelper databaseHelper;
    String id;
    boolean racun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle("Ažuriranje promena");
        setContentView(R.layout.activity_update);

        editCena = findViewById(R.id.editUpdateCena);
        editFirma = findViewById(R.id.editUpdateFirma);
        editKolicina = findViewById(R.id.editUpdateKolicina);
        editNaziv = findViewById(R.id.editUpdateNaziv);
        databaseHelper = new DatabaseHelper(this);
        Button update = findViewById(R.id.buttonUpdateUpdate);
        Button delete = findViewById(R.id.buttonUpdateDelete);

        setujPolja();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editKolicina.getText().toString().equals("") && !editNaziv.getText().toString().equals("")
                        && !editCena.getText().toString().equals("") && !editFirma.getText().toString().equals("")) {
                    if(!racun) {
                        databaseHelper.updateData(Long.parseLong(id), editNaziv.getText().toString(),
                                Integer.parseInt(editKolicina.getText().toString()), editFirma.getText().toString(),
                                Integer.parseInt(editCena.getText().toString()));
                        Intent intentnt = new Intent(UpdateActivity.this, PretragaMagacinaActivity.class);
                        startActivity(intentnt);
                    } else {
                        databaseHelper.updateDataFromRacun(Long.parseLong(id), Integer.parseInt(editKolicina.getText().toString()));
                        Intent intentnt = new Intent(UpdateActivity.this, RacunActivity.class);
                        startActivity(intentnt);
                    }
                } else
                    toastMessage("Polja moraju biti popunjena");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toastMessage("Obrisano");
                if(!racun) {
                    boolean oo = databaseHelper.deleteItem(id);
                    if (oo) {
                        Intent intentnt = new Intent(UpdateActivity.this, PretragaMagacinaActivity.class);
                        startActivity(intentnt);
                    } else
                        toastMessage("Greska");
                } else  {
                    boolean oo = databaseHelper.deleteItemFromRacun(id);
                    if (oo) {
                        Intent intentnt = new Intent(UpdateActivity.this, RacunActivity.class);
                        startActivity(intentnt);
                    } else
                        toastMessage("Greska");
                }
            }
        });
    }

    private void toastMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void setujPolja() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String naziv = intent.getStringExtra("naziv");
        String firma = intent.getStringExtra("firma");
        String kolicina = intent.getStringExtra("kolicina");
        String cena = intent.getStringExtra("cena");
        racun = intent.getBooleanExtra("racun",false);

        editNaziv.setText(naziv);
        editFirma.setText(firma);
        editKolicina.setText(kolicina);
        editCena.setText(cena);

        if(racun) {
            editNaziv.setEnabled(false);
            editFirma.setEnabled(false);
            editCena.setEnabled(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guest_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                if(racun) {
                    Intent intent4 = new Intent(UpdateActivity.this, RacunActivity.class);
                    startActivity(intent4);
                } else {
                    Intent intent4 = new Intent(UpdateActivity.this, PretragaMagacinaActivity.class);
                    startActivity(intent4);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}