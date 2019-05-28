package com.projekat.pma.model;

import java.util.Date;

public class Restriction {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private double lat;

    private double lon;

    public Restriction() {
    }

    public Restriction(Long id, Date fromDate, Date toDate, double lat, double lon) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

