package com.ce2006.project.activity.account;

import com.ce2006.project.localstorage.PreferenceManager;

/**
 * Act as a mean for {@link com.ce2006.project.activity.account.AuthActivity}
 * to see the progress of the {@link com.ce2006.project.activity.account.SignUpFragment}
 * and {@link com.ce2006.project.activity.account.LogInFragment}
 */
public interface AuthActivityListener {
    /**
     * Call when either {@link com.ce2006.project.activity.account.SignUpFragment}
     * and {@link com.ce2006.project.activity.account.LogInFragment} starts
     * its request to the server
     */
    public void startProgress();

    /**
     * Call when either {@link com.ce2006.project.activity.account.SignUpFragment}
     * and {@link com.ce2006.project.activity.account.LogInFragment} after
     * receiving results from the server
     */
    public void stopProgress();

    /**
     * Informing {@link com.ce2006.project.activity.account.AuthActivity}
     * with results
     *
     * @param success  status of the request
     * @param username username of the user
     * @param password password of the user
     * @param type     accountType of the user
     */
    public void endsWithResults(boolean success, String username, String password, PreferenceManager.AccountType type);
}
