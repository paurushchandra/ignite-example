package com.example.model;

import java.io.Serializable;

/**
 * Created by user on 19/4/16.
 */
public class SecurityDetail implements Serializable {

    private String sedol;
    private Sector sector;

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
