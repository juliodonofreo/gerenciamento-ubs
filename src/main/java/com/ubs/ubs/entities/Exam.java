package com.ubs.ubs.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private LocalDateTime examDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    public Exam(){

    }

    public Exam(Long id, String type, Appointment appointment, LocalDateTime examDate) {
        this.id = id;
        this.type = type;
        this.appointment = appointment;
        this.examDate = examDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}