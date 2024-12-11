package com.example.campusexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Signup.db";
    private static final String TABLE_USERS = "allusers";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE allusers(email TEXT PRIMARY KEY, password TEXT, name TEXT, age INTEGER, phone TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("DROP TABLE IF EXISTS allusers");
        onCreate(MyDatabase);
    }

    public Boolean insertData(String email, String password, String name, int age, String phone) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("age", age);
        contentValues.put("phone", phone);
        long result = MyDatabase.insert("allusers", null, contentValues);

        return result != -1;
    }
    public Cursor getUserData(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        return MyDatabase.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM allusers WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        if (exists) {
            DataStatic.email = email; // Set the email on successful login
        }
        cursor.close();
        return exists;
    }


    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        int result = MyDatabase.update("allusers", contentValues, "email = ?", new String[]{email});
        return result > 0;
    }

    public User getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));
            String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
            cursor.close();
            return new User(email, name, age, phone);
        }
        return null;
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_EMAIL, COLUMN_NAME, COLUMN_AGE, COLUMN_PHONE},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean updateUserDetails(String email, String name, int age, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("age", age);
        contentValues.put("phone", phone);

        int result = db.update("allusers", contentValues, "email = ?", new String[]{email});
        return result > 0;
    }

}