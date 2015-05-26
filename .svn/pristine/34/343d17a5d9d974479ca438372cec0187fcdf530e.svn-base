package com.ce2006.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ce2006.project.model.Appointment;
import com.example.user.ce2006_project.R;

/**
 * Created by lhtan on 31/3/15.
 */
public class AppointmentArrayAdapter extends ArrayAdapter<Appointment> {
    public AppointmentArrayAdapter(Context context, Appointment[] appointments) {
        super(context, R.layout.list_item_appointment, appointments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Appointment appointment = getItem(position);
        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_appointment, null);
            Holder holder = new Holder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtClinicName = (TextView) convertView.findViewById(R.id.txtClinicName);
            convertView.setTag(holder);
        }
        Holder holder = (Holder) convertView.getTag();
        holder.txtDate.setText(appointment.getDateString());
        holder.txtTime.setText(appointment.getTimeString());
        holder.txtClinicName.setText(appointment.getClinicName());
        return convertView;
    }

    static class Holder {
        private TextView txtDate, txtTime, txtClinicName;
    }
}
