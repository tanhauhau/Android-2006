package com.ce2006.project.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ce2006.project.model.Appointment;
import com.example.user.ce2006_project.R;

/**
 * activity containing fragments which make appointment
 */
public class MakeAppointmentActivity extends ActionBarActivity implements AppointmentReasonFragment.Listener, MakeAppointmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("main")
                .add(R.id.container, new AppointmentReasonFragment())
                .commit();
    }

    /**
     * Handle when {@link com.ce2006.project.activity.appointment.AppointmentReasonFragment return results}
     *
     * @param reason the reason of the users want to make appointment
     */
    @Override
    public void onChooseReason(int reason) {
        switch (reason) {
            case REASON_REFERRAL:
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack("refer")
                        .replace(R.id.container, new ReferralAppointmentFragment())
                        .commit();
                break;
            case REASON_NEW_APPOINTMENT:
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack("new")
                        .replace(R.id.container, new NewAppointmentFragment())
                        .commit();
                break;
            case REASON_FOLLOW_UP:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * start {@link com.ce2006.project.activity.appointment.ViewModifyAppointmentActivity}
     * with {@code appointment} put as extra in the intent
     *
     * @param appointment appointment used to start the activity
     */
    @Override
    public void appointmentMade(Appointment appointment) {
        Intent intent = new Intent(this, ViewModifyAppointmentActivity.class);
        intent.putExtra("appointment", appointment);
        startActivity(intent);
        finish();
    }
}
