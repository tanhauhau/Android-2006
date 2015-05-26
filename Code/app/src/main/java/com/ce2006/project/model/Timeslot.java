package com.ce2006.project.model;

/**
 * Timeslot model
 */
public class Timeslot {
    /**
     * hour of the timeslot
     */
    private int hour;
    /**
     * minute of the timeslot
     */
    private int min;
    /**
     * string representation of the timeslot
     */
    private String string;

    public Timeslot(String time, int n) {
        String[] t = time.split(":");
        hour = Integer.parseInt(t[0]);
        min = Integer.parseInt(t[1]);
        //get representation string
        int ehour = hour, emin = min;
        emin += 30;
        if (emin >= 60) {
            emin -= 60;
            ehour += 1;
        }
        string = String.format("%d:%02d - %d:%02d (%d)", hour, min, ehour, emin, n);
    }

    /**
     * @return hour of the timeslot
     */
    public int getHour() {
        return hour;
    }

    /**
     * @return minute of the timeslot
     */
    public int getMin() {
        return min;
    }

    /**
     * @return string representation of the timeslot
     */
    @Override
    public String toString() {
        return string;
    }
}
