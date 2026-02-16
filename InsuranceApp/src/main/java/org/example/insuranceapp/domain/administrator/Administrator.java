package org.example.insuranceapp.domain.administrator;

public class Administrator {

    private Long id;
    private String uniqueIdentifier;
    private String name;
    private String email;
    private AdministratorRole administratorRole;

    public Administrator(){}

    public Administrator(String name, String email, AdministratorRole administratorRole) {
        this.name = name;
        this.email = email;
        this.administratorRole = administratorRole;
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
