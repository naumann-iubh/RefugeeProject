package org.test.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
public class Contestants {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contestants_id;

    private String name;

    private String email;

    public Integer getContestants_id() {
        return contestants_id;
    }

    public void setContestants_id(Integer contestants_id) {
        this.contestants_id = contestants_id;
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
}
