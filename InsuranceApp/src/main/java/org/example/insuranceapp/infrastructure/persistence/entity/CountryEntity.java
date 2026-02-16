package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<CountyEntity> counties;

    public CountryEntity() {
    }

    public CountryEntity(String name) {
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

    public List<CountyEntity> getCounties() {
        return counties;
    }

    public void setCounties(List<CountyEntity> county) {
        this.counties = county;
    }
}

