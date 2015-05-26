package com.ce2006.project.model;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Doctor model
 */
public class Doctor implements Serializable {
    /**
     * id of the doctor
     */
    private Long id;
    /**
     * name of the doctor
     */
    private String name;
    /**
     * type of the doctor
     */
    private int type;
    /**
     * clinic where the doctors works with
     */
    private Clinic clinic;
    /**
     * string representation of the doctor
     */
    private String string;
    /**
     * email address of the doctor
     */
    private String email;

    private Doctor() {
    }

    /**
     * Factory method to get {@link com.ce2006.project.model.Doctor}
     *
     * @param jsonObject JsonObject to be parsed
     * @return doctor from the jsonobject
     */
    public static Doctor parseFromJson(JSONObject jsonObject) {
        Doctor doctor = new Doctor();
        if (jsonObject.containsKey("id")) doctor.id = (Long) jsonObject.get("id");
        if (jsonObject.containsKey("name")) doctor.name = (String) jsonObject.get("name");
        if (jsonObject.containsKey("type"))
            doctor.type = ((Long) jsonObject.get("type")).intValue();
        if (jsonObject.containsKey("clinic"))
            doctor.clinic = Clinic.parseFromJson((JSONObject) jsonObject.get("clinic"));
        if (jsonObject.containsKey("email")) doctor.email = (String) jsonObject.get("email");
        if (doctor.clinic != null) {
            doctor.string = new StringBuilder(doctor.name)
                    .append(" (")
                    .append(doctor.getClinic().getName())
                    .append(")")
                    .toString();
        } else {
            doctor.string = doctor.name;
        }
        return doctor;
    }

    /**
     * @return id of the doctor
     */
    public Long getId() {
        return id;
    }

    /**
     * @return clinic of the doctor works at
     */
    public Clinic getClinic() {
        return clinic;
    }

    /**
     * @return name of the doctor
     */
    public String getName() {
        return name;
    }

    /**
     * @return type of the doctor
     */
    public int getType() {
        return type;
    }

    /**
     * @param type type of the doctor to be set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return email of the doctor
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return string representation of the doctor
     */
    @Override
    public String toString() {
        return string;
    }
}





