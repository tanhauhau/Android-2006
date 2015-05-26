package com.ce2006.project.activity.appointment;

import android.os.AsyncTask;
import android.view.View;

/**
 * SubClass of AsyncTask that does any task related to appointment
 * Using async task will spawn another thread to do network related stuff
 * which will not block the UI thread
 */
class AppointmentTask extends AsyncTask<Void, Void, Object> {
    private View _progressBar;
    private AppointmentTaskCommand command;
    private AppointmentTaskListener listener;

    /**
     * Constructor for AppointmentTask
     *
     * @param _progressBar progressBar view which will set to visible when the task starts and invisible when the task ends,
     *                     optionally can accept null if no progressBar needed
     * @param command      AppointmentTaskCommand which takes in the command for the task
     * @param listener     AppointmentTaskListener which tells when the task starts and ends,
     *                     and provide the output from the command as a parameter
     */
    AppointmentTask(View _progressBar,
                    AppointmentTaskCommand command,
                    AppointmentTaskListener listener) {
        this._progressBar = _progressBar;
        this.command = command;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.taskStart(this);
        if (_progressBar != null) _progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Void... params) {
        return command.execute();
    }

    @Override
    protected void onPostExecute(Object obj) {
        listener.taskEnd(this, obj);
        if (_progressBar != null) _progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * implement execute() function to do work in AppointmentTask and
     * return whatever result
     */
    public interface AppointmentTaskCommand {
        public Object execute();
    }

    /**
     * Implement taskStart to get to know when the task starts
     * and implement taskEnd to get the results from execute() in appointmentTaskCommand
     */
    public interface AppointmentTaskListener {
        public void taskStart(AppointmentTask task);

        public void taskEnd(AppointmentTask task, Object obj);
    }
}
