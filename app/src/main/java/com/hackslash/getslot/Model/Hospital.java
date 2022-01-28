package com.hackslash.getslot.Model;

public class Hospital {
    private String name,address,feetype,sessions,vaccine;

    public Hospital(String name, String address, String feetype,String sessions,String vaccine) {
        this.name = name;
        this.address = address;
        this.feetype = feetype;
        this.sessions=sessions;
        this.vaccine=vaccine;
    }

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }
}
