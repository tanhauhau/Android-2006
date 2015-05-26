package com.ce2006.project.activity.account;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.server.Particulars;
import com.example.user.ce2006_project.R;

/**
 * Log In Fragment
 */
public class LogInFragment extends Fragment implements View.OnClickListener {
    private EditText _txtName, _txtPassword;
    private Button _btnSubmit, _btnSignUp;
    private ProgressBar _progressBar;
    private LoginTask loginTask;
    private AuthActivityListener listener;

    /**
     * Override onAttach, activity must implement {@link com.ce2006.project.activity.account.AuthActivityListener}
     *
     * @param activity
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.label_login);
        try {
            listener = (AuthActivityListener) activity;
        } catch (Exception e) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, null);
        _txtName = (EditText) view.findViewById(R.id.txtName);
        _txtPassword = (EditText) view.findViewById(R.id.txtPassword1);
        _btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        _btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        _btnSubmit.setOnClickListener(this);
        _btnSignUp.setOnClickListener(this);
        _progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    /**
     * Override onClick
     * if _btnSubmit is clicked,
     * check username and password field
     * and start {@link com.ce2006.project.activity.account.LogInFragment.LoginTask}
     * if _btnSignUp is clicked,
     * switch to signup page
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == _btnSubmit) {
            //check username and password field filled
            String username = _txtName.getText().toString();
            String password = _txtPassword.getText().toString();
            if (username.isEmpty()) {
                _txtName.requestFocus();
                Toast.makeText(getActivity(), "Please fill in your name!", Toast.LENGTH_LONG).show();
            } else if (password.isEmpty()) {
                _txtPassword.requestFocus();
                Toast.makeText(getActivity(), "Please fill in your password!", Toast.LENGTH_LONG).show();
            } else {
                loginTask = new LoginTask(getActivity());
                loginTask.execute(username, password);
            }
        } else if (v == _btnSignUp) {
            //
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new SignUpFragment())
                    .commit();
        }
    }

    /**
     * Log In Task
     * Called {@link com.ce2006.project.server.Particulars#checkUsernamePassword(String, String)}
     * to log in, the results returned is either {@code "failed"},
     * {@code "admin"}, {@code "doctor"} or {@code "patient"}
     */
    private class LoginTask extends AsyncTask<String, Void, String> {
        private Context context;
        /**
         * username used for the login task
         */
        private String name;
        /**
         * password used for the login task
         */
        private String password;

        private LoginTask(Context context) {
            this.context = context;
        }

        /**
         * Show the progress bar and informs the listener the login process
         * is going to start
         */
        @Override
        protected void onPreExecute() {
            _progressBar.setVisibility(View.VISIBLE);
            listener.startProgress();
        }

        /**
         * Call {@link com.ce2006.project.server.Particulars#checkUsernamePassword(String, String)}
         *
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            if (params.length != 2) {
                return "";
            } else {
                name = params[0];
                password = params[1];
                return new Particulars().checkUsernamePassword(name, password);
            }
        }

        /**
         * Hide the progress bar, inform the listener where the login process
         * has just ended and
         * process the results returned by the server
         * if login successful, inform the listener
         *
         * @param type
         */
        @Override
        protected void onPostExecute(String type) {
            listener.stopProgress();

            if (type.equals("failed")) {
                _txtPassword.requestFocus();
                Toast.makeText(context, "Please make sure you type in the correct username/password.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Successfully Log In!", Toast.LENGTH_LONG).show();
                PreferenceManager.AccountType accountType = PreferenceManager.AccountType.Patient;
                if (type.equals("admin")) {
                    accountType = PreferenceManager.AccountType.Admin;
                } else if (type.equals("doctor")) {
                    accountType = PreferenceManager.AccountType.Doctor;
                }
                listener.endsWithResults(true, name, password, accountType);
            }
            _progressBar.setVisibility(View.INVISIBLE);

        }
    }

}
