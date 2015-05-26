package com.ce2006.project.model;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Patient object
 */
public class Patient implements Serializable {
    /**
     * Type of the patient, call via phone or email
     */
    private int contactType;
    /**
     * phone number of the patient
     */
    private String phone;
    /**
     * email of the patient
     */
    private String email;
    /**
     * name of the patient
     */
    private String name;

    private Patient() {
    }

    /**
     * Factory method to parse patient from jsonobject
     * @param jsonObject jsonObject to be parsed
     * @return patient
     */
    public static Patient parseFromJson(JSONObject jsonObject) {
        Patient patient = new Patient();
        if (jsonObject.containsKey("type"))
            patient.contactType = ((Long) jsonObject.get("type")).intValue();
        if (jsonObject.containsKey("name")) patient.name = (String) jsonObject.get("name");
        if (jsonObject.containsKey("phone")) patient.phone = (String) jsonObject.get("phone");
        if (jsonObject.containsKey("email")) patient.email = (String) jsonObject.get("email");
        return patient;
    }

    /**
     * @return {@link com.ce2006.project.model.Patient#contactType}
     */
    public int getContactType() {
        return contactType;
    }

    /**
     * @return phone number of the patient
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return email address of the patient
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return name of the patient
     */
    public String getName() {
        return name;
    }
}
