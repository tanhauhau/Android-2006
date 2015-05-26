package com.ce2006.project.server;

import com.ce2006.project.model.Clinic;
import com.ce2006.project.model.Credential;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * utility class to create, delete, modify and get clinic
 * Created by lhtan on 24/3/15.
 */
public class ClinicManager {
    private Credential credential;

    public ClinicManager(Credential credential) {
        this.credential = credential;
    }

    /**
     * create a new clinic
     * @param city
     * @param country
     * @param address
     * @param postal
     * @param name
     * @param contact
     * @return
     */
    public Clinic createClinic(String city, String country, String address, String postal, String name, String contact) {
        Request request = new JsonRequest("api/clinic/create");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("city", city);
        request.data("country", country);
        request.data("address", address);
        request.data("postal", postal);
        request.data("name", name);
        request.data("contact", contact);
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("clinic")) {
            JSONObject c = (JSONObject) content.get("clinic");
            return Clinic.parseFromJson(c);
        }
        return null;
    }

    /**
     * update the clinic
     * @param clinic
     * @return
     */
    public boolean updateClinic(Clinic clinic) {
        Request request = new JsonRequest("api/clinic/edit");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("clinicid", String.valueOf(clinic.getId()));
        request.data("city", clinic.getLocation().getCity());
        request.data("country", clinic.getLocation().getCountry());
        request.data("address", clinic.getLocation().getAddress());
        request.data("postal", clinic.getLocation().getPostalCode());
        request.data("name", clinic.getName());
        request.data("contact", clinic.getContact());
        JSONObject content = (JSONObject) request.execute();
        if (content == null || content.keySet().contains("error")) {
            return false;
        }
        return true;
    }

    /**
     * delete clinic
     * @param clinic
     * @return
     */
    public boolean deleteClinic(Clinic clinic) {
        Request request = new JsonRequest("api/clinic/delete");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("id", String.valueOf(clinic.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content == null || content.keySet().contains("error")) {
            return false;
        }
        return true;
    }

    /**
     * get stats of the clinic
     * @param clinic
     * @return
     */
    public int[] getClinicStat(Clinic clinic) {
        Request request = new JsonRequest("api/clinic/stat");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        request.data("id", String.valueOf(clinic.getId()));
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("stats")) {
            JSONObject obj = (JSONObject) content.get("stats");
            return new int[]{((Long) obj.get("doctor")).intValue(),
                    ((Long) obj.get("appointment")).intValue()};
        }
        return new int[]{};
    }

    /**
     * get list of all clinics
     * @return
     */
    public Clinic[] getClinicList() {
        Request request = new JsonRequest("api/clinic/list");
        request.data("username", credential.getUsername());
        request.data("password", credential.getPassword());
        JSONObject content = (JSONObject) request.execute();
        if (content != null && content.keySet().contains("clinics")) {
            JSONArray array = (JSONArray) content.get("clinics");
            Clinic[] clinics = new Clinic[array.size()];
            for (int i = 0; i < clinics.length; i++) {
                clinics[i] = Clinic.parseFromJson((JSONObject) array.get(i));
            }
            return clinics;
        }
        return new Clinic[]{};
    }
}