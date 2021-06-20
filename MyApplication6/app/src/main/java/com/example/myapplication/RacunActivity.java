package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class RacunActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle("Izdavanje računa");
        setContentView(R.layout.activity_racun);

        recyclerView = findViewById(R.id.recyclerView);
        databaseHelper = new DatabaseHelper(this);
        FloatingActionButton scan = findViewById(R.id.floatingActionButton);
        FloatingActionButton input = findViewById(R.id.floatingKeyboard);
        FloatingActionButton exp = findViewById(R.id.floatingExport);

        ucitajPodatke();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(RacunActivity.this, "xoxo", Toast.LENGTH_SHORT).show();
                IntentIntegrator scanIntegrator = new IntentIntegrator(RacunActivity.this);
                scanIntegrator.initiateScan();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(RacunActivity.this);
                builder.setTitle("Ucitavanje koda: ");

                //unos polje
                final EditText input = new EditText(RacunActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // potvrdno dugme
                builder.setPositiveButton("Uredu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //toastMessage(input.getText().toString());
                        String id = "";
                        String naziv = "";
                        String firma = "";
                        String cena = "";

                        Cursor data = databaseHelper.containsData(Long.parseLong(input.getText().toString()));

                        if (data.getCount() > 0) {
                            while (data.moveToNext()) {
                                id = data.getString(0);
                                naziv = data.getString(1);
                                firma = data.getString(2);
                                cena = data.getString(4);
                            }

                            boolean oo = databaseHelper.insertDataInRecipt(Long.parseLong(id), naziv, firma, 1, Integer.parseInt(cena));
                            if (oo) {
                                toastMessage("Uspesno");
                                databaseHelper.displayData2();
                                ucitajPodatke();
                            } else
                                toastMessage("Greska");
                        } else {
                            toastMessage("Nema tog proizvoda na raspolaganju!");
                        }
                    }
                });

                // negativno dugme
                builder.setNegativeButton("Povratak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  toastMessage("izdavanje racuna!");
                // ovde se ubaci logika izdavanje racuna i update u bazi podataka u odnos kolko je proizvoda kupljeno
                AlertDialog.Builder builder  = new AlertDialog.Builder(RacunActivity.this);
                builder.setTitle("Izdavanje računa: ");


                //unos polje
                final EditText input = new EditText(RacunActivity.this);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);

                Cursor data = databaseHelper.displayRacun();
                str = "";
                int suma = 0;
                int razmak = 0;

                while(data.moveToNext()) {
                    str += data.getString(1) + " " + data.getString(2) + " ";
                    razmak = data.getString(1).length() + data.getString(2).length() + 1;
                    int n = 40 - razmak;
                    for(int i = 0; i < n; i++) {
                        str += ".";
                    }

                    int kol = Integer.parseInt(data.getString(3));
                    int cena = Integer.parseInt(data.getString(4));
                    int ukupno = kol * cena;
                    suma += ukupno;
                    str += " " + kol + "x" + cena + "\n";
                    for(int i = 0; i < 50; i++) {
                        str += ".";
                    }
                    str += "  " + ukupno + " RSD\n";
                }

                str += "Ukupno za platiti: " + suma + "RSD!";
                builder.setMessage("Ukupno za platiti:  " + suma + "RSD!\n\n\n Unesi mail korisnika:");

                // potvrdno dugme
                builder.setPositiveButton("Pošalji", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //toastMessage(input.getText().toString());

                        Cursor data = databaseHelper.displayRacun();

                        while(data.moveToNext()) {
                            int kol = Integer.parseInt(data.getString(3));

                            // uzimanje podataka iz baze i promene kolicine nakon prodaje
                            String id = "";
                            String naziv = "";
                            String firma = "";
                            String cena2 = "";
                            int staraKolicina = 10;

                            Cursor data2 = databaseHelper.containsData(Long.parseLong(data.getString(0)));

                            if (data2.getCount() > 0) {
                                while (data2.moveToNext()) {
                                    id = data2.getString(0);
                                    naziv = data2.getString(1);
                                    firma = data2.getString(2);
                                    staraKolicina = Integer.parseInt(data2.getString(3));
                                    cena2 = data2.getString(4);
                                }

                                int kolicinaZaPromenu = staraKolicina - kol;
                                if(kolicinaZaPromenu < 0) kolicinaZaPromenu = 0;

                                databaseHelper.updateData(Long.parseLong(id), naziv, kolicinaZaPromenu, firma, Integer.parseInt(cena2));
                            }
                        }

                        sendEmail(input.getText().toString(), str);
                    }
                });

                // negativno dugme
                builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void ucitajPodatke() {
        // databaseHelper.displayData();

       //Cursor data = databaseHelper.getData();
         Cursor data = databaseHelper.displayRacun();
        ArrayList<String> nazivi = new ArrayList<>();
        ArrayList<String> kolicine = new ArrayList<>();
        ArrayList<String> cene = new ArrayList<>();
        ArrayList<String> firme = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        while(data.moveToNext()) {
            nazivi.add(data.getString(1));
            kolicine.add(data.getString(3));
            firme.add(data.getString(2));
            cene.add(data.getString(4));
            ids.add(data.getString(0));
        }
        CustomAdapter adapter = new CustomAdapter(
                RacunActivity.this,
                this,ids, nazivi, firme,
                kolicine, cene,
                true, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null) {
            if (scanningResult.getContents() != null) {
                String scanContent = scanningResult.getContents();
                String id = "";
                String naziv = "";
                String firma = "";
                String cena = "";

                Cursor data = databaseHelper.containsData(Long.parseLong(scanContent));

                if (data.getCount() > 0) {
                    while (data.moveToNext()) {
                        id = data.getString(0);
                        naziv = data.getString(1);
                        firma = data.getString(2);
                        cena = data.getString(4);
                    }

                    boolean oo = databaseHelper.insertDataInRecipt(Long.parseLong(id), naziv, firma, 1, Integer.parseInt(cena));
                    if (oo) {
                        toastMessage("Uspesno");
                        databaseHelper.displayData2();
                        ucitajPodatke();
                    } else
                        toastMessage("Greska");
                } else {
                    toastMessage("Loso skenirano, probaj opet!");
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Nema skeniranih podataka!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void toastMessage(String mess) {
        Toast.makeText(RacunActivity.this, mess, Toast.LENGTH_SHORT).show();
    }

    private void obrisiRacun() {
        boolean oo = databaseHelper.deleteAllFromRacun();
        if(oo) {
            ucitajPodatke();
        } else {
            toastMessage("Neuspesno obrisano");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:

                return true;
            case R.id.item2:
                obrisiRacun();
                Intent intent2 = new Intent(RacunActivity.this, PretragaMagacinaActivity.class);
                startActivity(intent2);
                return true;
            case R.id.item3:
                obrisiRacun();
                Intent intent3 = new Intent(RacunActivity.this, MagacinActivity.class);
                startActivity(intent3);
                return true;
            case R.id.Exit:
                obrisiRacun();
                Intent intent4 = new Intent(RacunActivity.this, MainActivity.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendEmail(String to, String text) {
        Log.i("Send email", "");

        String[] TO = {to};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Račun");
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            startActivity(Intent.createChooser(emailIntent, "Slanje ..."));
            finish();
            Log.i("MyTag", "Finished sending email...");
            obrisiRacun();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RacunActivity.this,
                    "Nemate podrska za slanje", Toast.LENGTH_SHORT).show();
        }
    }
}