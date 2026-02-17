package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.geography.city.City;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.domain.geography.country.Country;
import org.example.insuranceapp.domain.geography.country.CountryRepository;
import org.example.insuranceapp.domain.geography.county.County;
import org.example.insuranceapp.domain.geography.county.CountyRepository;
import org.example.insuranceapp.web.dto.geography.CityResponse;
import org.example.insuranceapp.web.dto.geography.CountryResponse;
import org.example.insuranceapp.web.dto.geography.CountyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeographyServiceUnitTest {

    @Mock private CountryRepository countryRepository;
    @Mock private CountyRepository countyRepository;
    @Mock private CityRepository cityRepository;

    @InjectMocks private GeographyService geographyService;

    @Test
    void getCountries_Success() {
        Country c = new Country("Romania");
        c.setId(1L);
        when(countryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(c)));

        Page<CountryResponse> result = geographyService.getCountries(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Romania", result.getContent().get(0).name());
    }

    @Test
    void getCounties_Success() {
        Country country = new Country("Romania");
        country.setId(1L);
        County county = new County("Cluj", country);
        county.setId(10L);

        when(countryRepository.existsById(1L)).thenReturn(true);
        when(countyRepository.findByCountryId(eq(1L), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(county)));

        Page<CountyResponse> result = geographyService.getCounties(1L, Pageable.unpaged());

        assertEquals("Cluj", result.getContent().get(0).name());
        assertEquals(1L, result.getContent().get(0).countryId());
    }

    @Test
    void getCounties_CountryNotFound() {
        Pageable pageable = Pageable.unpaged();
        when(countryRepository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> geographyService.getCounties(99L, pageable));
    }

    @Test
    void getCities_Success() {
        County county = new County("Cluj", new Country("Romania"));
        county.setId(10L);
        City city = new City("Cluj-Napoca", county);
        city.setId(100L);

        when(countyRepository.existsById(10L)).thenReturn(true);
        when(cityRepository.findByCountyId(eq(10L), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(city)));

        Page<CityResponse> result = geographyService.getCities(10L, Pageable.unpaged());

        assertEquals("Cluj-Napoca", result.getContent().get(0).name());
    }

    @Test
    void getCities_CountyNotFound() {
        Pageable pageable = Pageable.unpaged();
        when(countyRepository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> geographyService.getCities(99L, pageable));
    }
}