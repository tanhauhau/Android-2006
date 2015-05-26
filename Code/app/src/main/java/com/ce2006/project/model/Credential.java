package com.ce2006.project.model;

import com.ce2006.project.localstorage.PreferenceManager;

/**
 * Class storing credentials of the user
 * Created by lhtan on 3/4/15.
 */
public class Credential {
    private String username;
    private String password;

    private Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @param username username of the user
     * @param password password of the user
     * @return credential
     */
    public static Credential getCredential(String username, String password) {
        return new Credential(username, password);
    }

    /**
     * preference manager is used to get the username and password
     *
     * @param preference preferencemanager that handles local preferences
     * @return credential from the preference
     */
    public static Credential getCredential(PreferenceManager preference) {
        return new Credential(preference.getUserName(), preference.getPassword());
    }

    /**
     * @return usernmae
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
