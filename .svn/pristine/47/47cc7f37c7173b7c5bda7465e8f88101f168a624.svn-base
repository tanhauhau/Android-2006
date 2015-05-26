package com.ce2006.project.activity.account;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ce2006.project.localstorage.PreferenceManager;
import com.example.user.ce2006_project.R;

/**
 * Authentication Activity
 * starts with having a {@link com.ce2006.project.activity.account.LogInFragment} in the activity,
 * the users can swap between {@link com.ce2006.project.activity.account.LogInFragment}
 * or {@link com.ce2006.project.activity.account.SignUpFragment} either fragment
 * will return results via {@link AuthActivityListener}.
 * Results returned contain username, password and account type,
 * which will saved to local preferences via {@link com.ce2006.project.localstorage.PreferenceManager}
 */

public class AuthActivity extends ActionBarActivity implements AuthActivityListener {
    private boolean _inProgress = false;

    /**
     * Override back pressed handler by preventing leaving from the activity
     * when {@link com.ce2006.project.activity.account.LogInFragment} or
     * {@link com.ce2006.project.activity.account.SignUpFragment} is in
     * the middle of sending request and receiving results from the server.
     */
    @Override
    public void onBackPressed() {
        if (!_inProgress) {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    /**
     * override onCreate method, start with adding
     * {@link com.ce2006.project.activity.account.LogInFragment}
     * to the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new LogInFragment())
                .commit();
    }


    /*
        Implementing AuthActivityListener
     */
    @Override
    public void startProgress() {
        _inProgress = true;
    }

    @Override
    public void stopProgress() {
        _inProgress = false;
    }

    @Override
    public void endsWithResults(boolean success, String username, String password, PreferenceManager.AccountType type) {
        if (success) {
            setResult(Activity.RESULT_OK);
            //write to preference
            PreferenceManager manager = PreferenceManager.getManager(this);
            manager.saveDetails(username, password, type);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }
}
