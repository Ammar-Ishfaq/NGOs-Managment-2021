package com.ammar.fyp.ModelClasses;

public class NGODONOR {
    private String DONOR;
    private String NGO;

    public NGODONOR() {
    }

    public NGODONOR(String NGO, String DONOR) {
        this.NGO = NGO;
        this.DONOR = DONOR;
    }

    public String getNGO() {
        return NGO;
    }

    public void setNGO(String NGO) {
        this.NGO = NGO;
    }

    public String getDONOR() {
        return DONOR;
    }

    public void setDONOR(String DONOR) {
        this.DONOR = DONOR;
    }
}
