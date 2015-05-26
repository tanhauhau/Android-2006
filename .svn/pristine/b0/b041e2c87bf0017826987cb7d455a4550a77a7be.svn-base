package com.ce2006.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ce2006.project.activity.account.ModifyParticularActivity;
import com.ce2006.project.activity.account.ModifyPasswordActivity;
import com.ce2006.project.activity.appointment.MakeAppointmentActivity;
import com.ce2006.project.activity.appointment.ViewModifyAppointmentActivity;
import com.ce2006.project.activity.firstaid.FirstAidActivity;
import com.ce2006.project.localstorage.PreferenceManager;
import com.example.user.ce2006_project.R;

/**
 * Patient's Main Actiivty
 */
public class PatientMainActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnMakeAppointment, btnChangeAppointment, btnChangeParticulars, btnFirstAidKit, btnLogout, btnChangePassword;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_patient);
        btnMakeAppointment = (Button) findViewById(R.id.btnMakeAppointment);
        btnChangeAppointment = (Button) findViewById(R.id.btnChangeAppointmnet);
        btnChangeParticulars = (Button) findViewById(R.id.btnUpdateParticulars);
        btnFirstAidKit = (Button) findViewById(R.id.btnFirstAid);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnMakeAppointment.setOnClickListener(this);
        btnChangeAppointment.setOnClickListener(this);
        btnChangeParticulars.setOnClickListener(this);
        btnFirstAidKit.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);

        PreferenceManager manager = PreferenceManager.getManager(this);
        txtName.setText(String.format(getString(R.string.hello_welcome), manager.getUserName()));
    }

    /**
     * Redirect to various activities
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == btnMakeAppointment) {
            startActivity(new Intent(this, MakeAppointmentActivity.class));
        } else if (v == btnChangeAppointment) {
            startActivity(new Intent(this, ViewModifyAppointmentActivity.class));
        } else if (v == btnChangeParticulars) {
            startActivity(new Intent(this, ModifyParticularActivity.class));
        } else if (v == btnFirstAidKit) {
            startActivity(new Intent(this, FirstAidActivity.class));
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
