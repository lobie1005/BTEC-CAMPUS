public class ExpenseManagerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize singletons
        DatabaseManager.getInstance(this);
        SessionManager.getInstance(this);
        
        // Enable strict mode in debug builds
        if (BuildConfig.DEBUG) {
            enableStrictMode();
        }
    }
    
    private void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .build());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseManager.getInstance(this).closeDatabase();
    }
}
