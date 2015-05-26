package com.ce2006.project.activity.account;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.server.Particulars;
import com.example.user.ce2006_project.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fragment to allow user to register a new patient account
 * Calling activity must implement {@link com.ce2006.project.activity.account.AuthActivityListener}
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText txtName, txtPassword1, txtPassword2, txtEmail, txtContact, txtAddress, txtPostal;
    private Spinner spinnerCountry, spinnerCity;
    private Button buttonSubmit, buttonLogin;
    private Switch switchType;
    private SignUpTask particularTask = null;
    private ProgressBar progressBar;
    private AuthActivityListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.label_register);
        try {
            listener = (AuthActivityListener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, null);
        txtName = (EditText) view.findViewById(R.id.txtName);
        txtPassword1 = (EditText) view.findViewById(R.id.txtPassword1);
        txtPassword2 = (EditText) view.findViewById(R.id.txtPassword2);
        txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        txtContact = (EditText) view.findViewById(R.id.txtContact);
        txtAddress = (EditText) view.findViewById(R.id.txtAddress);
        txtPostal = (EditText) view.findViewById(R.id.txtPostal);
        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        spinnerCountry = (Spinner) view.findViewById(R.id.spinnerCountry);
        spinnerCountry.setOnItemSelectedListener(this);
        buttonSubmit = (Button) view.findViewById(R.id.btnSubmit);
        buttonSubmit.setOnClickListener(this);
        buttonLogin = (Button) view.findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        switchType = (Switch) view.findViewById(R.id.switchType);
        return view;
    }

    /**
     * Override onClick
     * if buttonSubmit is clicked
     * check all the fields to be filled and filled correctly,
     * and start {@link com.ce2006.project.activity.account.SignUpFragment.SignUpTask},
     * if buttonLogin is clicked
     * direct user to {@link com.ce2006.project.activity.account.LogInFragment}
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == buttonSubmit) {
            boolean emptyField = false;
            TextView focusTo = null;
            if (txtName.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtName;
            }
            if (txtPassword1.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtPassword1;
            }
            if (txtPassword2.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtPassword2;
            }
            if (txtEmail.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtEmail;
            }
            if (txtContact.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtContact;
            }
            if (txtAddress.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtAddress;
            }
            if (spinnerCity.getSelectedItem() == null) {
                emptyField = true;
            }
            if (txtPostal.getText().toString().isEmpty()) {
                emptyField = true;
                focusTo = txtPostal;
            }

            if (spinnerCountry.getSelectedItem() == null) {
                emptyField = true;
            }
            if (emptyField) {
                if (focusTo != null) focusTo.requestFocus();
                Toast.makeText(getActivity(), "Field should not be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            //password length at least 6 characters
            if (txtPassword1.getText().length() < 6) {
                txtPassword1.requestFocus();
                Toast.makeText(getActivity(), "Password should at least 6 characters!", Toast.LENGTH_LONG).show();
                return;
            }
            //password should be the same
            if (!txtPassword2.getText().toString().equals(txtPassword1.getText().toString())) {
                txtPassword2.requestFocus();
                Toast.makeText(getActivity(), "Password not the same!", Toast.LENGTH_LONG).show();
                return;
            }
            //check email address format
            if (!isEmailValid(txtEmail.getText().toString())) {
                txtEmail.requestFocus();
                Toast.makeText(getActivity(), "Please input a valid email address!", Toast.LENGTH_LONG).show();
                return;
            }
            //check phone number length
            int phoneNumLength = txtContact.length();
            if (phoneNumLength < 7 || phoneNumLength > 15) {
                txtContact.requestFocus();
                Toast.makeText(getActivity(), "Please input a valid contact number!", Toast.LENGTH_LONG).show();
                return;
            }
            //check postal code length
            if (txtPostal.length() < 5 || txtPostal.length() > 6) {
                txtPostal.requestFocus();
                Toast.makeText(getActivity(), "Please input a valid postal code!", Toast.LENGTH_LONG).show();
                return;
            }


            particularTask = new SignUpTask(getActivity());
            particularTask.execute(txtName.getText().toString(),
                    txtPassword1.getText().toString(),
                    txtEmail.getText().toString(),
                    txtContact.getText().toString(),
                    txtAddress.getText().toString(),
                    (String) spinnerCity.getSelectedItem(),
                    txtPostal.getText().toString(),
                    (String) spinnerCountry.getSelectedItem(),
                    switchType.isChecked() ? "1" : "0");
        } else if (v == buttonLogin) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new LogInFragment())
                    .commit();
        }
    }

    /**
     * Check if email is valid email
     *
     * @param email email to be checked
     * @return true if email is valid, false otherwise
     */
    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * load different city name according to country name
     */
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * SignUpTask
     * calls {@link com.ce2006.project.server.Particulars#createParticulars(String, String, String, String, String, String, String, String, String)}
     */
    private class SignUpTask extends AsyncTask<String, Void, Boolean> {
        private Context context;
        private String name, password;

        private SignUpTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            listener.startProgress();
            progressBar.setVisibility(View.VISIBLE);
            buttonSubmit.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length != 9) {
                return false;
            } else {
                name = params[0];
                password = params[1];
                String email = params[2];
                String contact = params[3];
                String address = params[4];
                String city = params[5];
                String postal = params[6];
                String country = params[7];
                String type = params[8];
                return Particulars.createParticulars(name, password, email, contact, country, city, address, postal, type);
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.INVISIBLE);
            listener.stopProgress();
            if (!success) {
                txtName.requestFocus();
                Toast.makeText(context, "There is already an account associated with the name!", Toast.LENGTH_LONG).show();
                buttonSubmit.setEnabled(true);
            } else {
                Toast.makeText(context, "Successfully Signup!", Toast.LENGTH_LONG).show();
                listener.endsWithResults(success, name, password, PreferenceManager.AccountType.Patient);
            }
        }
    }
}
