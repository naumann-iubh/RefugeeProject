package com.lorbeer.refugeeuserapp.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {

    @SerializedName("course_id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("short_description")
    private String shortDescription;
    @SerializedName("dates")
    private List<String> dates;
    @SerializedName("place")
    private String place;
    @SerializedName("max_contestants")
    private Integer maxContestants;
    @SerializedName("requirements")
    private List<String> requirements;
    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getMaxContestants() {
        return maxContestants;
    }

    public void setMaxContestants(Integer maxContestants) {
        this.maxContestants = maxContestants;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", dates=" + dates +
                ", place='" + place + '\'' +
                ", maxContestants=" + maxContestants +
                ", requirements=" + requirements +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
