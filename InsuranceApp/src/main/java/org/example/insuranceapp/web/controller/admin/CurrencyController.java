package org.example.insuranceapp.web.controller.admin;

import jakarta.validation.Valid;
import org.example.insuranceapp.application.service.CurrencyService;
import org.example.insuranceapp.web.dto.metadata.CurrencyRequest;
import org.example.insuranceapp.web.dto.metadata.CurrencyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/currencies")
public class CurrencyController {
    public final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping
    public Page<CurrencyResponse> getAllCurrencies(Pageable pageable){
        return currencyService.getAllCurrencies(pageable);
    }

    @PostMapping
    public ResponseEntity<CurrencyResponse> createCurrency(@RequestBody @Valid CurrencyRequest currencyRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.createCurrency(currencyRequest));
    }

    @PatchMapping("/{currencyId}")
    public ResponseEntity<CurrencyResponse> updateCurrencyStatus(@PathVariable Long currencyId, @RequestParam boolean active){
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.updateCurrencyStatus(currencyId, active));
    }
}
