package com.ce2006.project.localstorage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manager that handles user's session
 */
public class PreferenceManager {
    private static PreferenceManager instance;
    Activity activity;

    /**
     * Manager that handles user's session
     *
     * @param activity
     */
    private PreferenceManager(Activity activity) {
        this.activity = activity;
    }

    ;

    /**
     * Static Factory method for the PreferenceManager
     *
     * @param activity
     * @return
     */
    public static PreferenceManager getManager(Activity activity) {
        if (instance == null) {
            instance = new PreferenceManager(activity);
        }
        return instance;
    }

    /**
     * @return name of the user logged in to the session or "" if no user logged in
     */
    public String getUserName() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("username", "");
    }

    /**
     * @return password of the user logged in to the session or
     * "" if no user logged in
     */
    public String getPassword() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("password", "");
    }

    /**
     * @return account type of the user logged in or null if no user logged in
     */
    public AccountType getAccountType() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        try {
            return AccountType.valueOf(sharedPref.getString("account", ""));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * remove session of the user
     */
    public void clear() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.commit();
    }

    /**
     * save session of the logged in user
     *
     * @param username username of the user
     * @param password password of the user
     */
    public void saveDetails(String username, String password) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * save session of the logged in user
     *
     * @param username username of the user
     * @param password password of the user
     * @param type     accounttype of the user
     */
    public void saveDetails(String username, String password, AccountType type) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("account", type.toString());
        editor.commit();
    }

    /**
     * @return true if user logged in, false otherwise
     */
    public boolean hasSavedDetails() {
        return !this.getUserName().isEmpty();
    }

    /**
     * AccountType enum, containing {@link com.ce2006.project.localstorage.PreferenceManager.AccountType#Patient},
     * {@link com.ce2006.project.localstorage.PreferenceManager.AccountType#Doctor},
     * {@link com.ce2006.project.localstorage.PreferenceManager.AccountType#Admin}
     *
     */
    public enum AccountType {
        /**
         * Logged in user is a patient
         */
        Patient,
        /**
         * Logged in user is a doctor
         */
        Doctor,
        /**
         * Logged in user is an admin
         */
        Admin}
}
