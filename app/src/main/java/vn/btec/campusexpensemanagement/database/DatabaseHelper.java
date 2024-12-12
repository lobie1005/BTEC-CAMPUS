package vn.btec.campusexpensemanagement.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Base64;

import vn.btec.campusexpensemanagement.entities.Transaction;
import vn.btec.campusexpensemanagement.entities.User;
import vn.btec.campusexpensemanagement.entities.Category;
import vn.btec.campusexpensemanagement.entities.Goals;
import vn.btec.campusexpensemanagement.models.BalanceInfor;

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

    private static final String DATABASE_NAME = "campus_expense.db";
    private static final int DATABASE_VERSION = 3;

    // Transactions table
    private static final String TABLE_TRANSACTION = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_USER_ID = "user_id";

    // User table
    private static final String TABLE_USER = "users";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Category table
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_TYPE = "type";
    private static final String COLUMN_CATEGORY_EMAIL = "email";

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
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TYPE + " INTEGER,"
                + COLUMN_CATEGORY + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_TRANSACTION_ID + ")" +
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
                + COLUMN_CATEGORY_TYPE + " TEXT,"
                + COLUMN_CATEGORY_EMAIL + " TEXT " +

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
        if (oldVersion < 2) {
            // Add user_id column to transactions
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTION + 
                      " ADD COLUMN " + COLUMN_USER_ID + " INTEGER");
        }
        if (oldVersion < 3) {
            // Add any new upgrades
        }
    }

    // Insert a new transaction record
    public long addTransaction(double amount, String description, String date, int type, String email,
                               String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_USER_ID, getUserId(email));
        values.put(COLUMN_CATEGORY, category);

        return db.insert(TABLE_TRANSACTION, null, values);
    }

    public long addTransaction(Transaction transaction) {
        return addTransaction(
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getEmail(),
                transaction.getCategory());
    }

    public boolean updateTransaction(int id, double amount, String description, String date,
                                     int type, String email, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_USER_ID, getUserId(email));
        values.put(COLUMN_CATEGORY, category);

        int rowsAffected = db.update(TABLE_TRANSACTION, values, COLUMN_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TRANSACTION, COLUMN_TRANSACTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new user record
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PASSWORD, user.getPassword());
        return db.insert(TABLE_USER, null, values);
    }

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

        int rowsAffected = db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
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
    public boolean insertCategory(String name, String type, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_TYPE, type);
        values.put(COLUMN_CATEGORY_EMAIL, email);

        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateCategory(int categoryId, String name, String type, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_CATEGORY_TYPE, type);
        values.put(COLUMN_CATEGORY_EMAIL, email);

        int rowsAffected = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
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
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                @SuppressLint("Range")
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE));
                @SuppressLint("Range")
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_EMAIL));

                Category category = new Category(id, name, type, email);
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
            query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_CATEGORY_EMAIL + " IS NULL";
            queryParams = null;
        } else {
            query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_CATEGORY_EMAIL + " = ? OR " + COLUMN_CATEGORY_EMAIL
                    + " IS NULL";
            queryParams = new String[]{email};
        }

        Cursor cursor = db.rawQuery(query, queryParams);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                @SuppressLint("Range")
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE));

                Category category = new Category(id, name, type, email);
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
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range")
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range")
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range")
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range")
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range")
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range")
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, getUserEmailById(userId), category);
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

        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId(email))});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range")
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range")
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range")
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range")
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range")
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range")
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, getUserEmailById(userId), category);
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
        Cursor cursor = db.query(TABLE_USER, null, 
            COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);

        if (cursor.moveToFirst()) {
            // Extract user details from the cursor
            @SuppressLint("Range")
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            @SuppressLint("Range")
            String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            @SuppressLint("Range")
            String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
            @SuppressLint("Range")
            String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            @SuppressLint("Range")
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

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
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range")
                String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
                @SuppressLint("Range")
                String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
                @SuppressLint("Range")
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range")
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

                User user = new User(id, firstName, lastName, email, password);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public BalanceInfor getBalanceInfor(String email) {
        double totalIncome = 0;
        double totalExpense = 0;

        List<Transaction> transactions = getAllTransactionsByEmail(email);
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.INCOME_TYPE) {
                totalIncome += transaction.getAmount();
            } else {
                totalExpense += transaction.getAmount();
            }
        }

        double netBalance = totalIncome - totalExpense;
        User user = getUserByEmail(email);
        return new BalanceInfor(email, user.getFirstName() + " " + user.getLastName(), user.getFirstName(),
                user.getLastName(), netBalance);
    }

    // Method to insert default categories into the database
    private void insertDefaultCategories(SQLiteDatabase db, String email) {
        String[] defaultCategories = {"Food", "Transport", "Entertainment", "Utilities", "Health"};

        for (String categoryName : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, categoryName);
            values.put(COLUMN_CATEGORY_EMAIL, email); // Set email or leave it null for default
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
    public boolean isRegisteredEmailDuplicate(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});

        boolean isDuplicate = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isDuplicate;
    }

    public boolean signUp(String firstName, String lastName, String email, String password) {
        if (isRegisteredEmailDuplicate(email)) {
            return false; // Email already exists
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String hashedPassword = hashPassword(password); // Hash the password

        if (hashedPassword == null) {
            db.close();
            return false; // Hashing failed
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        if (result != -1) {
            // Set current user email when sign-up is successful
            setCurrentUserEmail(email);
        }

        return result != -1;
    }

    // Sign In function
    public boolean signIn(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        String[] columns = {COLUMN_USER_ID, COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashedPassword};

        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        boolean isSuccess = false;
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (userIdIndex != -1) {
                int userId = cursor.getInt(userIdIndex);
                generateUuidForUser(userId); // We can still generate the UUID if needed
                isSuccess = true;
            }
            cursor.close();
        }

        db.close();
        return isSuccess;
    }

    private String generateUuidForUser(int userId) {
        // Create a deterministic UUID based on the user ID
        return UUID.nameUUIDFromBytes(String.valueOf(userId).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public boolean changePassword(String email, String currentPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_PASSWORD};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            cursor.close();
            
            if (verifyPassword(currentPassword, storedPassword)) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_PASSWORD, hashPassword(newPassword));
                
                int rowsAffected = db.update(TABLE_USER, values, selection, selectionArgs);
                return rowsAffected > 0;
            }
        }
        return false;
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

    // Get current userid (uuid)
    public Integer getCurrentUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("current_user_id", 0);
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
                " WHERE " + COLUMN_USER_ID + " = ? " +
                " AND strftime('%s', " + COLUMN_DATE + ") BETWEEN ? AND ?";

        // Convert milliseconds to formatted date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String startDateStr = dateFormat.format(new Date(startDate));
        String endDateStr = dateFormat.format(new Date(endDate));

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId(email)), startDateStr, endDateStr});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range")
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range")
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range")
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range")
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range")
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range")
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, getUserEmailById(userId), category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // Convenience method to get current month's transactions
    public List<Transaction> getCurrentMonthTransactions(String email, String transactionType) {
        // Validate inputs
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (transactionType == null || transactionType.trim().isEmpty()) {
            transactionType = "ALL"; // Set a default value or use logic to handle this case
        }

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

        // Convert long timestamps to String
        String startOfMonthStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(startOfMonth));
        String endOfMonthStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(endOfMonth));

        // Fetch transactions using filters
        return getTransactionsWithFilter(
                email,
                null, // Assuming type filter is not used here
                transactionType,
                startOfMonthStr,
                endOfMonthStr,
                null, // Assuming no minAmount filter
                null, // Assuming no maxAmount filter
                TransactionSortOption.DATE_NEW_TO_OLD);
    }

    // Advanced method to get transactions with more filtering and sorting options
    public enum TransactionSortOption {
        DATE_NEW_TO_OLD,
        DATE_OLD_TO_NEW,
        AMOUNT_HIGH_TO_LOW,
        AMOUNT_LOW_TO_HIGH,
        ALPHABETICAL_ASC,
        ALPHABETICAL_DESC
    }

    public List<Transaction> getTransactionsWithFilter(
            String email,
            Integer type,
            String category,
            String startDate,
            String endDate,
            Double minAmount,
            Double maxAmount,
            TransactionSortOption sortOption) {

        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> whereConditions = new ArrayList<>();
        List<String> whereArgs = new ArrayList<>();

        // Base query
        String baseQuery = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE ";

        // Add email condition
        whereConditions.add(COLUMN_USER_ID + " = ?");
        whereArgs.add(String.valueOf(getUserId(email)));

        // Add type condition if specified
        if (type != null) {
            whereConditions.add(COLUMN_TYPE + " = ?");
            whereArgs.add(String.valueOf(type));
        }

        // Add category condition if specified
        if (category != null && !category.isEmpty()) {
            whereConditions.add(COLUMN_CATEGORY + " = ?");
            whereArgs.add(category);
        }

        // Add date range conditions if specified
        if (startDate != null && !startDate.isEmpty()) {
            whereConditions.add(COLUMN_DATE + " >= ?");
            whereArgs.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            whereConditions.add(COLUMN_DATE + " <= ?");
            whereArgs.add(endDate);
        }

        // Add amount range conditions if specified
        if (minAmount != null) {
            whereConditions.add(COLUMN_AMOUNT + " >= ?");
            whereArgs.add(String.valueOf(minAmount));
        }
        if (maxAmount != null) {
            whereConditions.add(COLUMN_AMOUNT + " <= ?");
            whereArgs.add(String.valueOf(maxAmount));
        }

        // Combine where conditions
        String whereClause = String.join(" AND ", whereConditions);
        String query = baseQuery + whereClause;

        // Add ORDER BY clause based on sort option
        if (sortOption != null) {
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
        }

        Cursor cursor = db.rawQuery(query, whereArgs.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                transaction.setType(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE)));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                transaction.setEmail(getUserEmailById(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    // Advanced transaction filtering method
    public List<Transaction> getFilteredTransactions(
            String email,
            Long startDate, // Optional start date
            Long endDate, // Optional end date
            String transactionType, // "Income", "Expense", or null
            String category, // Optional category filter
            double minAmount, // Optional minimum amount
            double maxAmount, // Optional maximum amount
            String sortBy, // "date", "amount", "category"
            boolean sortAscending // Sort direction
    ) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> filteredTransactions = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(String.valueOf(getUserId(email)));

        // Date range filter
        if (startDate != null && endDate != null) {
            query += " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            selectionArgs.add(String.valueOf(startDate));
            selectionArgs.add(String.valueOf(endDate));
        }

        // Transaction type filter
        if (transactionType != null) {
            int typeValue = transactionType.equals("Income") ? 1 : 2;
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
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);

                if (idIndex != -1)
                    transaction.setId(cursor.getInt(idIndex));
                if (amountIndex != -1)
                    transaction.setAmount(cursor.getDouble(amountIndex));
                if (descriptionIndex != -1)
                    transaction.setDescription(cursor.getString(descriptionIndex));
                if (dateIndex != -1)
                    transaction.setDate(cursor.getString(dateIndex));
                if (typeIndex != -1)
                    transaction.setType(cursor.getInt(typeIndex));
                if (categoryIndex != -1)
                    transaction.setCategory(cursor.getString(categoryIndex));
                if (userIdIndex != -1)
                    transaction.setEmail(getUserEmailById(cursor.getInt(userIdIndex)));

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
                "FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?";

        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(String.valueOf(getUserId(email)));

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

    public int getUserId(String email) {
        if (email == null || email.trim().isEmpty()) {
            return -1;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getUserEmailById(int userId) {
        if (userId <= 0) {
            return null;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        try (Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public boolean authenticateUser(String email, String password) {
        if (email == null || password == null) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        try (Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)) {
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getRecentTransactions(String email, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTION +
                " WHERE " + COLUMN_USER_ID + " = ?" +
                " ORDER BY " + COLUMN_DATE + " DESC" +
                " LIMIT " + limit;

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId(email))});

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                transaction.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                transaction.setType(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE)));
                transaction.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                transaction.setEmail(getUserEmailById(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    public double getTotalIncome(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTION +
                " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_TYPE + " = 1";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId(email))});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public double getTotalExpense(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTION +
                " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_TYPE + " = 2";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(getUserId(email))});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // New methods to manage categories and spending threshold
    public boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_TYPE, category.getType());
        values.put(COLUMN_CATEGORY_EMAIL, category.getEmail());
        long result = db.insert(TABLE_CATEGORY, null, values);
        return result != -1; // Return true if insertion was successful
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_TYPE, category.getType());
        String whereClause = COLUMN_CATEGORY_ID + " = ?";
        String[] whereArgs = {String.valueOf(category.getId())};
        int rowsAffected = db.update(TABLE_CATEGORY, values, whereClause, whereArgs);
        return rowsAffected > 0; // Return true if update was successful
    }


    public List<Category> getAllCategories(String email) {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_CATEGORY_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_CATEGORY, null, selection, selectionArgs, null, null, null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_TYPE));
                String emailValue = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_EMAIL));
                
                // Use the constructor with parameters
                Category category = new Category(id, name, type, emailValue);
                categories.add(category);
            }
            cursor.close();
        }
        return categories;
    }

    public void setSpendingThreshold(double threshold) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("spending_threshold", (float) threshold);
        editor.apply();
    }

    public double getSpendingThreshold() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat("spending_threshold", 0);
    }
}
