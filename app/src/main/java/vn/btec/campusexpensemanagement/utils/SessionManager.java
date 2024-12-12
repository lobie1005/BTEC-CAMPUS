public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    // Add timeout management
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes
    private static final String KEY_LAST_ACTIVITY = "last_activity";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static SessionManager instance;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void createLoginSession(int userId, String email) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public String getCurrentUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public int getCurrentUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
    
    public boolean isSessionValid() {
        if (!isLoggedIn()) return false;
        
        long lastActivity = prefs.getLong(KEY_LAST_ACTIVITY, 0);
        long now = System.currentTimeMillis();
        
        if (now - lastActivity > SESSION_TIMEOUT) {
            logout();
            return false;
        }
        
        // Update last activity
        editor.putLong(KEY_LAST_ACTIVITY, now);
        editor.apply();
        return true;
    }
}
