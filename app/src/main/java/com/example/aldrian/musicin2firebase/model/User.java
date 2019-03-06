package com.example.aldrian.musicin2firebase.model;

/**
 * Created by Tommy on 7/12/17.
 */

public class User {

    private String id;
    private String role;
    private String name;
    private String email;
    private String phone;
    private String password;
    public static String MUSICIAN_ROLE="musician";
    public static String OWNER_ROLE="owner";

    public User(String role, String name, String email, String phone, String password) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole(){ return role;}
    public void setRole(String role){
        this.role = role;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone(){
        return phone ;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
