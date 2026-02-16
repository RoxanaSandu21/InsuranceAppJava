package org.example.insuranceapp.domain.building;

import org.example.insuranceapp.domain.client.Client;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.policy.Policy;

import java.math.BigDecimal;
import java.util.List;

public class Building {

    private Long id;
    private Client client;
    private String street;
    private int number;
    private City city;
    private int constructionYear;
    private BuildingType type;
    private int numberOfFloors;
    private Long surfaceArea;
    private BigDecimal insuredValue;
    private RiskIndicator riskIndicator;
    private List<Policy> policies;

    public Building() {
    }

    public Building(Client client, String street, int number, City city, int constructionYear, BuildingType type, int numberOfFloors, Long surfaceArea, BigDecimal insuredValue, RiskIndicator riskIndicator) {
        this.client = client;
        this.street = street;
        this.number = number;
        this.city = city;
        this.constructionYear = constructionYear;
        this.type = type;
        this.numberOfFloors = numberOfFloors;
        this.surfaceArea = surfaceArea;
        this.insuredValue = insuredValue;
        this.riskIndicator = riskIndicator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(int constructionYear) {
        this.constructionYear = constructionYear;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public Long getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(Long surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public BigDecimal getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(BigDecimal insuredValue) {
        this.insuredValue = insuredValue;
    }

    public RiskIndicator getRiskIndicator() {
        return riskIndicator;
    }

    public void setRiskIndicator(RiskIndicator riskIndicator) {
        this.riskIndicator = riskIndicator;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
}
