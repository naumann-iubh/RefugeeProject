package com.lorbeer.refugeeuserapp.domain;

import com.google.gson.annotations.SerializedName;

public class Contestants {

    @SerializedName("contestants_id")
    private Integer id;
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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


}
