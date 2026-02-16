package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.building.RiskIndicator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "buildings")
public class BuildingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(nullable = false)
    @NotBlank(message = "Street cannot be empty")
    private String street;

    @Column(nullable = false)
    @Positive
    private int number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    private int constructionYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuildingType type;

    private int numberOfFloors;

    private Long surfaceArea;

    @Column(nullable = false)
    @DecimalMin("0.01")
    private BigDecimal insuredValue;

    @Enumerated(EnumType.STRING)
    private RiskIndicator riskIndicator;

    @OneToMany(mappedBy = "building")
    private List<PolicyEntity> policies;


    public BuildingEntity() {
    }

    public BuildingEntity(ClientEntity client, String street, int number, CityEntity city, int constructionYear, BuildingType type, int numberOfFloors, Long surfaceArea, BigDecimal insuredValue, RiskIndicator riskIndicator) {
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

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
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

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
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

    public List<PolicyEntity> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PolicyEntity> policies) {
        this.policies = policies;
    }
}
