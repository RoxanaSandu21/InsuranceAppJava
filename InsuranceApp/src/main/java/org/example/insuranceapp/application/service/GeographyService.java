package org.example.insuranceapp.application.service;

import org.example.insuranceapp.application.exception.NotFoundException;
import org.example.insuranceapp.domain.geography.city.CityRepository;
import org.example.insuranceapp.domain.geography.country.CountryRepository;
import org.example.insuranceapp.domain.geography.county.CountyRepository;
import org.example.insuranceapp.web.dto.geography.CityResponse;
import org.example.insuranceapp.web.dto.geography.CountryResponse;
import org.example.insuranceapp.web.dto.geography.CountyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GeographyService {
    private final CountryRepository countryRepository;
    private final CountyRepository countyRepository;
    private final CityRepository cityRepository;

    public GeographyService(CountryRepository countryRepository, CountyRepository countyRepository, CityRepository cityRepository){
        this.countryRepository = countryRepository;
        this.countyRepository = countyRepository;
        this.cityRepository = cityRepository;
    }

    public Page<CountryResponse> getCountries(Pageable pageable){
        return countryRepository.findAll(pageable)
                .map(country -> new CountryResponse(country.getId(), country.getName()));
    }

    public Page<CountyResponse> getCounties(Long countryId, Pageable pageable){
        if (!countryRepository.existsById(countryId)) {
            throw new NotFoundException("Country with id " + countryId);
        }

        return countyRepository.findByCountryId(countryId, pageable)
                .map(county -> new CountyResponse(county.getId(), county.getName(), county.getCountry().getId()));
    }

    public Page<CityResponse> getCities(Long countyId, Pageable pageable){
        if (!countyRepository.existsById(countyId)) {
            throw new NotFoundException("County with id " + countyId);
        }
        return cityRepository.findByCountyId(countyId, pageable)
                .map(city -> new CityResponse(city.getId(), city.getName(), city.getCounty().getId()));
    }
}
