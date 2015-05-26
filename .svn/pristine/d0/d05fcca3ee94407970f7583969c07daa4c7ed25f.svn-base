package com.ce2006.project.model;

import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * Created by lhtan on 4/4/15.
 */
public class Location implements Serializable {
    /**
     * city of the location
     */
    private String city;
    /**
     * country of the location
     */
    private String country;
    /**
     * address of the location
     */
    private String address;
    /**
     * postal code of the location
     */
    private String postalCode;

    private Location() {
    }

    /**
     * factory method to return location from jsonobject
     *
     * @param jsonObject jsonobject to be parsed
     * @return location
     */
    public static Location parseFromJson(JSONObject jsonObject) {
        Location location = new Location();
        if (jsonObject.containsKey("city")) location.city = (String) jsonObject.get("city");
        if (jsonObject.containsKey("country"))
            location.country = (String) jsonObject.get("country");
        if (jsonObject.containsKey("address"))
            location.address = (String) jsonObject.get("address");
        if (jsonObject.containsKey("postalCode"))
            location.postalCode = (String) jsonObject.get("postalCode");
        return location;
    }

    /**
     * @return city of the location
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city city of the location
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return country of the locaiton
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country country of the location
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return address of the location
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address address of the location
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return postal code of the location
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode postal code of the location
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     *
     * @return string representation of the location
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", address, city, postalCode, country);
    }
}