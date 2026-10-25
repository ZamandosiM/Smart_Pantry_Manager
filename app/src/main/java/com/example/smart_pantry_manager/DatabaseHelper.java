package com.example.smart_pantry_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        onCreate(db);
    }
    public boolean addIngredient(String name, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);

        long result = db.insert("ingredients", null, values);
        db.close();

        return result != -1;
    }
    public Cursor getAllIngredients() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredients ORDER BY name ASC",
                null
        );
    }
    public boolean deleteIngredient(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                "ingredients",
                "id = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result > 0;
    }
}