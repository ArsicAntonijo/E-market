package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "baza";
    // tabela prozivoda
    private final static String TABLE_NAME = "table_market";
    private final static String COL1 = "ID";
    private final static String COL2 = "Naziv_proizvoda";
    private final static String COL3 = "Ime_firme";
    private final static String COL4 = "Kolocina_na_raspolaganju";
    private final static String COL5 = "Cena_proizvoda";

    // tabela racuna
    private final static String TABLE_NAME2 = "table_market2";
    private final static String COL1_2 = "ID";
    private final static String COL2_2 = "Naziv_proizvoda";
    private final static String COL3_2 = "Ime_fimre";
    private final static String COL4_2 = "Kolicina_kupljeno";
    private final static String COL5_2 = "Cena_proizvoda";

    // tabela radnika
    private final static String TABLE_NAME3 = "table_market3";
    private final static String COL1_3 = "ID";
    private final static String COL2_3 = "Ime";
    private final static String COL3_3 = "Prezime";
    private final static String COL4_3 = "Korisnicko_ime";
    private final static String COL5_3 = "Lozinka";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " ( " + COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " INTEGER, " + COL5 + " INTEGER)";
        String sql2 = "CREATE TABLE " + TABLE_NAME2 + " ( " + COL1_2 + " INTEGER PRIMARY KEY, " +
                COL2_2 + " TEXT, " + COL3_2 + " TEXT, " + COL4_2 + " INTEGER, " + COL5_2 + " INTEGER)";
        String sql3 = "CREATE TABLE " + TABLE_NAME3 + " ( " + COL1_3 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2_3 + " TEXT, " + COL3_3 + " TEXT, " + COL4_3 + " TEXT, " + COL5_3 + " TEXT)";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(db);
    }

    public boolean insertData(long id, String naziv, int kolicina, String firma, int cena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1, id);
        values.put(COL2, naziv);
        values.put(COL3, firma);
        values.put(COL4, kolicina);
        values.put(COL5, cena);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public void updateData(long id, String naziv, int kolicina, String firma, int cena) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + COL2 + " ='" + naziv + "', " + COL3 +
                " = '" + firma + "', " + COL4 + "='" + kolicina + "', " + COL5 + "='" + cena + "' WHERE " + COL1 + "='" + id + "'";
        db.execSQL(sql);
    }
    public Cursor containsData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + "='" + id + "'";
        Cursor data = db.rawQuery(sql, null);
        return data;
    }

    public void displayData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(sql, null);

        String str = "" + data.getCount() +" ";
        while(data.moveToNext()) {
            str += data.getString(0) + "->" + data.getString(1) + "   ";
        }
        Log.d("MyLog", str);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(sql, null);

        return data;
    }

    public Cursor getSearchData(String pojamPretrage) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " LIKE '%" + pojamPretrage +
                "%' OR " + COL2 + " LIKE '%" + pojamPretrage + "%' OR " + COL3 + " LIKE '%" + pojamPretrage + "%'";
        Cursor data = db.rawQuery(sql, null);
        return data;
    }



    public boolean deleteItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = db.delete(TABLE_NAME, "ID=?", new String[]{id});
        return result != -1;
    }

    // rad sa tabelom racuna --------------------------
    public boolean insertDataInRecipt(long id, String naziv, String firma, int kolicina, int cena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1_2, id);
        values.put(COL2_2, naziv);
        values.put(COL3_2, firma);
        values.put(COL4_2, kolicina);
        values.put(COL5_2, cena);

        long result = db.insert(TABLE_NAME2, null, values);
        return result != -1;
    }

    public void displayData2() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2;
        Cursor data = db.rawQuery(sql, null);

        String str = "" + data.getCount() +" ";
        while(data.moveToNext()) {
            str += data.getString(0) + "->" + data.getString(1) + "   ";
        }
        Log.d("MyLog", str);
    }

    public Cursor displayRacun() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2;
        return db.rawQuery(sql, null);
    }

    public boolean deleteItemFromRacun(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long result = db.delete(TABLE_NAME2, "ID=?", new String[]{id});
        return result != -1;
    }

    public void updateDataFromRacun(long id, int kolicina) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME2 + " SET "  + COL4_2 + "='" + kolicina + "' WHERE " + COL1 + "='" + id + "'";
        db.execSQL(sql);
    }

    public boolean deleteAllFromRacun() {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME2,
                null, null);
        return result != -1;
    }

    // rad sa bazom radnika
    public boolean insertNewWorker(String ime, String prezime, String kor, String loz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2_3, ime);
        values.put(COL3_3, prezime);
        values.put(COL4_3, kor);
        values.put(COL5_3, loz);

        long result = db.insert(TABLE_NAME3, null, values);
        return result != -1;
    }

    public void displayData3() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3;
        Cursor data = db.rawQuery(sql, null);

        String str = "" + data.getCount() +" ";
        while(data.moveToNext()) {
            str += data.getString(0) + "->" + data.getString(1) + "   ";
        }
        Log.d("MyLog", str);
    }

    public Cursor containsWorker(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + COL1_3 + " ='" + id + "'";
        return db.rawQuery(sql, null);
    }

    public boolean deleteWorker(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME3, "ID=?", new String[]{id});
        return result != -1;
    }

    public void updateWorkerData(int id, String ime, String prezime, String kor, String loz) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME3 + " SET "  + COL2_3 + "='" + ime + "', "  + COL3_3 + "='" + prezime + "', "
                + COL4_3 + " ='" + kor + "', " + COL5_3 + " ='" + loz + "'"
         + " WHERE " + COL1 + "='" + id + "'";
        db.execSQL(sql);
    }

    public Cursor getWorkerData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3;
        return db.rawQuery(sql, null);
    }
}
