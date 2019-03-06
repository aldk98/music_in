package com.example.aldrian.musicin2firebase.model;

/**
 * Created by Tommy on 8/12/17.
 */

public class Job {

    private String id;
    private String owner_id;
    private String musician_id;
    private String businessName;
    private String address;
    private String time;
    private String payRange;
    private String date;
    private String genre;
    private String status = WAITING;


    public static String ON_APPEAL="onAppeal";
    public static String WAITING="waiting";
    public static String ACCEPTED="accepted";
    public static String DENIED="denied";
    public static String SEEN_BY_MUSICIAN="seenByMusician";



    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getMusician_id() {
        return musician_id;
    }
    public void setMusician_id(String musician_id) {
        this.musician_id = musician_id;
    }

    public String getOwner_id() {
        return owner_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPayRange() {
        return payRange;
    }
    public void setPayRange(String payRange) {
        this.payRange = payRange;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


}
