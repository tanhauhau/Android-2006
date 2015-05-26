package com.ce2006.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ce2006.project.activity.account.AuthActivity;
import com.ce2006.project.localstorage.PreferenceManager;

/**
 * Main activity
 */
public class MainActivity extends ActionBarActivity {

    public static final int INTENT_REQUEST_LOGIN = 0;
    public static final int INTENT_REQUEST_MAIN = 1;
    public static final int RESULT_LOGOUT = 0;
    public static final int RESULT_BACK = 1;

    /**
     * if the user is logged in, call {@link MainActivity#launchMainPage()},
     * otherwise call {@link MainActivity#launchLoginPage()}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager manager = PreferenceManager.getManager(this);
        if (!manager.hasSavedDetails()) {
            launchLoginPage();
        } else {
            launchMainPage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                launchMainPage();
            } else {
                //close the activity if not logged in, means won't show the menu
                finish();
            }
        } else if (requestCode == INTENT_REQUEST_MAIN) {
            if (resultCode == RESULT_LOGOUT) {
                launchLoginPage();
            } else if (resultCode == RESULT_BACK) {
                finish();
            }
        }
    }

    /**
     * direct to respective main pages according to the user account type
     */
    private void launchMainPage() {
        switch (PreferenceManager.getManager(this).getAccountType()) {
            case Patient:
                startActivityForResult(new Intent(this, PatientMainActivity.class), INTENT_REQUEST_MAIN);
                break;
            case Doctor:
                startActivityForResult(new Intent(this, DoctorMainActivity.class), INTENT_REQUEST_MAIN);
                break;
            case Admin:
                startActivityForResult(new Intent(this, AdminMainActivity.class), INTENT_REQUEST_MAIN);
                break;
        }

    }

    /**
     * show the login page
     */
    private void launchLoginPage() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, INTENT_REQUEST_LOGIN);
    }
}