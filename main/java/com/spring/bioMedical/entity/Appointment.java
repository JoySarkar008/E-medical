package com.spring.bioMedical.entity;

import javax.persistence.*;

import com.spring.bioMedical.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "app")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "is_visiting")
    private Boolean isVisiting = false;

    @Column(name = "is_prescribed")
    private Boolean isPrescribed = false;

    @Column(name = "visited_time")
    private LocalDateTime visitedTime;

    @Column(name = "description")
    private String description;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "serial_no")
    private int serialNo;

    @Column(name = "regtime")
    @Transient
    private String regtime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public Boolean getPrescribed() {
        return isPrescribed;
    }

    public void setPrescribed(Boolean prescribed) {
        isPrescribed = prescribed;
    }

    public Boolean getVisiting() {
        return isVisiting;
    }

    public void setVisiting(Boolean visiting) {
        isVisiting = visiting;
    }

    public LocalDateTime getVisitedTime() {
        return visitedTime;
    }

    public void setVisitedTime(LocalDateTime visitedTime) {
        this.visitedTime = visitedTime;
    }

    public Long getDoctorId() {
        return doctorId;
    }


    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getRegtime() {
        return regtime;
    }


    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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


    @Override
    public String toString() {
        return "Appointment [id=" + id + ", name=" + name + ", email=" + email + ", date=" + date + ", time=" + time
                + ", description=" + description + "]";
    }


}