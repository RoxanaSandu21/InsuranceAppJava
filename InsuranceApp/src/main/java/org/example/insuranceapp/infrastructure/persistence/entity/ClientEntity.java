package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.validation.constraints.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.example.insuranceapp.domain.client.ClientType;

import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType type;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Identification number cannot be empty")
    private String identificationNumber;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    private String address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<BuildingEntity> buildings;

    private int numberOfChangesOfIn;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<PolicyEntity> policies;


    public ClientEntity() {
    }

    public ClientEntity(ClientType type, String name, String identificationNumber, String email, String phoneNumber, String address) {
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

    public List<BuildingEntity> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingEntity> building) {
        this.buildings = building;
    }

    public int getNumberOfChangesOfIn() {
        return numberOfChangesOfIn;
    }

    public void setNumberOfChangesOfIn(int numberOfChangesOfIN) {
        this.numberOfChangesOfIn = numberOfChangesOfIN;
    }

    public List<PolicyEntity> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyEntity> policies) {
        this.policies = policies;
    }
}

