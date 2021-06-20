package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
    ArrayList nazivi;
    ArrayList kolicine;
    ArrayList cene;
    ArrayList firme;
    ArrayList ids;
    boolean skip, racun;
    Context context;
    Activity activity;

    public CustomAdapter(Activity activity, Context context, ArrayList ids, ArrayList nazivi,
                         ArrayList firme,ArrayList kolicine, ArrayList cene, boolean skip, boolean racun) {
        this.context = context;
        this.nazivi = nazivi;
        this.kolicine = kolicine;
        this.ids = ids;
        this.cene = cene;
        this.firme = firme;
        this.skip = skip;
        this.activity = activity;
        this.racun = racun;
    }

    @NonNull
    @Override
    public CustomAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_data, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyHolder holder, final int position) {
        holder.naziv.setText(String.valueOf(nazivi.get(position)));
        holder.kolicina.setText(String.valueOf(kolicine.get(position)));
        holder.firma.setText(String.valueOf(firme.get(position)));
        holder.cena.setText(String.valueOf(cene.get(position)) + " RSD");

        if(skip) {
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(ids.get(position)));
                    intent.putExtra("naziv", String.valueOf(nazivi.get(position)));
                    intent.putExtra("firma", String.valueOf(firme.get(position)));
                    intent.putExtra("kolicina", String.valueOf(kolicine.get(position)));
                    intent.putExtra("cena", String.valueOf(cene.get(position)));
                    if (racun)
                        intent.putExtra("racun", true);
                    else
                        intent.putExtra("racun", false);
                    activity.startActivityForResult(intent, 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nazivi.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView naziv, kolicina, cena, firma;
        LinearLayout mainLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            naziv = itemView.findViewById(R.id.textNaziv);
            kolicina = itemView.findViewById(R.id.textKolicina);
            cena = itemView.findViewById(R.id.textCena);
            firma = itemView.findViewById(R.id.textFirma);
            mainLayout = itemView.findViewById(R.id.mainLayaout);
        }
    }
}
