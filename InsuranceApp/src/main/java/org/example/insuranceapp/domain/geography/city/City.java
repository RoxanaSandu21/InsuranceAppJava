package org.example.insuranceapp.domain.geography.city;

import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.geography.county.County;

import java.util.List;

public class City {

    private Long id;
    private String name;
    private County county;
    private List<Building> buildings;

    public City() {
    }

    public City(String name, County county) {
        this.name = name;
        this.county = county;
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

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}
