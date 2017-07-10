package com.multimarca.tae.voceadorestae.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.multimarca.tae.voceadorestae.Objects.Carrier;
import com.multimarca.tae.voceadorestae.Objects.Message;

import java.util.ArrayList;
import java.util.List;


public class DbManager extends SQLiteOpenHelper {


    private Context ctx ;
    private static String DB_NAME = "multimarca.db";
    private static int Version = 1;
    private static DbManager mInstance = null;


    public DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.ctx = context;

    }
    public static synchronized DbManager getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DbManager(ctx.getApplicationContext(), DB_NAME, null, Version);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table carrier " +
                        "(id integer primary key," +
                        "name varchar(50), " +
                        "code integer, " +
                        "prices text," +
                        "observation text," +
                        "type integer" +
                        "status integer DEFAULT 1)"
        );
        db.execSQL(
                "create table transactions " +
                        "(id integer primary key," +
                        "company_ID varchar(2), " +
                        "company_name varchar(100), " +
                        "number text," +
                        "amount double," +
                        "type integer," +
                        "dateI datetime DEFAULT CURRENT_TIMESTAMP," +
                        "dateF datetime," +
                        "folio integer)"
        );
        db.execSQL(
                "create table gcm_history " +
                        "(id integer primary key," +
                        "title text, " +
                        "message text, " +
                        "dateI datetime DEFAULT CURRENT_TIMESTAMP)"
        );
    }
    public long InsertCarrier(String name, String code, String prices, String observation, int type) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("code",code);
        contentValues.put("prices", prices);
        contentValues.put("observation", observation);
        contentValues.put("type", type);

        return sqLiteDatabase.insert("carrier", null, contentValues);
    }


    public boolean truncateCarriers() {

        SQLiteDatabase sql = this.getReadableDatabase();
        sql.execSQL("DELETE FROM carrier");

        return true;
    }

    /**
     *
     * @param type 1 TAE 0 Servicios
     * @return Carriers
     */
    public List<Carrier> getAllCarriers(int type) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();


        Cursor cursor = sqLiteDatabase.rawQuery("select * from carrier WHERE type = "+type, null);
        List<Carrier> carriers = new ArrayList<>();
        if(cursor != null && !cursor.isClosed()) {
            if(cursor.moveToFirst()){
                do{
                    Carrier carrier = new Carrier(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("code")),
                            cursor.getString(cursor.getColumnIndex("prices")),
                            cursor.getString(cursor.getColumnIndex("observation")),
                            cursor.getInt(cursor.getColumnIndex("type"))
                    );
                    carriers.add(carrier);
                }while (cursor.moveToNext());
                cursor.close();
                return carriers;
            }
        }
        return carriers;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists carrier");
        db.execSQL(
                "create table carrier " +
                        "(id integer primary key," +
                        "name varchar(50), " +
                        "code integer, " +
                        "prices text," +
                        "observation text," +
                        "type integer" +
                        "status integer DEFAULT 1)"
        );
        db.execSQL("drop table if exists gcm_history");
        db.execSQL(
                "create table gcm_history " +
                        "(id integer primary key," +
                        "title text, " +
                        "message text, " +
                        "dateI datetime DEFAULT CURRENT_TIMESTAMP)");

    }
    public long insertGCMMessage(String title, String message) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("message", message);

        return sqLiteDatabase.insert("gcm_history", null, contentValues);
    }

    public ArrayList<Message> getMessages() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from gcm_history ORDER BY id DESC LIMIT 5", null);
        ArrayList<Message> messages = new ArrayList<>();
        if(cursor != null && !cursor.isClosed()) {
            if(cursor.moveToFirst()){
                do{
                    Message message = new Message(
                            cursor.getString(cursor.getColumnIndex("title")),
                            cursor.getString(cursor.getColumnIndex("message"))
                    );
                    messages.add(message);
                }while (cursor.moveToNext());
                cursor.close();
                return messages;
            }
        }
        return messages;
    }

    public long insertTransaction(String company_id, String company, String number, String amount, String type) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("company_id",company_id);
        contentValues.put("company_name",company);
        contentValues.put("number",number);
        contentValues.put("amount",amount);
        contentValues.put("type", type);

        return sqLiteDatabase.insert("transactions", null, contentValues);
    }
}
