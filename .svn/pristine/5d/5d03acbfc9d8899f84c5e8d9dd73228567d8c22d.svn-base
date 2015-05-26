package com.ce2006.project.activity.account;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.server.Particulars;
import com.example.user.ce2006_project.R;

public class ModifyPasswordActivity extends ActionBarActivity implements View.OnClickListener {
    private EditText txtPassword1, txtPassword2;
    private Button btnSubmit;
    private View progressBar;
    private boolean inProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        txtPassword1 = (EditText) findViewById(R.id.txtPassword1);
        txtPassword2 = (EditText) findViewById(R.id.txtPassword2);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            //make sure the field not empty
            if (txtPassword1.getText().toString().isEmpty()) {
                Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_LONG).show();
                txtPassword1.requestFocus();
            }
            if (txtPassword2.getText().toString().isEmpty()) {
                Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_LONG).show();
                txtPassword2.requestFocus();
            }
            //password length at least 6 characters
            if (txtPassword1.getText().length() < 6) {
                txtPassword1.requestFocus();
                Toast.makeText(this, "Password should at least 6 characters!", Toast.LENGTH_LONG).show();
                return;
            }
            //password should be the same
            if (!txtPassword2.getText().toString().equals(txtPassword1.getText().toString())) {
                txtPassword2.requestFocus();
                Toast.makeText(this, "Password not the same!", Toast.LENGTH_LONG).show();
                return;
            }
            //else submit
            PreferenceManager manager = PreferenceManager.getManager(this);
            ModifyPasswordTask task = new ModifyPasswordTask(this);
            task.execute(manager.getUserName(), manager.getPassword(), txtPassword1.getText().toString());
            inProgress = true;
        }
    }

    void updatePreference(String username, String password) {
        PreferenceManager.getManager(this).saveDetails(username, password);
        inProgress = false;
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!inProgress) {
            super.onBackPressed();
        }
    }

    private class ModifyPasswordTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private String name, password, newpassword;

        private ModifyPasswordTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length != 3) {
                return false;
            } else {
                name = params[0];
                password = params[1];
                newpassword = params[2];
                return Particulars.updatePassword(name, password, newpassword);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success) {
                btnSubmit.setEnabled(true);
            } else {
                Toast.makeText(context, "Password Changed Successfully!", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
            updatePreference(name, newpassword);
        }
    }

}
