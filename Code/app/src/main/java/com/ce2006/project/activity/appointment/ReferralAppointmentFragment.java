package com.ce2006.project.activity.appointment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.ce2006.project.adapter.ClinicArrayAdapter;
import com.ce2006.project.adapter.DoctorNameArrayAdapter;
import com.ce2006.project.adapter.TimeslotArrayAdapter;
import com.ce2006.project.localstorage.PreferenceManager;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.model.Timeslot;
import com.ce2006.project.server.AppointmentBuilder;
import com.example.user.ce2006_project.R;

import java.util.Calendar;

/**
 * fragment creating appointment via referral
 * Created by lhtan on 1/4/15.
 */
public class ReferralAppointmentFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    private Button btnChooseDate, btnSubmit;
    private Spinner spinnerLocationFrom, spinnerClinicFrom, spinnerDoctorFrom, spinnerLocationTo, spinnerClinicTo, spinnerDoctorTo, spinnerTime;
    private View _progressBar;

    private AppointmentBuilder appointmentBuilder;
    private MakeAppointmentListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        PreferenceManager manager = PreferenceManager.getManager(activity);
        appointmentBuilder = new AppointmentBuilder(Credential.getCredential(manager));
        try {
            listener = (MakeAppointmentListener) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_referral, null);
        spinnerLocationFrom = (Spinner) view.findViewById(R.id.spinnerLocationFrom);
        spinnerClinicFrom = (Spinner) view.findViewById(R.id.spinnerClinicFrom);
        spinnerDoctorFrom = (Spinner) view.findViewById(R.id.spinnerDoctorFrom);
        spinnerLocationTo = (Spinner) view.findViewById(R.id.spinnerLocationTo);
        spinnerClinicTo = (Spinner) view.findViewById(R.id.spinnerClinicTo);
        spinnerDoctorTo = (Spinner) view.findViewById(R.id.spinnerDoctorTo);
        btnChooseDate = (Button) view.findViewById(R.id.btnChooseDate);
        spinnerTime = (Spinner) view.findViewById(R.id.spinnerTime);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        _progressBar = view.findViewById(R.id.progressBar);

        spinnerLocationFrom.setOnItemSelectedListener(this);
        spinnerClinicFrom.setOnItemSelectedListener(this);
        spinnerDoctorFrom.setOnItemSelectedListener(this);
        spinnerLocationTo.setOnItemSelectedListener(this);
        spinnerClinicTo.setOnItemSelectedListener(this);
        spinnerDoctorTo.setOnItemSelectedListener(this);
        spinnerTime.setOnItemSelectedListener(this);
        btnChooseDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        loadCities(spinnerLocationFrom);
        loadCities(spinnerLocationTo);


        //choose current date
        chooseToday();
        return view;
    }

    private void loadCities(final Spinner spinner) {
        AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
            @Override
            public Object execute() {
                return appointmentBuilder.getCities();
            }
        }, new AppointmentTask.AppointmentTaskListener() {
            @Override
            public void taskStart(AppointmentTask task) {
            }

            @Override
            public void taskEnd(AppointmentTask task, Object obj) {
                String[] cities = (String[]) obj;
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        });
        task.execute();
    }

    private void chooseToday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnChooseDate.setTag(calendar);
        updateDateText(year, month, day);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    Doctor doctorFrom = (Doctor) spinnerDoctorFrom.getSelectedItem();
                    String cityFrom = (String) spinnerLocationFrom.getSelectedItem();
                    Clinic clinicFrom = (Clinic) spinnerClinicFrom.getSelectedItem();
                    appointmentBuilder.setNote(new StringBuilder("Referred from ")
                            .append(doctorFrom.getName())
                            .append(" (")
                            .append(clinicFrom.getName())
                            .append(", ")
                            .append(cityFrom)
                            .append(")")
                            .toString());
                    Doctor doctor = (Doctor) spinnerDoctorTo.getSelectedItem();
                    return appointmentBuilder.makeAppointment(doctor.getId());
                }
            }, new AppointmentTask.AppointmentTaskListener() {
                @Override
                public void taskStart(AppointmentTask task) {
                    btnSubmit.setEnabled(false);
                }

                @Override
                public void taskEnd(AppointmentTask task, Object obj) {
                    com.ce2006.project.model.Appointment newAppointment = (com.ce2006.project.model.Appointment) obj;
                    if (newAppointment != null) {
                        Toast.makeText(getActivity(), "Appointment Made!", Toast.LENGTH_SHORT).show();
                        listener.appointmentMade(newAppointment);
                        //return
                    } else {
                        Toast.makeText(getActivity(), "Failed to make appointment!", Toast.LENGTH_SHORT).show();
                    }
                    btnSubmit.setEnabled(true);
                }
            });
            task.execute();
        } else if (v == btnChooseDate) {
            DialogFragment datePickerFragment = DatePickerFragment.getDatePicker(this);
            datePickerFragment.setTargetFragment(this, 0);
            Bundle bundle = new Bundle();
            final Calendar cal = (Calendar) btnChooseDate.getTag();
            bundle.putInt(DatePickerFragment.YEAR, cal.get(Calendar.YEAR));
            bundle.putInt(DatePickerFragment.MONTH, cal.get(Calendar.MONTH));
            bundle.putInt(DatePickerFragment.DAY, cal.get(Calendar.DAY_OF_MONTH));
            datePickerFragment.setArguments(bundle);
            datePickerFragment.show(getFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spinnerLocationFrom) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getClinic((String) spinnerLocationFrom.getSelectedItem());
                }
            },
                    new AppointmentTask.AppointmentTaskListener() {
                        @Override
                        public void taskStart(AppointmentTask task) {
                        }

                        @Override
                        public void taskEnd(AppointmentTask task, Object obj) {
                            Clinic[] clinicLists = (Clinic[]) obj;
                            spinnerClinicFrom.setAdapter(new ClinicArrayAdapter(getActivity(), clinicLists));
                        }
                    });
            task.execute();
        } else if (parent == spinnerClinicFrom) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getDoctor(((Clinic) spinnerClinicFrom.getSelectedItem()).getId());
                }
            }, new AppointmentTask.AppointmentTaskListener() {
                @Override
                public void taskStart(AppointmentTask task) {
                }

                @Override
                public void taskEnd(AppointmentTask task, Object obj) {
                    Doctor[] doctors = (Doctor[]) obj;
                    spinnerDoctorFrom.setAdapter(new DoctorNameArrayAdapter(getActivity(), doctors));
                }
            });
            task.execute();
        } else if (parent == spinnerDoctorFrom) {
        } else if (parent == spinnerLocationTo) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getClinic((String) spinnerLocationTo.getSelectedItem());
                }
            },
                    new AppointmentTask.AppointmentTaskListener() {
                        @Override
                        public void taskStart(AppointmentTask task) {
                        }

                        @Override
                        public void taskEnd(AppointmentTask task, Object obj) {
                            Clinic[] clinicLists = (Clinic[]) obj;
                            spinnerClinicTo.setAdapter(new ClinicArrayAdapter(getActivity(), clinicLists));
                        }
                    });
            task.execute();
        } else if (parent == spinnerClinicTo) {
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getDoctor(((Clinic) spinnerClinicTo.getSelectedItem()).getId());
                }
            }, new AppointmentTask.AppointmentTaskListener() {
                @Override
                public void taskStart(AppointmentTask task) {
                }

                @Override
                public void taskEnd(AppointmentTask task, Object obj) {
                    Doctor[] doctors = (Doctor[]) obj;
                    spinnerDoctorTo.setAdapter(new DoctorNameArrayAdapter(getActivity(), doctors));
                }
            });
            task.execute();
        } else if (parent == spinnerDoctorTo) {
            final Calendar cal = (Calendar) btnChooseDate.getTag();
            AppointmentTask task = new AppointmentTask(_progressBar, new AppointmentTask.AppointmentTaskCommand() {
                @Override
                public Object execute() {
                    return appointmentBuilder.getTimeslot(((Doctor) spinnerDoctorFrom.getSelectedItem()).getId(), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                }
            }, new AppointmentTask.AppointmentTaskListener() {
                @Override
                public void taskStart(AppointmentTask task) {
                }

                @Override
                public void taskEnd(AppointmentTask task, Object obj) {
                    Timeslot[] timeslots = (Timeslot[]) obj;
                    TimeslotArrayAdapter adapter = new TimeslotArrayAdapter(getActivity(), timeslots);
                    spinnerTime.setAdapter(adapter);
                }
            });
            task.execute();
        } else if (parent == spinnerTime) {
            Timeslot timeslot = (Timeslot) spinnerTime.getSelectedItem();
            appointmentBuilder.setHour(timeslot.getHour());
            appointmentBuilder.setMinute(timeslot.getMin());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void updateDateText(int year, int month, int day) {
        btnChooseDate.setText(new StringBuilder()
                .append(day).append("/")
                .append(month + 1).append("/")
                .append(year));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Log.d("Tan", "ondateset: " + year + "," + month + "," + day);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        btnChooseDate.setTag(c);
        updateDateText(year, month, day);
    }
}