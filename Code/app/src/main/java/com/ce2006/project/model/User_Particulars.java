package com.ce2006.project.model;

/**
 * Particulars of the user
 * Created by GraceChristina on 24/2/2015.
 */
public class User_Particulars {
    /**
     * name of the user
     */
    private String name;
    /**
     * contact type of the user
     */
    private boolean contact_type; // email -> true; sms -> false;
    /**
     * contact number of the user
     */
    private String contact_number;
    /**
     * email address of the user
     */
    private String email;
    /**
     * location address of the user
     */
    private String address, postal, city, country;

    public User_Particulars(String name, boolean contact_type, String contact_number, String email, String address, String postal, String city, String country) {
        this.name = name;
        this.contact_type = contact_type;
        this.contact_number = contact_number;
        this.email = email;
        this.address = address;
        this.postal = postal;
        this.city = city;
        this.country = country;
    }

    /**
     * @return name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * @return contact type of the user
     */
    public boolean isContactType() {
        return contact_type;
    }

    /**
     * @return contact number of the user
     */
    public String getContactNumber() {
        return contact_number;
    }

    /**
     * @return email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return address of the user
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address address of the user
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return postal code of the user
     */
    public String getPostal() {
        return postal;
    }

    /**
     * @param postal postal code of the user
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    /**
     * @return city of the user
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city city of the user
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return country of the user
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country country of the user
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @param contact_type contact type of the user
     */
    public void setContact_type(boolean contact_type) {
        this.contact_type = contact_type;
    }

    /**
     * @param contact_number contact type of the user
     */
    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
