package com.ce2006.project.activity.appointment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ce2006.project.adapter.AppointmentArrayAdapter;
import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Appointment;
import com.ce2006.project.model.Credential;
import com.ce2006.project.server.AppointmentBuilder;
import com.example.user.ce2006_project.R;

/**
 * ChooseAppointmentActivity
 * Activity that allow user to choose appointment and return results
 * via {@link android.support.v7.app.ActionBarActivity#setResult(int, android.content.Intent)},
 *
 * @see android.support.v7.app.ActionBarActivity#onActivityResult(int, int, android.content.Intent)
 */
public class ChooseAppointmentActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static final String APPOINTMENT_EXTRA = "appointment";
    private ListView listView;

    /**
     * Fetch appointment using {@link android.os.AsyncTask} calling
     * {@link com.ce2006.project.server.AppointmentBuilder#getAppointments()}
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init layout
        setContentView(R.layout.activity_container);
        FrameLayout view = (FrameLayout) findViewById(R.id.container);
        View list = getLayoutInflater().inflate(R.layout.fragment_view_list, null);
        view.addView(list);
        listView = (ListView) list.findViewById(R.id.list);

        final View progressBar = list.findViewById(R.id.progressBar);
        final AppointmentBuilder builder = new AppointmentBuilder(Credential.getCredential(PreferenceManager.getManager(this)));
        new AsyncTask<Void, Void, Appointment[]>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Appointment[] doInBackground(Void... params) {
                return builder.getAppointments();
            }

            @Override
            protected void onPostExecute(Appointment[] appointments) {
                progressBar.setVisibility(View.INVISIBLE);
                loadAppointments(appointments);
            }
        }.execute();
    }

    /**
     * show appointments to {@link com.ce2006.project.activity.appointment.ChooseAppointmentActivity#listView}
     *
     * @param appointments
     */
    private void loadAppointments(Appointment[] appointments) {
        listView.setAdapter(new AppointmentArrayAdapter(this, appointments));
        listView.setOnItemClickListener(this);
    }

    /**
     * When any of the appointment in the list is clicked
     * return the result to the calling activity
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == listView) {
            Appointment appointment = (Appointment) listView.getAdapter().getItem(position);
            Intent result = new Intent();
            result.putExtra(APPOINTMENT_EXTRA, appointment);
            setResult(RESULT_OK, result);
            finish();
        }
    }
}
