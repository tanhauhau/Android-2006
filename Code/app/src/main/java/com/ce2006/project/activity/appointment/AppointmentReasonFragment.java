package com.ce2006.project.activity.appointment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.ce2006_project.R;

/**
 * Fragment prompting user's intend on making appointment
 * 1. a new appointment
 * 2. due to referral
 * 3. follow up appointment
 * <p/>
 * the activity containing this fragment must implement AppointmentReasonFragment.Listener
 */
public class AppointmentReasonFragment extends Fragment implements View.OnClickListener {
    private Button btnReferral, btnNewAppointment, btnFollowUp;
    private Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_reason, null);
        btnReferral = (Button) view.findViewById(R.id.btnReferral);
        btnNewAppointment = (Button) view.findViewById(R.id.btnNewAppointment);
//        btnFollowUp = (Button) view.findViewById(R.id.btnFollowUp);
//        btnFollowUp.setOnClickListener(this);
        btnReferral.setOnClickListener(this);
        btnNewAppointment.setOnClickListener(this);
        return view;
    }

    /**
     * Button.onClickListener
     * Calling AppointmentReasonFragment.Listener::onChooseReason(int)
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == btnReferral) {
            listener.onChooseReason(Listener.REASON_REFERRAL);
        } else if (v == btnNewAppointment) {
            listener.onChooseReason(Listener.REASON_NEW_APPOINTMENT);
        } else if (v == btnFollowUp) {
            listener.onChooseReason(Listener.REASON_FOLLOW_UP);
        }
    }

    /**
     * Listener for the activity
     */
    public static interface Listener {
        public static final int REASON_REFERRAL = 1;
        public static final int REASON_NEW_APPOINTMENT = 2;
        public static final int REASON_FOLLOW_UP = 3;

        /**
         * @param reason the reason of the users want to make appointment
         */
        public void onChooseReason(int reason);
    }
}
