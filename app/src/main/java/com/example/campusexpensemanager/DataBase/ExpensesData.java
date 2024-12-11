package com.example.campusexpensemanager.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.DataStatic;
import com.example.campusexpensemanager.MenuActivity;
import com.example.campusexpensemanager.Model.Expenses;
import com.example.campusexpensemanager.listCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpensesData extends SQLiteOpenHelper {

    public static final String DB_NAME = "Expenses";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "expenses";
    public static final String ID_COL = "id";

    public static final String AMOUNT_COL = "amount";
    public static final String NAME_COL = "name";
    public static final String NOTE_COL = "note";
    public static final String EMAIL_COL = "email";
    public static final String DATE_COL = "date";
    public static Integer TYPE_COL = 0;
    public static final String CATE_TABLE = "categoryTable";



    public ExpensesData(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AMOUNT_COL + " VARCHAR(200), "
                + NAME_COL + " VARCHAR(60), "
                + NOTE_COL + " VARCHAR(100),"
                + EMAIL_COL + " VARCHAR(100),"
                + DATE_COL + "  VARCHAR(100))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNewExpenses(String amount, String name, String note,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
       values.put(AMOUNT_COL,amount);
       values.put(NAME_COL, name);
       values.put(NOTE_COL, note);
       values.put(DATE_COL, date);
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return  insert;
    }

    public void update(String id, String amount, String name, String note, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMOUNT_COL, amount);
        contentValues.put(NAME_COL, name);
        contentValues.put(NOTE_COL, note);
        contentValues.put(DATE_COL, date);

        long result = database.update(TABLE_NAME, contentValues, "id=?", new String[]{id});
        if (result == -1) {
        } else {
        }
    }

    public boolean delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Xóa dòng có id cụ thể
        int result = database.delete(TABLE_NAME, "id=?", new String[]{id});

        // Kiểm tra kết quả
        return result > 0; // Trả về true nếu xóa thành công, false nếu thất bại
    }

    //create table category
    public void createCategoryTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CREATE TABLE " + CATE_TABLE + "( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " VARCHAR(60), "
                + TYPE_COL + " INTEGER, "
                + EMAIL_COL + " VARCHAR(100)";
        db.execSQL(query);
    }

    //getuseremail after login
    public String getUserEmail() {
        return DataStatic.email;
    }


    public List<listCategory> getAllCategories() {
        List<listCategory> categoriesList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (data.getCount() == 0) {

        } else {
            if (categoriesList == null) {
                categoriesList = new ArrayList<>();
            }

            while (data.moveToNext()) {
                categoriesList.add(new listCategory(data.getString(0), data.getString(1)));
            }
        }

        return categoriesList;
    }



    public List<Expenses> getAllIncome() {
        List<Expenses> expensesModelList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (data.getCount() == 0) {

        } else {
            if (expensesModelList == null) {
                expensesModelList = new ArrayList<>();
            }

            while (data.moveToNext()) {
                expensesModelList.add(new Expenses(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4)));
            }
        }

        return expensesModelList;
    }

}
