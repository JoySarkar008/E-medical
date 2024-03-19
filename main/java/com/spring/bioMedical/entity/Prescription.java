package com.spring.bioMedical.entity;

import javax.persistence.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "prescription")
    private String prescription;

    @Column(name = "appointmentId")
    private int appointmentId;

    public Prescription() {
    }

    public Prescription(int id, String email, String prescription, int appointmentId) {
        this.id = id;
        this.email = email;
        this.prescription = prescription;
        this.appointmentId = appointmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}
