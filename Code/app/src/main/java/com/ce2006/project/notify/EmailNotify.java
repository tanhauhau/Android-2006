package com.ce2006.project.notify;

import android.content.Context;
import android.content.Intent;

import com.ce2006.project.model.Patient;

/**
 * Implementation of Notify that sends out email to the user
 */
public class EmailNotify implements Notify {

    private Context context;

    public EmailNotify(Context context) {
        this.context = context;
    }

    @Override
    public boolean notify(Patient patient, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, patient.getEmail());
            intent.putExtra(Intent.EXTRA_SUBJECT, "Reminder");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
