package com.ce2006.project.activity.doctor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Credential;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.server.DoctorManager;
import com.example.user.ce2006_project.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity to create new doctor
 */

public class NewDoctorActivity extends ActionBarActivity implements OnClickListener {
    private DoctorManager doctorManager;
    private EditText txtDoctorName, txtEmail;
    private Spinner spinnerType;
    private View progressBar;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_new);

        txtDoctorName = (EditText) findViewById(R.id.txtDoctorName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        btnSubmit.setOnClickListener(this);
        doctorManager = new DoctorManager(Credential.getCredential(PreferenceManager.getManager(this)));
    }

    /**
     * When submit button is clicked,
     * update the doctor, and inform the listeners
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            boolean emptyField = false;
            TextView focusTo = null;
            if (txtDoctorName.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtDoctorName;
            }
            if (txtEmail.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtDoctorName;
            }
            if (emptyField) {
                if (focusTo != null) focusTo.requestFocus();
                Toast.makeText(this, "Field should not be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            //check email address format
            if (!isEmailValid(txtEmail.getText().toString())) {
                txtEmail.requestFocus();
                Toast.makeText(this, "Please input a valid email address!", Toast.LENGTH_LONG).show();
                return;
            }
            createDoctor();
        }
    }

    /**
     * check email address is a valid email address
     * @param email email address to be checked
     * @return true if valid, false otherwise
     */
    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Call {@link com.ce2006.project.server.DoctorManager#createDoctor(String, int, String)}
     * in a {@link android.os.AsyncTask}
     */
    private void createDoctor() {
        new AsyncTask<Void, Void, Doctor>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Doctor doInBackground(Void... params) {
                return doctorManager.createDoctor(
                        txtDoctorName.getText().toString(),
                        spinnerType.getSelectedItemPosition(),
                        txtEmail.getText().toString());
            }

            @Override
            protected void onPostExecute(Doctor doctor) {
                doctorCreated(doctor);
            }
        }.execute();
    }

    public void doctorCreated(Doctor doctor) {
        Intent intent = new Intent(this, ViewModifyDoctorActivity.class);
        intent.putExtra("doctor", doctor);
        startActivity(intent);
        finish();
    }
}