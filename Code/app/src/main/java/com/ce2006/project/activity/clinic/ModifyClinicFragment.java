package com.ce2006.project.activity.clinic;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ce2006.project.model.Clinic;
import com.ce2006.project.server.ClinicManager;
import com.example.user.ce2006_project.R;


/**
 * fragment to modify clinic
 * Created by lhtan on 31/3/15.
 */
public class ModifyClinicFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Clinic clinic;
    private ClinicManager manager;
    private EditText txtClinicName, txtClinicContact, txtClinicAddress, txtPostalCode;
    private Spinner spinnerCity, spinnerCountry;
    private Button btnSubmit;
    private Listener listener;
    private View progressBar;

    public static ModifyClinicFragment getFragment(Clinic clinic, ClinicManager manager) {
        ModifyClinicFragment fragment = new ModifyClinicFragment();
        fragment.clinic = clinic;
        fragment.manager = manager;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (listener == null) {
            try {
                listener = (Listener) activity;
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clinic_modify, null);
        txtClinicName = (EditText) view.findViewById(R.id.txtClinicName);
        txtClinicContact = (EditText) view.findViewById(R.id.txtClinicContact);
        txtClinicAddress = (EditText) view.findViewById(R.id.txtClinicAddress);
        txtPostalCode = (EditText) view.findViewById(R.id.txtPostalCode);
        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        progressBar = view.findViewById(R.id.progressBar);


        btnSubmit.setOnClickListener(this);
        spinnerCity.setOnItemSelectedListener(this);
        spinnerCountry.setOnItemSelectedListener(this);

        //load default value
        txtClinicName.setText(clinic.getName());
        txtClinicContact.setText(clinic.getContact());
        txtClinicAddress.setText(clinic.getLocation().getAddress());
        txtPostalCode.setText(clinic.getLocation().getPostalCode());
        for (int i = 0; i < spinnerCountry.getAdapter().getCount(); i++) {
            if (((String) spinnerCountry.getItemAtPosition(i)).equals(clinic.getLocation().getCountry())) {
                spinnerCountry.setSelection(i);
                break;
            }
        }
        return view;
    }

    /**
     * When submit button is clicked,
     * update the clinic, and inform the listeners
     */
    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            updateClinic();
            listener.clinicModified(clinic);
        }
    }

    /**
     * update clinic by calling
     * {@link com.ce2006.project.server.ClinicManager#updateClinic(com.ce2006.project.model.Clinic)}
     * in {@link android.os.AsyncTask}
     */
    private void updateClinic() {
        clinic.setContact(txtClinicContact.getText().toString());
        clinic.setName(txtClinicName.getText().toString());
        clinic.getLocation().setAddress(txtClinicAddress.getText().toString());
        clinic.getLocation().setCity((String) spinnerCity.getSelectedItem());
        clinic.getLocation().setCountry((String) spinnerCountry.getSelectedItem());
        clinic.getLocation().setPostalCode(txtPostalCode.getText().toString());
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return manager.updateClinic(clinic);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerCountry) {
            String country = (String) spinnerCountry.getSelectedItem();
            String[] spinnerArray = new String[]{};
            if (country.equals("Singapore")) {
                spinnerArray = getResources().getStringArray(R.array.city_singapore);
            } else if (country.equals("Malaysia")) {
                spinnerArray = getResources().getStringArray(R.array.city_malaysia);
            } else if (country.equals("Thailand")) {
                spinnerArray = getResources().getStringArray(R.array.city_thailand);
            }
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCity.setAdapter(spinnerArrayAdapter);

            for (int i = 0; i < spinnerCity.getAdapter().getCount(); i++) {
                if (((String) spinnerCity.getItemAtPosition(i)).equals(clinic.getLocation().getCity())) {
                    spinnerCity.setSelection(i);
                    break;
                }
            }
        }
    }

    public static interface Listener {
        void clinicModified(Clinic clinic);
    }
}
