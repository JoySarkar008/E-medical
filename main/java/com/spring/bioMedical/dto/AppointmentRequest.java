package com.spring.bioMedical.dto;

import com.spring.bioMedical.entity.Admin;

import java.util.List;

public class AppointmentRequest {

    private String name;

    private String email;

    private String date;

    private String time;

    private String description;

    private String doctorId;

    public AppointmentRequest() {
    }

    public AppointmentRequest(String name, String email, String date, String time, String description, String id) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.description = description;
        this.doctorId = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "AppointmentRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", doctorId=" + doctorId +
                '}';
    }
}
