package com.lorbeer.refugeeapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Course {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("shortDescription")
    private String shortDescription;
    @JsonProperty("dates")
    private List<String> dates;
    @JsonProperty("place")
    private String place;
    @JsonProperty("maxContestants")
    private Integer maxContestants;
    @JsonProperty("requirements")
    private List<String> requirements;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("shortDescription")
    public String getShortDescription() {
        return shortDescription;
    }
    @JsonProperty("shortDescription")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    @JsonProperty("dates")
    public List<String> getDates() {
        return dates;
    }
    @JsonProperty("dates")
    public void setDates(List<String> dates) {
        this.dates = dates;
    }
    @JsonProperty("place")
    public String getPlace() {
        return place;
    }
    @JsonProperty("place")
    public void setPlace(String place) {
        this.place = place;
    }
    @JsonProperty("maxContestants")
    public Integer getMaxContestants() {
        return maxContestants;
    }
    @JsonProperty("maxContestants")
    public void setMaxContestants(Integer maxContestants) {
        this.maxContestants = maxContestants;
    }
    @JsonProperty("requirements")
    public List<String> getRequirements() {
        return requirements;
    }
    @JsonProperty("requirements")
    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
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
                '}';
    }
}
