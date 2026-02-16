package org.example.insuranceapp.domain.client;

import jakarta.validation.constraints.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.example.insuranceapp.domain.building.Building;
import org.example.insuranceapp.domain.policy.Policy;

import java.util.List;

public class Client {
    private Long id;
    private ClientType type;
    private String name;
    private String identificationNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private List<Building> buildings;
    private int numberOfChangesOfIn;
    private List<Policy> policies;

    public Client() {
    }

    public Client(ClientType type, String name, String identificationNumber, String email, String phoneNumber, String address) {
        this.type = type;
        this.name = name;
        this.identificationNumber = identificationNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> building) {
        this.buildings = building;
    }

    public int getNumberOfChangesOfIn() {
        return numberOfChangesOfIn;
    }

    public void setNumberOfChangesOfIn(int numberOfChangesOfIN) {
        this.numberOfChangesOfIn = numberOfChangesOfIN;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
}
