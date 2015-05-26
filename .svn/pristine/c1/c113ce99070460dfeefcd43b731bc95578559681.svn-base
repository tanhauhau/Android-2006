package com.ce2006.project.activity.doctor;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ce2006.project.model.Appointment;
import com.example.user.ce2006_project.R;


/**
 * View details of the appointment of the doctor
 * Created by lhtan on 31/3/15.
 */
public class ViewDoctorAppointmentActivity extends ActionBarActivity implements View.OnClickListener {

    private Appointment appointment;
    private Button btnRemind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_appointment_doctor_view);

        appointment = (Appointment) getIntent().getSerializableExtra(ViewDoctorAppointmentListActivity.APPOINTMENT);
        Log.d("Tan", "received: " + appointment);
        TextView txtPatientName = (TextView) findViewById(R.id.txtPatientName);
        TextView txtAppointmentDate = (TextView) findViewById(R.id.txtAppointmentDate);
        TextView txtAppointmentTime = (TextView) findViewById(R.id.txtAppointmentTime);
        TextView txtNote = (TextView) findViewById(R.id.txtNote);

        Log.d("Tan", "appointment.patient: " + appointment.getPatient());
        Log.d("Tan", "appointment.patient.name: " + appointment.getPatient().getName());
        txtPatientName.setText(appointment.getPatient().getName());
        txtAppointmentDate.setText(appointment.getDateString());
        txtAppointmentTime.setText(appointment.getTimeString());
        txtNote.setText(appointment.getNote());

        btnRemind = (Button) findViewById(R.id.btnRemind);
        btnRemind.setVisibility(View.GONE);
        btnRemind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}




