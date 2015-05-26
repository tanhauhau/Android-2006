package com.ce2006.project.notify;

import android.telephony.SmsManager;

import com.ce2006.project.model.Patient;

/**
 * Implementation of Notify interface that sends out SMS to the patient
 */
public class SMSNotify implements Notify {
    @Override
    public boolean notify(Patient patient, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(patient.getPhone(), null, message, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
