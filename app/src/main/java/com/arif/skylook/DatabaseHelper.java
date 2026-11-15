package com.arif.skylook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "skylook.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SELECTED_CITY = "selected_city";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SELECTED_CITY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COUNTRY + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_CITY);
        onCreate(db);
    }

    public void saveSelectedCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SELECTED_CITY, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, 1);
        values.put(COLUMN_NAME, city.getName());
        values.put(COLUMN_COUNTRY, city.getCountry());
        values.put(COLUMN_LATITUDE, city.getLatitude());
        values.put(COLUMN_LONGITUDE, city.getLongitude());
        db.insert(TABLE_SELECTED_CITY, null, values);
        db.close();
    }

    public City getSelectedCity() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SELECTED_CITY,
                new String[]{COLUMN_NAME, COLUMN_COUNTRY, COLUMN_LATITUDE, COLUMN_LONGITUDE},
                COLUMN_ID + "=?",
                new String[]{"1"},
                null, null, null);

        City city = null;
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
            city = new City(name, country, latitude, longitude);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return city;
    }

    public boolean hasSelectedCity() {
        return getSelectedCity() != null;
    }
}
