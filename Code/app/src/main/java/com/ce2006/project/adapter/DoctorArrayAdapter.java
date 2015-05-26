package com.ce2006.project.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ce2006.project.model.Doctor;

/**
 * Created by lhtan on 31/3/15.
 */
public class DoctorArrayAdapter extends ArrayAdapter<Doctor> {
    public DoctorArrayAdapter(Context context, Doctor[] doctors) {
        super(context, android.R.layout.simple_list_item_1, doctors);
    }
}
