package com.ce2006.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ce2006.project.activity.account.ModifyPasswordActivity;
import com.ce2006.project.activity.doctor.ViewDoctorAppointmentListActivity;
import com.ce2006.project.localstorage.PreferenceManager;
import com.example.user.ce2006_project.R;

/**
 * Doctor main activity
 */
public class DoctorMainActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnViewAppointmnet, btnLogout, btnChangePassword;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_doctor);
        btnViewAppointmnet = (Button) findViewById(R.id.btnViewAppointmnet);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnViewAppointmnet.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);

        PreferenceManager manager = PreferenceManager.getManager(this);
        txtName.setText(String.format(getString(R.string.hello_welcome), manager.getUserName()));
    }

    /**
     * direct to respective activity
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == btnViewAppointmnet) {
            startActivity(new Intent(this, ViewDoctorAppointmentListActivity.class));
        } else if (v == btnChangePassword) {
            startActivity(new Intent(this, ModifyPasswordActivity.class));
        } else if (v == btnLogout) {
            PreferenceManager manager = PreferenceManager.getManager(this);
            manager.clear();
            setResult(MainActivity.RESULT_LOGOUT);
            finish();
        }
    }

    /**
     * tell the main activity to finish
     */
    @Override
    public void onBackPressed() {
        setResult(MainActivity.RESULT_BACK);
        super.onBackPressed();
    }
}
