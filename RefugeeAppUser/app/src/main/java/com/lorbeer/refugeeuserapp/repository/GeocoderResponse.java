package com.lorbeer.refugeeuserapp.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GeocoderResponse {
    @Override
    public String toString() {
        return "GeocoderResponse{" +
                "placeId=" + placeId +
                ", licence='" + licence + '\'' +
                ", osmType='" + osmType + '\'' +
                ", osmId=" + osmId +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", _class='" + _class + '\'' +
                ", type='" + type + '\'' +
                ", placeRank=" + placeRank +
                ", importance=" + importance +
                ", addresstype='" + addresstype + '\'' +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", address=" + address +
                ", boundingbox=" + boundingbox +
                '}';
    }

    @SerializedName("place_id")
    @Expose
    private Integer placeId;
    @SerializedName("licence")
    @Expose
    private String licence;
    @SerializedName("osm_type")
    @Expose
    private String osmType;
    @SerializedName("osm_id")
    @Expose
    private Long osmId;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("place_rank")
    @Expose
    private Integer placeRank;
    @SerializedName("importance")
    @Expose
    private Float importance;
    @SerializedName("addresstype")
    @Expose
    private String addresstype;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("boundingbox")
    @Expose
    private List<String> boundingbox;

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public Long getOsmId() {
        return osmId;
    }

    public void setOsmId(Long osmId) {
        this.osmId = osmId;
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

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPlaceRank() {
        return placeRank;
    }

    public void setPlaceRank(Integer placeRank) {
        this.placeRank = placeRank;
    }

    public Float getImportance() {
        return importance;
    }

    public void setImportance(Float importance) {
        this.importance = importance;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(List<String> boundingbox) {
        this.boundingbox = boundingbox;
    }

    private class Address implements Serializable {

        @SerializedName("house_number")
        @Expose
        private String houseNumber;
        @SerializedName("road")
        @Expose
        private String road;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;
        @SerializedName("town")
        @Expose
        private String town;
        @SerializedName("municipality")
        @Expose
        private String municipality;
        @SerializedName("county")
        @Expose
        private String county;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("ISO3166-2-lvl4")
        @Expose
        private String iSO31662Lvl4;
        @SerializedName("postcode")
        @Expose
        private String postcode;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("country_code")
        @Expose
        private String countryCode;

        private String getHouseNumber() {
            return houseNumber;
        }

        private void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        private String getRoad() {
            return road;
        }

        private void setRoad(String road) {
            this.road = road;
        }

        private String getNeighbourhood() {
            return neighbourhood;
        }

        private void setNeighbourhood(String neighbourhood) {
            this.neighbourhood = neighbourhood;
        }

        private String getTown() {
            return town;
        }

        private void setTown(String town) {
            this.town = town;
        }

        private String getMunicipality() {
            return municipality;
        }

        private void setMunicipality(String municipality) {
            this.municipality = municipality;
        }

        private String getCounty() {
            return county;
        }

        private void setCounty(String county) {
            this.county = county;
        }

        private String getState() {
            return state;
        }

        private void setState(String state) {
            this.state = state;
        }

        private String getiSO31662Lvl4() {
            return iSO31662Lvl4;
        }

        private void setiSO31662Lvl4(String iSO31662Lvl4) {
            this.iSO31662Lvl4 = iSO31662Lvl4;
        }

        private String getPostcode() {
            return postcode;
        }

        private void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        private String getCountry() {
            return country;
        }

        private void setCountry(String country) {
            this.country = country;
        }

        private String getCountryCode() {
            return countryCode;
        }

        private void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }
}
