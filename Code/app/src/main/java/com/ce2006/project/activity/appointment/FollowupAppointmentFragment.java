package com.ce2006.project.activity.appointment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.ce2006.project.model.Appointment;
import com.ce2006.project.server.AppointmentBuilder;
import com.example.user.ce2006_project.R;

/**
 *
 */
public class FollowupAppointmentFragment extends Fragment implements ViewAppointmentListFragment.Listener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private AppointmentBuilder appointmentBuilder;
    private Button btnChooseDate, btnSubmit;
    private Spinner spinnerTime;
    private View _progressBar;


    public static FollowupAppointmentFragment getFragment(AppointmentBuilder appointmentBuilder) {
        FollowupAppointmentFragment fragment = new FollowupAppointmentFragment();
        fragment.appointmentBuilder = appointmentBuilder;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, null);
        btnChooseDate = (Button) view.findViewById(R.id.btnChooseDate);
        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        _progressBar = view.findViewById(R.id.progressBar);

        spinnerTime.setOnItemSelectedListener(this);
        btnChooseDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("follow_list")
                .add(R.id.frag_container, ViewAppointmentListFragment.getFragment(appointmentBuilder, this))
                .commit();
    }

    @Override
    public void appointmentSelected(Appointment appointment) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

    }
}
