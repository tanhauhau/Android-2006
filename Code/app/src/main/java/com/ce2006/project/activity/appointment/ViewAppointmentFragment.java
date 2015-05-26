package com.ce2006.project.activity.appointment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ce2006.project.model.Appointment;
import com.example.user.ce2006_project.R;

/**
 * showing details of the appointment
 * Created by lhtan on 31/3/15.
 */
public class ViewAppointmentFragment extends Fragment {
    private Appointment appointment;
    private TextView txtClinicName, txtClinicAddress, txtClinicContact, txtDoctorName, txtAppointmentDate, txtAppointmentTime, txtNote;

    public static ViewAppointmentFragment getFragment(Appointment appointment) {
        ViewAppointmentFragment fragment = new ViewAppointmentFragment();
        fragment.appointment = appointment;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_view, null);
        txtClinicName = (TextView) view.findViewById(R.id.txtClinicName);
        txtClinicAddress = (TextView) view.findViewById(R.id.txtClinicAddress);
        txtClinicContact = (TextView) view.findViewById(R.id.txtClinicContact);
        txtDoctorName = (TextView) view.findViewById(R.id.txtDoctorName);
        txtAppointmentDate = (TextView) view.findViewById(R.id.txtAppointmentDate);
        txtAppointmentTime = (TextView) view.findViewById(R.id.txtAppointmentTime);
        txtNote = (TextView) view.findViewById(R.id.txtNote);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Tan", "onResume() " + appointment);
        txtClinicName.setText(appointment.getClinicName());
        txtClinicAddress.setText(appointment.getClinicAddress());
        txtClinicContact.setText(appointment.getClinicContact());
        txtDoctorName.setText(appointment.getDoctorName());
        txtAppointmentDate.setText(appointment.getDateString());
        txtAppointmentTime.setText(appointment.getTimeString());
        txtNote.setText(appointment.getNote());
    }
}
