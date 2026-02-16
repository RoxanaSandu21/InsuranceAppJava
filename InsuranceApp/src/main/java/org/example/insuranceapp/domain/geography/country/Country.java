package org.example.insuranceapp.domain.geography.country;

import org.example.insuranceapp.domain.geography.county.County;

import java.util.List;

public class Country {

    private Long id;
    private String name;
    private List<County> counties;

    public Country() {
    }

    public Country(String name) {
        this.name = name;
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

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> county) {
        this.counties = county;
    }
}
