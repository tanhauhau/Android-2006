package com.ce2006.project.activity.clinic;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ce2006.project.model.Clinic;
import com.ce2006.project.server.ClinicManager;
import com.example.user.ce2006_project.R;

/**
 * view clinic details
 * Created by lhtan on 4/4/15.
 */
public class ViewClinicFragment extends Fragment {
    private Clinic clinic;
    private ClinicManager manager;
    private TextView txtClinicDoctor, txtClinicAppointment;

    public static ViewClinicFragment getFragment(Clinic clinic, ClinicManager manager) {
        ViewClinicFragment fragment = new ViewClinicFragment();
        fragment.clinic = clinic;
        fragment.manager = manager;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clinic_view, null);
        TextView txtClinicName = (TextView) view.findViewById(R.id.txtClinicName);
        TextView txtClinicContact = (TextView) view.findViewById(R.id.txtClinicContact);
        TextView txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtClinicAppointment = (TextView) view.findViewById(R.id.txtClinicAppointment);
        txtClinicDoctor = (TextView) view.findViewById(R.id.txtClinicDoctor);
        txtClinicName.setText(clinic.getName());
        txtClinicContact.setText(clinic.getContact());
        txtAddress.setText(clinic.getLocation().toString());

        updateStats();

        return view;
    }

    /**
     * update clinics statistics (#doctors, #appointments)
     */
    private void updateStats() {
        new AsyncTask<Void, Void, int[]>() {
            @Override
            protected int[] doInBackground(Void[] params) {
                return manager.getClinicStat(clinic);
            }

            @Override
            protected void onPostExecute(int[] ints) {
                txtClinicDoctor.setText(String.valueOf(ints[0]));
                txtClinicAppointment.setText(String.valueOf(ints[1]));
            }
        }.execute();
    }
}
