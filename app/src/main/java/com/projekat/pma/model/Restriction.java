package com.projekat.pma.model;

import java.util.Date;

public class Restriction {

    private Long id;

    private Date fromDate;

    private Date toDate;

    private double latFrom;

    private double lonFrom;

    private double latTo;

    private double lonTo;


    public Restriction() {
    }

    public Restriction(Long id, Date fromDate, Date toDate, double latFrom, double lonFrom,double latTo, double lonTo) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.latFrom = latFrom;
        this.lonFrom = lonFrom;
        this.latTo = latTo;
        this.lonTo = lonTo;
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


    public double getLatFrom() {
        return latFrom;
    }

    public double getLonFrom() {
        return lonFrom;
    }

    public void setLatFrom(double latFrom) {
        this.latFrom = latFrom;
    }

    public void setLonFrom(double lonFrom) {
        this.lonFrom = lonFrom;
    }

    public double getLatTo() {
        return latTo;
    }

    public double getLonTo() {
        return lonTo;
    }

    public void setLatTo(double latTo) {
        this.latTo = latTo;
    }

    public void setLonTo(double lonTo) {
        this.lonTo = lonTo;
    }
}

