package org.example.insuranceapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.insuranceapp.domain.administrator.AdministratorRole;

@Entity
@Table(name = "administrators")
public class AdministratorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uniqueIdentifier;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdministratorRole administratorRole;

    public AdministratorEntity(){}

    public AdministratorEntity(String name, String email, AdministratorRole administratorRole) {
        this.name = name;
        this.email = email;
        this.administratorRole = administratorRole;
    }

    @PrePersist
    public void onCreate() {
        if (this.uniqueIdentifier == null) {
            this.uniqueIdentifier = java.util.UUID.randomUUID().toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AdministratorRole getAdministratorRole() {
        return administratorRole;
    }

    public void setAdministratorRole(AdministratorRole administratorRole) {
        this.administratorRole = administratorRole;
    }
}
