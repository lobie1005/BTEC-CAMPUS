package com.example.campusexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler1 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "income.db";
    public static final String TABLE_NAME = "income_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "AMOUNT";
    public static final String COL3 = "TYPE";
    public static final String COL4 = "NOTE";
    public static final String COL5 = "DATE";
    public static final String EMAIL = "EMAIL";

    public DatabaseHandler1(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                EMAIL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String amount, String type, String note, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, amount);
        contentValues.put(COL3, type);
        contentValues.put(COL4, note);
        contentValues.put(COL5, date);
        contentValues.put(EMAIL, DataStatic.email);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean update(String id, String amount, String type, String note, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, amount);
        contentValues.put(COL3, type);
        contentValues.put(COL4, note);
        contentValues.put(COL5, date);

        int result = database.update(TABLE_NAME, contentValues, "ID=?", new String[]{id});
        return result > 0;
    }

    public boolean delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        int result = database.delete(TABLE_NAME, "ID=?", new String[]{id});
        return result > 0;
    }

    public List<incomeModel> getAllIncome() {
        List<incomeModel> incomeModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL + "=?", new String[]{DataStatic.email});

        while (data.moveToNext()) {
            incomeModelList.add(new incomeModel(
                    data.getString(0), // ID
                    data.getString(1), // AMOUNT
                    data.getString(2), // TYPE
                    data.getString(3), // NOTE
                    data.getString(4)  // DATE
            ));
        }
        data.close();
        return incomeModelList;
    }
}
