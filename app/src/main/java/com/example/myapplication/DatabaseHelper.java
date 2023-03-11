package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String DATABASE_NAME = "Filmovi.db";
    public static final String FILMOVI = "Filmovi";
    public static final String F_ID = "ID";
    public static final String F_IME = "IME";
    public static final String F_GODINA = "GODINA";
    public static final String F_OPIS = "OPIS";
    public static final String F_ZANR = "ZANR";
    public static final String F_SLIKA = "SLIKA";

    public static final String USER = "User";
    public static final String U_ID = "ID";
    public static final String U_USERNAME = "USERNAME";
    public static final String U_PASSWORD = "PASSWORD";

    public static final String USER_FILM = "USER_FILM";
    public static final String UF_ID = "ID";
    public static final String UF_UID = "U_ID";
    public static final String UF_FID = "F_ID";
    public static final String UF_VREME = "VREME";  //prijatelji


    public static final String KORISNIK_PRATI = "Korisnik_Prati";
    public static final String KP_ID = "ID";
    public static final String KP_U1_ID = "U_ID1";
    public static final String KP_U2_ID = "U_ID2";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FILMOVI +
                "("+ F_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                F_IME+ " TEXT," +
                F_GODINA+" TEXT,"+
                F_OPIS+ " TEXT," +
                F_ZANR+ " TEXT," +
                F_SLIKA+ " MEDIUMBLOB"
                + ")");

        db.execSQL("create table " + USER +
                " ("+ U_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_USERNAME+" TEXT, "+
                U_PASSWORD+" TEXT"
                + ")");
        db.execSQL("create table " + USER_FILM +
                " ("+ UF_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                " "+ UF_FID+ " INTEGER , " +
                UF_UID+" INTEGER, "+
                " "+ UF_VREME+ " DATETIME , " +
                "FOREIGN KEY ("+UF_FID+") REFERENCES Filmovi("+F_ID+") ON DELETE CASCADE, "+
                "FOREIGN KEY ("+UF_UID+") REFERENCES User("+U_ID+") ON DELETE CASCADE"
                + ")");

        db.execSQL("create table " + KORISNIK_PRATI +
                " ("+ KP_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                " "+ KP_U1_ID+ " INTEGER , " +
                KP_U2_ID+" INTEGER, "+
                "FOREIGN KEY ("+KP_U1_ID+") REFERENCES User("+U_ID+") ON DELETE CASCADE, "+
                "FOREIGN KEY ("+KP_U2_ID+") REFERENCES User("+U_ID+") ON DELETE CASCADE"
                + ")");
        db.execSQL("PRAGMA foreign_keys=ON;");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FILMOVI);
        db.execSQL("DROP TABLE IF EXISTS " + USER);
        db.execSQL("DROP TABLE IF EXISTS " + USER_FILM);
        db.execSQL("DROP TABLE IF EXISTS " + KORISNIK_PRATI);

        onCreate(db);
    }
    public boolean insertFilm(String table,String[] vals,byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(F_IME, vals[0]);
        contentValues.put(F_GODINA, vals[1]);
        contentValues.put(F_OPIS, vals[2]);
        contentValues.put(F_ZANR, vals[3]);
        contentValues.put(F_SLIKA,img);
        long result = db.insert(table, null, contentValues);

        if (result == -1)
            return false;
        else {
            return true;
        }
    }
    public boolean insertData(String table, String[] vals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(table.equals(FILMOVI)){
            contentValues.put(F_IME, vals[0]);
            contentValues.put(F_GODINA, vals[1]);
            contentValues.put(F_OPIS, vals[2]);
            contentValues.put(F_ZANR, vals[3]);
        }
        else if(table.equals(USER)){
            contentValues.put(U_USERNAME, vals[0]);
            contentValues.put(U_PASSWORD, vals[1]);
        }
        else if(table.equals(USER_FILM)){
            contentValues.put(UF_FID, vals[0]);
            contentValues.put(UF_UID, vals[1]);
            contentValues.put(UF_VREME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }
        else if(table.equals(KORISNIK_PRATI)){
            contentValues.put(KP_U1_ID, vals[0]);
            contentValues.put(KP_U2_ID, vals[1]);
        }
        long result = db.insert(table, null, contentValues);

        if (result == -1)
            return false;
        else {
            return true;
        }
    }
    public Cursor execRawQuery(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        return cursor;
    }
    public Integer getIDOfUser(String username){
        Integer id;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM User WHERE USERNAME ='"+username+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            id=cursor.getInt(0);
        }
        else{
            return -1;
        }
        return id;
    }

    public boolean verifyUser(String un,String pw){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * from User WHERE USERNAME='"+un+"' AND PASSWORD='"+pw+"'";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }
    public Integer deleteUser(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        if(db.delete("User", "ID=?", new String[]{id})!=-1){
            db.delete("USER_FILM","U_ID=?",new String[]{id});
            return db.delete("Korisnik_Prati","U_ID1=? OR U_ID2=?",new String[]{id,id});
        }
        return -1;
    }
    public Integer deleteData (String table, String where, String what){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table, where+"=?", new String[]{what});
    }
    public Cursor getData(String table, String clause) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(clause.equals("")) {
            res = db.rawQuery("select * from " + table, null);
        }
        else{
            res = db.rawQuery("select * from " + table + " WHERE " + clause,null);
        }
        return res;
    }
    public String getNumOfFollowers(String id){
        String num="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        String query = "select COUNT(*) from Korisnik_Prati WHERE U_ID2="+id;
        res = db.rawQuery(query,null);
        if (res.getCount() == 0) {
            //Toast.makeText(ProfilActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
            num = "0";
        } else {
            if (res.moveToFirst()) {
                num = res.getString(0);
            }
        }
        return num;
    }

    public String getNumOfFollowing(String id){
        String num="";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        String query = "select COUNT(*) from Korisnik_Prati WHERE U_ID1="+id;
        res = db.rawQuery(query,null);
        if (res.getCount() == 0) {
            //Toast.makeText(ProfilActivity.this, "Nema podataka", Toast.LENGTH_LONG).show();
            num = "0";
        } else {
            if (res.moveToFirst()) {
                num = res.getString(0);
            }
        }
        return num;
    }

    public Cursor getAllData(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        res = db.rawQuery("select * from " + table, null);
        return res;
    }
    public void updateUser(String id,String un,String pw){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, un);
        cv.put(U_PASSWORD, pw);
        if(un.length()<1 || pw.length()<1){
            Toast.makeText(context, "Korisničko ime i lozinka moraju imati barem jedan karakter.", Toast.LENGTH_SHORT).show();
            return;
        }
        long res = db.update(USER, cv, "ID=?", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "Neuspesno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno azurirano", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateData(String id,String ime,String godina,String opis,String zanr,byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(F_IME, ime);
        cv.put(F_GODINA, godina);
        cv.put(F_OPIS, opis);
        cv.put(F_ZANR, zanr);
        cv.put(F_SLIKA, img);
        long res = db.update(FILMOVI, cv, "id=?", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "Neuspesno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno azurirano", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteData(String id1,String id2){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete( USER_FILM,"U_ID=? AND F_ID=?",new String[]{id1,id2});
        if (res == -1) {
            Toast.makeText(context, "Neuspesno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno obrisano", Toast.LENGTH_SHORT).show();
        }
    }
    boolean checkIfFollowing(String id1,String id2){
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM "+KORISNIK_PRATI+ " WHERE U_ID1="+id1+" AND U_ID2="+id2;
        long res = db.rawQuery(query,null).getCount();
        if (res == 0) {
            return false;
        } else {
            return true;
        }
    }
    void deleteFollowing(String id1,String id2){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete( KORISNIK_PRATI,"U_ID1=? AND U_ID2=?",new String[]{id1,id2});
        if (res == -1) {
            Toast.makeText(context, "Neuspesno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno obrisano", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(FILMOVI, "id=?", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "Neuspesno", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno obrisano", Toast.LENGTH_SHORT).show();
        }
    }
    boolean checkIfUserLikedMovie(String uid,String mid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query="SELECT * FROM "+USER_FILM+ " WHERE U_ID="+uid+" AND F_ID="+mid;
        long res = db.rawQuery(query,null).getCount();
        if (res == 0) {
            return false;
        } else {
            return true;
        }
    }
}
