package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MagacinActivity extends AppCompatActivity {
   private Button add, update;
   private EditText editNaziv, editKolicina, editCena, editID, editFirma;
   DatabaseHelper databaseHelper;
   private boolean found = false;
   private boolean aktivan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle("Unos proizvoda");
        setContentView(R.layout.activity_magacin);

        add = findViewById(R.id.buttonUbaciNovi);
        update = findViewById(R.id.buttonUbaciIzmenjeni);
        editID = findViewById(R.id.editID);
        editNaziv = findViewById(R.id.editUbaciNaziv);
        editFirma = findViewById(R.id.editUbaciImeFirme);
        editKolicina = findViewById(R.id.editUbaciKolicinu);
        editCena = findViewById(R.id.editUbaciCena);
        databaseHelper = new DatabaseHelper(this);
        CheckBox checkBox = findViewById(R.id.checkBox);

        editID.setEnabled(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // toastMessage(contentTxt.getText().toString());
                if(!editKolicina.getText().toString().equals("") && !editNaziv.getText().toString().equals("")
                        && !editCena.getText().toString().equals("") && !editFirma.getText().toString().equals("")) {
                    boolean oo = databaseHelper.insertData(Long.parseLong(editID.getText().toString()),
                            editNaziv.getText().toString(), Integer.parseInt(editKolicina.getText().toString()),
                            editFirma.getText().toString(), Integer.parseInt(editCena.getText().toString()));
                    if (oo) {
                        toastMessage("Uspesno");
                        clear();
                    }
                    else
                        toastMessage("Greska");
                } else
                    toastMessage("Polja moraju biti popunjena");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editID.getText().toString().length() > 0) {
                    Cursor data = databaseHelper.containsData(Long.parseLong(editID.getText().toString()));
                    if (data.getCount() > 0) {
                        if (!editKolicina.getText().toString().equals("") && !editNaziv.getText().toString().equals("")
                                && !editCena.getText().toString().equals("") && !editFirma.getText().toString().equals("")) {
                            databaseHelper.updateData(Long.parseLong(editID.getText().toString()), editNaziv.getText().toString(),
                                    Integer.parseInt(editKolicina.getText().toString()), editFirma.getText().toString(),
                                    Integer.parseInt(editCena.getText().toString()));
                            clear();
                        } else
                            toastMessage("Polja moraju biti popunjena");
                    } else
                        toastMessage("Nema proizvod u magacinu sa navedenim ID!");
                }
                else
                    toastMessage("Morate uneti neku vrednost za ID.");
            }
        });

        editID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("-1") && s.length() > 3){
                    Cursor data = databaseHelper.containsData(Long.parseLong(editID.getText().toString()));
                    if (data.getCount() > 0) {
                        toastMessage("Prozivod je pronadjen u magacinu");

                        while(data.moveToNext()) {
                            editNaziv.setText(data.getString(1));
                            editFirma.setText(data.getString(2));
                            editKolicina.setText(data.getString(3));
                            editCena.setText(data.getString(4));
                        }
                    } else {
                        toastMessage("Skenirani proizvod se ne nalazi u magacinu!");
                        clear2();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editID.setEnabled(true);
                    editID.setText("");
                } else {
                    editID.setEnabled(false);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            editID.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Podaci skeniranja nisu dobijena!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void toastMessage(String mess) {
        Toast.makeText(MagacinActivity.this, mess, Toast.LENGTH_SHORT).show();
    }

    public void clear() {
        editID.setText("");
        editNaziv.setText("");
        editKolicina.setText("");
        editCena.setText("");
        editFirma.setText("");
    }
    public void clear2() {
        editNaziv.setText("");
        editKolicina.setText("");
        editCena.setText("");
        editFirma.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.magacin_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                clear();
                Intent intent = new Intent(MagacinActivity.this, RacunActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                clear();
                Intent intent2 = new Intent(MagacinActivity.this, PretragaMagacinaActivity.class);
                startActivity(intent2);
                return true;
            case R.id.item3:

                return true;
            case R.id.Exit:
                clear();
                Intent intent4 = new Intent(MagacinActivity.this, MainActivity.class);
                startActivity(intent4);
                return true;
            case R.id.scan:
                IntentIntegrator scanIntegrator = new IntentIntegrator(MagacinActivity.this);
                scanIntegrator.initiateScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}