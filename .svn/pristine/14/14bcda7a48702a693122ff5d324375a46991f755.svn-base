package com.ce2006.project.activity.appointment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ce2006.project.adapter.DoctorArrayAdapter;
import com.ce2006.project.adapter.TimeslotArrayAdapter;
import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Appointment;
import com.ce2006.project.model.Credential;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.model.Timeslot;
import com.ce2006.project.server.AppointmentBuilder;
import com.example.user.ce2006_project.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * fragment creating a new appointment
 */
public class NewAppointmentFragment extends Fragment implements AdapterView.OnItemSelectedListener, OnClickListener, DatePickerDialog.OnDateSetListener {
    private MakeAppointmentListener listener;
    private AppointmentBuilder appointmentBuilder;

    private Spinner _spinnerType, _spinnerLocation, _spinnerTime, _spinnerDoctor;
    private Button _btnChooseDate, _btnSubmit;
    private ProgressBar _progressBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PreferenceManager manager = PreferenceManager.getManager(activity);
        Credential credential = Credential.getCredential(manager);
        appointmentBuilder = new AppointmentBuilder(credential);
        try {
            listener = (MakeAppointmentListener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_new, null);
        _spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
        _spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        _spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        _spinnerDoctor = (Spinner) view.findViewById(R.id.spinnerDoctor);

        _btnChooseDate = (Button) view.findViewById(R.id.btnChooseDate);
        _btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        _progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //spinner
        ArrayList<String> cities = new ArrayList<>();
        cities.addAll(Arrays.asList(getResources().getStringArray(R.array.city_singapore)));
        cities.addAll(Arrays.asList(getResources().getStringArray(R.array.city_malaysia)));
        cities.addAll(Arrays.asList(getResources().getStringArray(R.array.city_thailand)));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinnerLocation.setAdapter(spinnerArrayAdapter);
        //listener
        _spinnerType.setOnItemSelectedListener(this);
        _spinnerLocation.setOnItemSelectedListener(this);
        _spinnerTime.setOnItemSelectedListener(this);
        _spinnerDoctor.setOnItemSelectedListener(this);

        _btnSubmit.setOnClickListener(this);
        _btnChooseDate.setOnClickListener(this);

        //choose current date
        chooseToday();
        return view;
    }

    private void chooseToday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        _btnChooseDate.setTag(calendar);
        updateDateText(year, month, day);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        if (parent == _spinnerType) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getClinicLocationList(position);
                }
            },
                    new AppointmentTask.AppointmentTaskListener() {
                        @Override
                        public void taskStart(AppointmentTask task) {
                        }

                        @Override
                        public void taskEnd(AppointmentTask task, Object obj) {
                            String[] clinicLists = (String[]) obj;
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clinicLists);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            _spinnerLocation.setAdapter(dataAdapter);
                        }
                    });
            task.execute();
        } else if (parent == _spinnerLocation) {
            final Calendar cal = (Calendar) _btnChooseDate.getTag();
            if (cal != null) {
                AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                    @Override
                    public Object execute() {
                        return appointmentBuilder.getTimeslot((String) _spinnerLocation.getSelectedItem(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                    }
                },
                        new AppointmentTask.AppointmentTaskListener() {
                            @Override
                            public void taskStart(AppointmentTask task) {
                            }

                            @Override
                            public void taskEnd(AppointmentTask task, Object obj) {
                                Timeslot[] timeslots = (Timeslot[]) obj;
                                TimeslotArrayAdapter adapter = new TimeslotArrayAdapter(getActivity(), timeslots);
                                _spinnerTime.setAdapter(adapter);
                            }
                        });
                task.execute();
            }
        } else if (parent == _spinnerTime) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    Timeslot timeslot = (Timeslot) _spinnerTime.getSelectedItem();
                    return appointmentBuilder.getDoctor(timeslot.getHour(), timeslot.getMin());
                }
            },
                    new AppointmentTask.AppointmentTaskListener() {
                        @Override
                        public void taskStart(AppointmentTask task) {
                        }

                        @Override
                        public void taskEnd(AppointmentTask task, Object obj) {
                            Doctor[] doctors = (Doctor[]) obj;
                            DoctorArrayAdapter adapter = new DoctorArrayAdapter(getActivity(), doctors);
                            _spinnerDoctor.setAdapter(adapter);
                        }
                    });
            task.execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        if (v == _btnChooseDate) {
            DialogFragment datePickerFragment = DatePickerFragment.getDatePicker(this);
            datePickerFragment.setTargetFragment(this, 0);
            Bundle bundle = new Bundle();
            final Calendar cal = (Calendar) _btnChooseDate.getTag();
            Log.d("Tan", "calendar: " + cal);
            bundle.putInt(DatePickerFragment.YEAR, cal.get(Calendar.YEAR));
            bundle.putInt(DatePickerFragment.MONTH, cal.get(Calendar.MONTH));
            bundle.putInt(DatePickerFragment.DAY, cal.get(Calendar.DAY_OF_MONTH));
            datePickerFragment.setArguments(bundle);
            datePickerFragment.show(getFragmentManager(), "datePicker");
        } else {
            if (v == _btnSubmit) {
                AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                    @Override
                    public Appointment execute() {
                        Doctor doctor = (Doctor) _spinnerDoctor.getSelectedItem();
                        return appointmentBuilder.makeAppointment(doctor.getId());
                    }
                },
                        new AppointmentTask.AppointmentTaskListener() {
                            @Override
                            public void taskStart(AppointmentTask task) {
                                _btnSubmit.setEnabled(false);
                            }

                            @Override
                            public void taskEnd(AppointmentTask task, Object obj) {
                                Appointment newAppointment = (Appointment) obj;
                                if (newAppointment != null) {
                                    Toast.makeText(getActivity(), "Appointment Made!", Toast.LENGTH_SHORT).show();
                                    listener.appointmentMade(newAppointment);
                                    //return
                                } else {
                                    Toast.makeText(getActivity(), "Failed to make appointment!", Toast.LENGTH_SHORT).show();
                                }
                                _btnSubmit.setEnabled(true);
                            }
                        });
                task.execute();
            }
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d("Tan", "ondateset: " + year + "," + month + "," + day);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Log.d("Tan", "calendar: " + c);
        _btnChooseDate.setTag(c);
        appointmentBuilder.setDate(day);
        appointmentBuilder.setMonth(month + 1);
        appointmentBuilder.setYear(year);
        updateDateText(year, month, day);
    }

    private void updateDateText(int year, int month, int day) {
        _btnChooseDate.setText(new StringBuilder()
                .append(day).append("/")
                .append(month + 1).append("/")
                .append(year));
    }
}