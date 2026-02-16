package org.example.insuranceapp.web.controller.broker;

import org.example.insuranceapp.application.service.GeographyService;
import org.example.insuranceapp.web.dto.geography.CityResponse;
import org.example.insuranceapp.web.dto.geography.CountryResponse;
import org.example.insuranceapp.web.dto.geography.CountyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brokers")
public class GeographyController {
    private final GeographyService geographyService;

    public GeographyController(GeographyService geographyService){
        this.geographyService = geographyService;
    }

    @GetMapping("/countries")
    public Page<CountryResponse> getCountries(Pageable pageable){
        return geographyService.getCountries(pageable);
    }

    @GetMapping("/countries/{countryId}/counties")
    public Page<CountyResponse> getCounties(@PathVariable Long countryId, Pageable pageable){
        return geographyService.getCounties(countryId, pageable);
    }

    @GetMapping("/counties/{countyId}/cities")
    public Page<CityResponse> getCities(@PathVariable Long countyId, Pageable pageable){
        return geographyService.getCities(countyId, pageable);
    }
}
