package org.example.insuranceapp.domain.geography.county;

import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.city.City;

import java.util.List;

public class County {

    private Long id;
    private String name;
    private Country country;
    private List<City> cities;

    public County() {
    }

    public County(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> city) {
        this.cities = city;
    }
}
