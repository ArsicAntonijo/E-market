package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class GuestActivity extends AppCompatActivity {
    private Button pretraga;
    private FloatingActionButton scan;
    private EditText text;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    boolean skip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().setTitle("Pretraga proizvoda");
        setContentView(R.layout.activity_guest);

        pretraga = findViewById(R.id.buttonPretraziPojam);
        scan = findViewById(R.id.floatingScenirajPojam);
        text = findViewById(R.id.editPojamPretrage);
        recyclerView = findViewById(R.id.prikazPretrageView);
        databaseHelper = new DatabaseHelper(this);

        ucitajPodatke();

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ucitajPodatke();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(GuestActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            if (scanningResult.getContents() != null) {
                String scanContent = scanningResult.getContents();

                text.setText(scanContent);

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Nema skiniranih podataka!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void toastMessage(String mess) {
        Toast.makeText(GuestActivity.this, mess, Toast.LENGTH_SHORT).show();
    }

    private void ucitajPodatke() {
        // databaseHelper.displayData();

        Cursor data = databaseHelper.getSearchData(text.getText().toString());
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
        CustomAdapter adapter = new CustomAdapter(GuestActivity.this, GuestActivity.this,ids, nazivi,
                firme, kolicine, cene, false, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guest_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                Intent intent4 = new Intent(GuestActivity.this, MainActivity.class);
                startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}