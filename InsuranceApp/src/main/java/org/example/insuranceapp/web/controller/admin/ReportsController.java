package org.example.insuranceapp.web.controller.admin;

import org.example.insuranceapp.application.service.ReportsService;
import org.example.insuranceapp.domain.building.BuildingType;
import org.example.insuranceapp.domain.policy.PolicyStatus;
import org.example.insuranceapp.infrastructure.report.ReportGrouping;
import org.example.insuranceapp.web.dto.report.ReportDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportsController {
    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService){
        this.reportsService = reportsService;
    }

    @GetMapping("/policies-by-country")
    public List<ReportDto> getPoliciesByCountry(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @RequestParam(required = false) PolicyStatus status, @RequestParam(required = false) Long currencyId, @RequestParam(required = false) BuildingType buildingType){
        return reportsService.getReport(ReportGrouping.COUNTRY, from, to, status, currencyId, buildingType);
    }

    @GetMapping("/policies-by-county")
    public List<ReportDto> getPoliciesByCounty(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @RequestParam(required = false) PolicyStatus status, @RequestParam(required = false) Long currencyId, @RequestParam(required = false) BuildingType buildingType){
        return reportsService.getReport(ReportGrouping.COUNTY, from, to, status, currencyId, buildingType);
    }

    @GetMapping("/policies-by-city")
    public List<ReportDto> getPoliciesByCity(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @RequestParam(required = false) PolicyStatus status, @RequestParam(required = false) Long currencyId, @RequestParam(required = false) BuildingType buildingType){
        return reportsService.getReport(ReportGrouping.CITY, from, to, status, currencyId, buildingType);
    }

    @GetMapping("/policies-by-broker")
    public List<ReportDto> getPoliciesByBroker(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to, @RequestParam(required = false) PolicyStatus status, @RequestParam(required = false) Long currencyId, @RequestParam(required = false) BuildingType buildingType){
        return reportsService.getReport(ReportGrouping.BROKER, from, to, status, currencyId, buildingType);
    }


}
