package com.ce2006.project.model;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Clinic model
 */
public class Clinic implements Serializable {
    /**
     * id of the clinic
     */
    private Long id;
    /**
     * name of the clinic
     */
    private String name;
    /**
     * contact of the clinic
     */
    private String contact;
    /**
     * location of the clinic
     */
    private Location location;

    private Clinic() {
    }

    /**
     * factory method to produce the clinic from jsonobject
     *
     * @param jsonObject
     * @return
     */
    public static Clinic parseFromJson(JSONObject jsonObject) {
        Clinic clinic = new Clinic();
        if (jsonObject.containsKey("id")) clinic.id = (Long) jsonObject.get("id");
        if (jsonObject.containsKey("name")) clinic.name = (String) jsonObject.get("name");
        if (jsonObject.containsKey("contact")) clinic.contact = (String) jsonObject.get("contact");
        if (jsonObject.containsKey("location")) {
            JSONObject l = (JSONObject) jsonObject.get("location");
            Location location = Location.parseFromJson(l);
            clinic.location = location;
        }
        return clinic;
    }

    /**
     * @return id of the clinic
     */
    public Long getId() {
        return id;
    }

    /**
     * @return name of the clinic
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name of the clinic
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return contact of the clinic
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact contact of the clinic
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return location of the clinic
     */
    public Location getLocation() {
        return location;
    }
}
