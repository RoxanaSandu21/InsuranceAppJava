package org.example.insuranceapp.web.dto.geography;

public record GeographyResponse (
        Long cityId,
        String city,
        Long countyId,
        String county,
        Long countryId,
        String country
        ){
}

