package com.ce2006.project.server;

import android.util.Log;

import com.ce2006.project.model.Appointment;
import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;
import com.ce2006.project.model.Doctor;
import com.ce2006.project.model.Timeslot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Utility class to build appointment
 * Created by lhtan on 24/3/15.
 */
public class AppointmentBuilder {
    private Credential credential;
    private int type;
    private String city;
    private String note = "";
    private int minute, hour, date, month, year;
    private long doctor;

    public AppointmentBuilder(Credential credential) {
        this.credential = credential;
    }

    /**
     * get a list of city locations with clinics that contains specific types of doctor
     * @param type type of doctor
     * @return
     */
    public String[] getClinicLocationList(int type) {
        this.type = type;
        Request request = new JsonRequest("api/appointment/clinic_loc");
        request.data("type", String.valueOf(type));
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("locations")) {
            JSONArray array = (JSONArray) content.get("locations");
            String[] results = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                String loc = (String) array.get(i);
                results[i] = loc;
            }
            return results;
        }
        return new String[]{};
    }

    /**
     * get timeslot available for a clinics within the city
     * in the time period
     * @param city
     * @param year
     * @param month
     * @param date
     * @return
     */
    public Timeslot[] getTimeslot(String city, int year, int month, int date) {
        this.city = city;
        this.year = year;
        this.month = month;
        this.date = date;
        Request request = new JsonRequest("api/appointment/timeslot/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("type", String.valueOf(type));
        request.data("city", city);
        String dateString = String.format("%d/%d/%d", date, month, year);
        request.data("date", dateString);
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("timeslots")) {
            JSONArray array = (JSONArray) content.get("timeslots");
            Timeslot[] timeslots = new Timeslot[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String time = (String) obj.get("time");
                Long n = (Long) obj.get("n");
                timeslots[i] = new Timeslot(time, n.intValue());
            }
            return timeslots;
        }
        return new Timeslot[]{};
    }

    /**
     * get the list of doctors available for the timeslot
     * @param hour hour of timeslot
     * @param minute minute of timeslot
     * @return
     */
    public Doctor[] getDoctor(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        Request request = new JsonRequest("api/appointment/doctor/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("type", String.valueOf(type));
        request.data("city", city);
        String dateString = String.format("%d/%d/%d", date, month, year);
        String timeString = String.format("%d:%d:00", hour, minute);
        request.data("date", dateString);
        request.data("timeslot", timeString);

        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("doctors")) {
            JSONArray array = (JSONArray) content.get("doctors");
            Doctor[] doctors = new Doctor[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                doctors[i] = Doctor.parseFromJson(obj);
            }
            return doctors;
        }
        return new Doctor[]{};
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * make appointment to the doctor
     * @param doctor
     * @return
     */
    public Appointment makeAppointment(Long doctor) {
        this.doctor = doctor;
        Request request = new JsonRequest("api/appointment/make");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        String dateString = String.format("%d/%d/%d", date, month, year);
        String timeString = String.format("%d:%d:00", hour, minute);
        request.data("date", dateString);
        request.data("timeslot", timeString);
        request.data("doctor", String.valueOf(doctor));
        request.data("note", note);
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("appointment")) {
            JSONObject appointment = (JSONObject) content.get("appointment");
            return Appointment.parseFromJson(appointment);
        }
        return null;
    }

    /**
     * get list of cities containing clinic
     * @return
     */
    public String[] getCities() {
        Request request = new JsonRequest("api/location/clinic");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("cities")) {
            JSONArray array = (JSONArray) content.get("cities");
            String[] cities = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                cities[i] = (String) array.get(i);
            }
            return cities;
        }
        return new String[]{};
    }

    /**
     * get list of appointments from the user
     * @return
     */
    public Appointment[] getAppointments() {
        Request request = new JsonRequest("api/appointment/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("appointments")) {
            JSONArray array = (JSONArray) content.get("appointments");
            Appointment[] appointments = new Appointment[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject appointment = (JSONObject) array.get(i);
                appointments[i] = Appointment.parseFromJson(appointment);
            }
            return appointments;
        }
        return new Appointment[]{};
    }

    /**
     * get timeslot available for the doctor
     * @param doctor
     * @param year
     * @param month
     * @param date
     * @return
     */
    public Timeslot[] getTimeslot(Long doctor, int year, int month, int date) {
        this.doctor = doctor;
        this.year = year;
        this.month = month;
        this.date = date;
        Request request = new JsonRequest("api/appointment/timeslot/doctor/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("doctor", String.valueOf(doctor));
        String dateString = String.format("%d/%d/%d", date, month, year);
        request.data("date", dateString);
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("timeslots")) {
            JSONArray array = (JSONArray) content.get("timeslots");
            Timeslot[] timeslots = new Timeslot[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                String time = (String) obj.get("time");
                Long n = (Long) obj.get("n");
                timeslots[i] = new Timeslot(time, n.intValue());
                Log.d("Tan", "timeslots" + timeslots[i]);
            }
            return timeslots;
        }
        return new Timeslot[]{};
    }

    /**
     * get clinic within the city
     * @param city
     * @return
     */
    public Clinic[] getClinic(String city) {
        Request request = new JsonRequest("api/clinic/location");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        Log.d("Tan", "city: " + city);
        request.data("city", city);
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("clinics")) {
            JSONArray array = (JSONArray) content.get("clinics");
            Clinic[] clinics = new Clinic[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                clinics[i] = Clinic.parseFromJson(obj);
            }
            return clinics;
        }
        return new Clinic[]{};
    }

    /**
     * get doctors in the clinic
     * @param clinic
     * @return
     */
    public Doctor[] getDoctor(Long clinic) {
        Request request = new JsonRequest("api/doctor/clinic");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("clinic", String.valueOf(clinic));
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("doctors")) {
            JSONArray array = (JSONArray) content.get("doctors");
            Doctor[] doctors = new Doctor[array.size()];
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                doctors[i] = Doctor.parseFromJson(obj);
            }
            return doctors;
        }
        return new Doctor[]{};
    }

    /**
     * delete the appointment
     * @param appointment
     * @return
     */
    public boolean delete(Appointment appointment) {
        Request request = new JsonRequest("api/appointment/delete");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("id", String.valueOf(appointment.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("success")) {
            return true;
        }
        return false;
    }

    /**
     * update the appointment's time and date
     * @param appointment
     * @return
     */
    public Appointment updateAppointment(Appointment appointment) {
        Request request = new JsonRequest("api/appointment/modify");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());

        String dateString = String.format("%d/%d/%d", appointment.getDate(), appointment.getMonth(), appointment.getYear());
        String timeString = String.format("%d:%d:00", appointment.getHour(), appointment.getMinute());
        request.data("date", dateString);
        request.data("timeslot", timeString);
        request.data("id", String.valueOf(appointment.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("appointment")) {
            JSONObject jsonAppointment = (JSONObject) content.get("appointment");
            return Appointment.parseFromJson(jsonAppointment);
        }
        return null;
    }
}


