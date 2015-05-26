package com.ce2006.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ce2006.project.model.Clinic;
import com.example.user.ce2006_project.R;

/**
 * Created by lhtan on 31/3/15.
 */
public class ClinicListArrayAdapter extends ArrayAdapter<Clinic> {
    public ClinicListArrayAdapter(Context context, Clinic[] clinics) {
        super(context, R.layout.list_item_clinic, clinics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_clinic, null);
            Holder holder = new Holder();
            holder.txtClinicName = (TextView) convertView.findViewById(R.id.txtClinicName);
            holder.txtClinicContact = (TextView) convertView.findViewById(R.id.txtClinicContact);
            holder.txtClinicAddress = (TextView) convertView.findViewById(R.id.txtClinicAddress);
            convertView.setTag(holder);
        }
        Clinic clinic = getItem(position);
        Holder holder = (Holder) convertView.getTag();
        holder.txtClinicName.setText(clinic.getName());
        holder.txtClinicContact.setText(clinic.getContact());
        holder.txtClinicAddress.setText(clinic.getLocation().toString());
        return convertView;
    }

    static class Holder {
        private TextView txtClinicName;
        private TextView txtClinicContact;
        private TextView txtClinicAddress;
    }
}
