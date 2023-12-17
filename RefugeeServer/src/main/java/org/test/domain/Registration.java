package org.test.domain;

import jakarta.persistence.*;

@Entity
public class Registration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer registration_id;

    private String registered_at;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "contestants_id")
    private Contestants contestants;


    public Integer getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(Integer registration_id) {
        this.registration_id = registration_id;
    }

    public String getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(String registered_at) {
        this.registered_at = registered_at;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public Contestants getContestants() {
        return contestants;
    }

    public void setContestants(Contestants contestants) {
        this.contestants = contestants;
    }
}
