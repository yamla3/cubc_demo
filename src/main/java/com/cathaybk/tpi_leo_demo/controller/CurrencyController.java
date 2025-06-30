package com.cathaybk.tpi_leo_demo.controller;

import com.cathaybk.tpi_leo_demo.dto.CurrencyRequest;
import com.cathaybk.tpi_leo_demo.dto.CurrencyResponse;
import com.cathaybk.tpi_leo_demo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public List<CurrencyResponse> getAllCurrencies() {
        return currencyService.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyResponse> getCurrency(@PathVariable String code) {
        return currencyService.findById(code)
                .map(currency -> new ResponseEntity<>(currency, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public CurrencyResponse createCurrency(@RequestBody CurrencyRequest currencyRequest) {
        return currencyService.save(currencyRequest);
    }

    @PutMapping("/{code}")
    public ResponseEntity<CurrencyResponse> updateCurrency(@PathVariable String code, @RequestBody CurrencyRequest currencyRequest) {
        try {
            CurrencyResponse updatedCurrency = currencyService.update(code, currencyRequest);
            return new ResponseEntity<>(updatedCurrency, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{code}")
    public void deleteCurrency(@PathVariable String code) {
        currencyService.deleteById(code);
    }
}
