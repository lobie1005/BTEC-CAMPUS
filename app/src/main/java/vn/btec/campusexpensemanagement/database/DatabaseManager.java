public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseHelper databaseHelper;
    private final Context context;

    private DatabaseManager(Context context) {
        this.context = context.getApplicationContext();
        databaseHelper = new DatabaseHelper(this.context);
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public DatabaseHelper getHelper() {
        return databaseHelper;
    }

    public void closeDatabase() {
        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }
    }
}
