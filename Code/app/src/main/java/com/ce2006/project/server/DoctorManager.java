package com.ce2006.project.server;

import com.ce2006.project.model.Appointment;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;
import com.ce2006.project.model.Doctor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * utility for doctor
 * Created by lhtan on 24/3/15.
 */
public class DoctorManager {
    private Credential credential;

    public DoctorManager(Credential credential) {
        this.credential = credential;
    }

    /**
     * create a new doctor
     * @param name
     * @param type
     * @param email
     * @return
     */
    public Doctor createDoctor(String name, int type, String email) {
        Request request = new JsonRequest("api/doctor/create");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("name", name);
        request.data("type", String.valueOf(type));
        request.data("email", email);

        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("doctor")) {
            JSONObject c = (JSONObject) content.get("doctor");
            return Doctor.parseFromJson(c);
        }
        return null;
    }

    /**
     * update the doctor
     * @param doctor
     * @return
     */
    public boolean updateDoctor(Doctor doctor) {
        Request request = new JsonRequest("api/doctor/edit");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("id", String.valueOf(doctor.getId()));
        request.data("type", String.valueOf(doctor.getType()));
        JSONObject content = (JSONObject) request.execute();
        if (content == null || content.keySet().contains("error")) {
            return false;
        }
        return true;
    }

    /**
     * delete the doctor
     * @param doctor
     * @return
     */
    public boolean deleteDoctor(Doctor doctor) {
        Request request = new JsonRequest("api/doctor/delete");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("id", String.valueOf(doctor.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content == null || content.keySet().contains("error")) {
            return false;
        }
        return true;
    }

    /**
     * link the doctor to specific clinic
     * @param doctor
     * @param clinic
     * @return
     */
    public boolean linkDoctor(Doctor doctor, Clinic clinic) {
        Request request = new JsonRequest("api/doctor/link");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("doctorid", String.valueOf(doctor.getId()));
        request.data("clinicid", String.valueOf(clinic.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content == null || content.keySet().contains("error")) {
            return false;
        }
        return true;
    }

    /**
     * get a list of all doctors
     * @return
     */
    public Doctor[] getDoctorList() {
        Request request = new JsonRequest("api/doctor/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("doctors")) {
            JSONArray array = (JSONArray) content.get("doctors");
            Doctor[] doctors = new Doctor[array.size()];
            for (int i = 0; i < doctors.length; i++) {
                doctors[i] = Doctor.parseFromJson((JSONObject) array.get(i));
            }
            return doctors;
        }
        return new Doctor[]{};
    }

    /**
     * get list of all appointments from the doctor
     * @return
     */
    public Appointment[] getAppointmentList() {
        Request request = new JsonRequest("api/doctor/appointment/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("appointments")) {
            JSONArray array = (JSONArray) content.get("appointments");
            Appointment[] appointments = new Appointment[array.size()];
            for (int i = 0; i < appointments.length; i++) {
                appointments[i] = Appointment.parseFromJson((JSONObject) array.get(i));
            }
            return appointments;
        }
        return new Appointment[]{};
    }

    /**
     * check see any new appointment
     * @param time last update time
     * @return
     */
    public Appointment[] checkAppointment(Long time) {
        Request request = new JsonRequest("api/doctor/check/appointment");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("time", String.valueOf(time));
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("appointments")) {
            JSONArray array = (JSONArray) content.get("appointments");
            Appointment[] appointments = new Appointment[array.size()];
            for (int i = 0; i < appointments.length; i++) {
                appointments[i] = Appointment.parseFromJson((JSONObject) array.get(i));
            }
            return appointments;
        }
        return new Appointment[]{};
    }
}