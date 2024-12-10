package vn.btec.campusexpensemanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "user_prefs";
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("remember_me", true);
        editor.apply();
    }

    public void clearLoginCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.putBoolean("remember_me", false);
        editor.apply();
    }

    public String getRememberedEmail() {
        return sharedPreferences.getString("email", "");
    }

    public String getRememberedPassword() {
        return sharedPreferences.getString("password", "");
    }

    public boolean isRememberMeEnabled() {
        return sharedPreferences.getBoolean("remember_me", false);
    }

    public void saveCurrentUser(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_user_email", email);
        editor.apply();
    }

    public String getCurrentUser() {
        return sharedPreferences.getString("current_user_email", null);
    }
} 