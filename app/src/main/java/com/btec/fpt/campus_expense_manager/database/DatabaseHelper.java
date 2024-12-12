package com.btec.fpt.campus_expense_manager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.entities.User;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Goals;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseDB";
    private static final int DATABASE_VERSION = 5;

    // Transactions table
    private static final String TABLE_TRANSACTION = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_EMAIL = "email";

    // User table
    private static final String TABLE_USER = "USER";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_PASSWORD = "password";

    // Category table
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // Goals table
    private static final String TABLE_GOALS = "GOALS";
    private static final String COLUMN_GOAL_ID = "goal_id";
    private static final String COLUMN_GOAL_NAME = "goal_name";
    private static final String COLUMN_GOAL_AMOUNT = "goal_amount";
    private static final String COLUMN_GOAL_CURRENT_AMOUNT = "current_amount";
    private static final String COLUMN_GOAL_ACHIEVED = "is_achieved";
    private static final String COLUMN_GOAL_EMAIL = "email";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transactions table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TYPE + " INTEGER,"
                + COLUMN_EMAIL + " TEXT," +
                COLUMN_CATEGORY + " TEXT" +

                ")";
        db.execSQL(CREATE_TRANSACTION_TABLE);

        // Create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT UNIQUE NOT NULL,"
                + COLUMN_EMAIL + " TEXT "+

                ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Create goals table
        String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
                + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GOAL_NAME + " TEXT, "
                + COLUMN_GOAL_AMOUNT + " REAL, "
                + COLUMN_GOAL_CURRENT_AMOUNT + " REAL, "
                + COLUMN_GOAL_ACHIEVED + " INTEGER DEFAULT 0, "
                + COLUMN_GOAL_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_GOALS_TABLE);

        insertDefaultCategories(db, null);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    // Insert a new transaction record
    public boolean insertTransaction(double amount, String description, String date, int type, String email, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_TRANSACTION, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateTransaction(int id, double amount, String description, String date,
                                     int type, String email, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CATEGORY, category);


        int rowsAffected = db.update(TABLE_TRANSACTION, values, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }
    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TRANSACTION, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new user record
    public boolean insertUser(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);

        String hashPassword = hashPassword(password);
        values.put(COLUMN_PASSWORD, hashPassword);

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }


    public boolean updateUser(int userId, String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        String hashPassword = hashPassword(password);

        values.put(COLUMN_PASSWORD, hashPassword);

        int rowsAffected = db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new category record
    public boolean insertCategory(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result != -1;
    }


    public boolean updateCategory(int categoryId, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_EMAIL, email);

        int rowsAffected = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsDeleted > 0;
    }


    // Retrieve all categories as a List<Category>
    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));

                Category category = new Category(id, name, email);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }


    public List<Category> getAllCategoryByEmail(String email) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Use "IS NULL" in SQL query when email is null
        String query;
        String[] queryParams;

        if (email == null) {
            query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_EMAIL + " IS NULL";
            queryParams = null;
        } else {
            query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_EMAIL + " = ? OR " + COLUMN_EMAIL + " IS NULL";
            queryParams = new String[]{email};
        }

        Cursor cursor = db.rawQuery(query, queryParams);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));

                Category category = new Category(id, name, email);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    // Retrieve all transactions
    public List<Transaction> getTransactionList() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, email, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }


    // Retrieve all transactions for a user by email
    public List<Transaction> getAllTransactionsByEmail(String email) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String email2 = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date,type, email2, category );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // Function to get a user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            // Extract user details from the cursor
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

            // Create a User object with the retrieved data
            user = new User(id, firstName, lastName, userEmail, password);
        }

        cursor.close();
        db.close();
        return user;
    }

    // Retrieve all users
    public List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

                User user = new User(id, firstName, lastName, email, password);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }


    public BalanceInfor getBalanceFromEmail(String email){

        User userFound = getUserByEmail(email);
        if(userFound!=null){

            String firstName = userFound.getFirstName();
            String lastName = userFound.getLastName();

            List<Transaction> allTransaction = getAllTransactionsByEmail(email);

            double expense = 0;
            double income = 0;
            double balance = 0;
            for(Transaction transaction: allTransaction){

                if(transaction.getType()==0){
                    expense += transaction.getAmount();
                }else if(transaction.getType()==1){
                    income += transaction.getAmount();
                }
            }

            balance = income - expense;

            BalanceInfor balanceInfor = new BalanceInfor(email, firstName + lastName, firstName, lastName, balance);
            balanceInfor.setEmail(email);
            balanceInfor.setBalance(balance);
            balanceInfor.setFirstName(firstName);
            balanceInfor.setLastName(lastName);


            return  balanceInfor;

        }


        return  null;
    }


    // Method to insert default categories into the database
    private void insertDefaultCategories(SQLiteDatabase db, String email) {
        String[] defaultCategories = {"Food", "Transport", "Entertainment", "Utilities", "Health"};

        for (String categoryName : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, categoryName);
            values.put(COLUMN_EMAIL, email); // Set email or leave it null for default
            db.insert(TABLE_CATEGORY, null, values);
        }
    }

    // Helper method to hash the password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to verify hashed password
    public boolean verifyPassword(String inputPassword, String storedHashedPassword) {
        // Hash the input password using the same method
        String hashedInputPassword = hashPassword(inputPassword);
        
        // Compare the hashed input with the stored hashed password
        return hashedInputPassword.equals(storedHashedPassword);
    }

    // Sign Up function
    public boolean signUp(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // User already exists
        }

        String hashedPassword = hashPassword(password); // Hash the password
        if (hashedPassword == null) {
            cursor.close();
            db.close();
            return false; // Hashing failed
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USER, null, values);
        cursor.close();
        db.close();

        if (result != -1) {
            // Set current user email when sign up is successful
            setCurrentUserEmail(email);
        }

        return result != -1;
    }

    // Sign In function
    public String signIn(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        String[] columns = {COLUMN_USER_ID, COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashedPassword};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        String userUuid = null;
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (userIdIndex != -1) {
                int userId = cursor.getInt(userIdIndex);
                userUuid = generateUuidForUser(userId);
            }
            cursor.close();
        }

        db.close();
        return userUuid;
    }

    private String generateUuidForUser(int userId) {
        // Create a deterministic UUID based on the user ID
        return UUID.nameUUIDFromBytes(String.valueOf(userId).getBytes(java.nio.charset.StandardCharsets.UTF_8)).toString();
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 1: Retrieve the current hashed password for the given email
        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String storedHashedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            cursor.close();

            // Step 2: Verify the old password
            if (!verifyPassword(oldPassword, storedHashedPassword)) {
                db.close();
                return false; // Old password is incorrect
            }

            // Step 3: Update the password with the new hashed password
            ContentValues values = new ContentValues();
            String newHashedPassword = hashPassword(newPassword);
            values.put(COLUMN_PASSWORD, newHashedPassword);

            int rowsAffected = db.update(TABLE_USER, values, COLUMN_EMAIL + " = ?", new String[]{email});
            db.close();
            return rowsAffected > 0; // Return true if the password was updated successfully
        } else {
            if (cursor != null) cursor.close();
            db.close();
            return false; // Email not found
        }
    }

    // Add goal methods
    public long addGoal(Goals goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, goal.getGoalName());
        values.put(COLUMN_GOAL_AMOUNT, goal.getGoalAmount());
        values.put(COLUMN_GOAL_CURRENT_AMOUNT, goal.getCurrentAmount());
        values.put(COLUMN_GOAL_ACHIEVED, goal.isAchieved() ? 1 : 0);
        values.put(COLUMN_GOAL_EMAIL, getCurrentUserEmail()); // Assuming you have a method to get current user's email

        long result = db.insert(TABLE_GOALS, null, values);
        db.close();
        return result;
    }

    public List<Goals> getAllGoals() {
        List<Goals> goalsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GOALS + 
                             " WHERE " + COLUMN_GOAL_EMAIL + " = '" + getCurrentUserEmail() + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int goalIdIndex = cursor.getColumnIndex(COLUMN_GOAL_ID);
                int goalNameIndex = cursor.getColumnIndex(COLUMN_GOAL_NAME);
                int goalAmountIndex = cursor.getColumnIndex(COLUMN_GOAL_AMOUNT);
                int goalCurrentAmountIndex = cursor.getColumnIndex(COLUMN_GOAL_CURRENT_AMOUNT);
                int goalAchievedIndex = cursor.getColumnIndex(COLUMN_GOAL_ACHIEVED);

                if (goalIdIndex >= 0 && goalNameIndex >= 0 && goalAmountIndex >= 0 &&
                        goalCurrentAmountIndex >= 0 && goalAchievedIndex >= 0) {
                    Goals goal = new Goals();
                    goal.setId(cursor.getInt(goalIdIndex));
                    goal.setGoalName(cursor.getString(goalNameIndex));
                    goal.setGoalAmount(cursor.getDouble(goalAmountIndex));
                    goal.setCurrentAmount(cursor.getDouble(goalCurrentAmountIndex));
                    goal.setAchieved(cursor.getInt(goalAchievedIndex) == 1);

                    goalsList.add(goal);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return goalsList;
    }

    public int updateGoal(Goals goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, goal.getGoalName());
        values.put(COLUMN_GOAL_AMOUNT, goal.getGoalAmount());
        values.put(COLUMN_GOAL_CURRENT_AMOUNT, goal.getCurrentAmount());
        values.put(COLUMN_GOAL_ACHIEVED, goal.isAchieved() ? 1 : 0);

        // Updating row
        return db.update(TABLE_GOALS, values, COLUMN_GOAL_ID + " = ?",
                new String[]{String.valueOf(goal.getId())});
    }

    public void deleteGoal(Goals goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, COLUMN_GOAL_ID + " = ?",
                new String[]{String.valueOf(goal.getId())});
        db.close();
    }

    // Helper method to get current user's email using SharedPreferences
    public String getCurrentUserEmail() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("current_user_email", null);
    }

    // Method to set current user's email when logging in
    public void setCurrentUserEmail(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_user_email", email);
        editor.apply();
    }

    // Method to clear current user's email when logging out
    public void clearCurrentUserEmail() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("current_user_email");
        editor.apply();
    }

    // Method to get transactions by date range for a specific user
    public List<Transaction> getTransactionsByDateRange(String email, long startDate, long endDate) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTION + 
                       " WHERE " + COLUMN_EMAIL + " = ? AND " + 
                       "strftime('%s', " + COLUMN_DATE + ") BETWEEN ? AND ?";

        // Convert milliseconds to formatted date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String startDateStr = dateFormat.format(new Date(startDate));
        String endDateStr = dateFormat.format(new Date(endDate));

        Cursor cursor = db.rawQuery(query, new String[]{email, startDateStr, endDateStr});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String email2 = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, email2, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // Convenience method to get current month's transactions
    public List<Transaction> getCurrentMonthTransactions(String email, String transactionType) {
        Calendar calendar = Calendar.getInstance();
        
        // Set to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfMonth = calendar.getTimeInMillis();

        // Set to the last day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endOfMonth = calendar.getTimeInMillis();

        return getTransactionsWithFilter(
            email, 
            startOfMonth, 
            endOfMonth, 
            transactionType, 
            TransactionSortOption.DATE_NEW_TO_OLD
        );
    }

    // Add this enum to support different sorting options
    public enum TransactionSortOption {
        DATE_NEW_TO_OLD,
        DATE_OLD_TO_NEW,
        AMOUNT_HIGH_TO_LOW,
        AMOUNT_LOW_TO_HIGH,
        ALPHABETICAL_ASC,
        ALPHABETICAL_DESC
    }

    // Advanced method to get transactions with more filtering and sorting options
    public List<Transaction> getTransactionsWithFilter(
            String email, 
            long startDate, 
            long endDate, 
            String transactionType, // "Income", "Expense", or null for all
            TransactionSortOption sortOption
    ) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Base query
        String query = "SELECT * FROM " + TABLE_TRANSACTION + 
                       " WHERE " + COLUMN_EMAIL + " = ? " +
                       " AND strftime('%s', " + COLUMN_DATE + ") BETWEEN ? AND ?";

        // Add transaction type filter if specified
        if (transactionType != null) {
            int typeValue = transactionType.equals("Income") ? 1 : 0;
            query += " AND " + COLUMN_TYPE + " = " + typeValue;
        }

        // Add sorting based on sort option
        switch (sortOption) {
            case DATE_NEW_TO_OLD:
                query += " ORDER BY " + COLUMN_DATE + " DESC";
                break;
            case DATE_OLD_TO_NEW:
                query += " ORDER BY " + COLUMN_DATE + " ASC";
                break;
            case AMOUNT_HIGH_TO_LOW:
                query += " ORDER BY " + COLUMN_AMOUNT + " DESC";
                break;
            case AMOUNT_LOW_TO_HIGH:
                query += " ORDER BY " + COLUMN_AMOUNT + " ASC";
                break;
            case ALPHABETICAL_ASC:
                query += " ORDER BY " + COLUMN_DESCRIPTION + " ASC";
                break;
            case ALPHABETICAL_DESC:
                query += " ORDER BY " + COLUMN_DESCRIPTION + " DESC";
                break;
        }

        // Convert milliseconds to formatted date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String startDateStr = dateFormat.format(new Date(startDate));
        String endDateStr = dateFormat.format(new Date(endDate));

        Cursor cursor = db.rawQuery(query, new String[]{email, startDateStr, endDateStr});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String email2 = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, email2, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // Advanced transaction filtering method
    public List<Transaction> getFilteredTransactions(
        String email, 
        Long startDate,     // Optional start date
        Long endDate,       // Optional end date
        String transactionType, // "Income", "Expense", or null
        String category,    // Optional category filter
        double minAmount,   // Optional minimum amount
        double maxAmount,   // Optional maximum amount
        String sortBy,      // "date", "amount", "category"
        boolean sortAscending // Sort direction
    ) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> filteredTransactions = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(email);

        // Date range filter
        if (startDate != null && endDate != null) {
            query += " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            selectionArgs.add(String.valueOf(startDate));
            selectionArgs.add(String.valueOf(endDate));
        }

        // Transaction type filter
        if (transactionType != null) {
            int typeValue = transactionType.equalsIgnoreCase("Income") ? 1 : 2;
            query += " AND " + COLUMN_TYPE + " = ?";
            selectionArgs.add(String.valueOf(typeValue));
        }

        // Category filter
        if (category != null && !category.isEmpty()) {
            query += " AND " + COLUMN_CATEGORY + " = ?";
            selectionArgs.add(category);
        }

        // Amount range filter
        if (minAmount > 0) {
            query += " AND " + COLUMN_AMOUNT + " >= ?";
            selectionArgs.add(String.valueOf(minAmount));
        }
        if (maxAmount > 0) {
            query += " AND " + COLUMN_AMOUNT + " <= ?";
            selectionArgs.add(String.valueOf(maxAmount));
        }

        // Sorting
        if (sortBy != null) {
            String orderBy = "";
            switch (sortBy.toLowerCase()) {
                case "date":
                    orderBy = COLUMN_DATE;
                    break;
                case "amount":
                    orderBy = COLUMN_AMOUNT;
                    break;
                case "category":
                    orderBy = COLUMN_CATEGORY;
                    break;
                default:
                    orderBy = COLUMN_DATE;
            }
            query += " ORDER BY " + orderBy + (sortAscending ? " ASC" : " DESC");
        }

        Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                int idIndex = cursor.getColumnIndex(COLUMN_TRANSACTION_ID);
                int amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);

                if (idIndex != -1) transaction.setId(cursor.getInt(idIndex));
                if (amountIndex != -1) transaction.setAmount(cursor.getDouble(amountIndex));
                if (descriptionIndex != -1) transaction.setDescription(cursor.getString(descriptionIndex));
                if (dateIndex != -1) transaction.setDate(cursor.getString(dateIndex));
                if (typeIndex != -1) transaction.setType(cursor.getInt(typeIndex));
                if (categoryIndex != -1) transaction.setCategory(cursor.getString(categoryIndex));
                if (emailIndex != -1) transaction.setEmail(cursor.getString(emailIndex));

                filteredTransactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return filteredTransactions;
    }

    // Method to get transaction summary
    public TransactionSummary getTransactionSummary(String email, Long startDate, Long endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        TransactionSummary summary = new TransactionSummary();

        String baseQuery = "SELECT " +
            "SUM(CASE WHEN " + COLUMN_TYPE + " = 1 THEN " + COLUMN_AMOUNT + " ELSE 0 END) as total_income, " +
            "SUM(CASE WHEN " + COLUMN_TYPE + " = 2 THEN " + COLUMN_AMOUNT + " ELSE 0 END) as total_expense, " +
            "COUNT(*) as total_transactions " +
            "FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ?";

        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(email);

        if (startDate != null && endDate != null) {
            baseQuery += " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            selectionArgs.add(String.valueOf(startDate));
            selectionArgs.add(String.valueOf(endDate));
        }

        Cursor cursor = db.rawQuery(baseQuery, selectionArgs.toArray(new String[0]));

       if (cursor.moveToFirst()) {
            int totalIncomeIndex = cursor.getColumnIndex("total_income");
            int totalExpenseIndex = cursor.getColumnIndex("total_expense");
            int totalTransactionsIndex = cursor.getColumnIndex("total_transactions");

            if (totalIncomeIndex >= 0) {
                summary.totalIncome = cursor.getDouble(totalIncomeIndex);
            }
            if (totalExpenseIndex >= 0) {
                summary.totalExpense = cursor.getDouble(totalExpenseIndex);
            }
            if (totalTransactionsIndex >= 0) {
                summary.totalTransactions = cursor.getInt(totalTransactionsIndex);
            }
        }

        cursor.close();
        db.close();

        return summary;
    }

    // Transaction summary class
    public static class TransactionSummary {
        public double totalIncome;
        public double totalExpense;
        public int totalTransactions;

        public double getNetBalance() {
            return totalIncome - totalExpense;
        }
    }
}
