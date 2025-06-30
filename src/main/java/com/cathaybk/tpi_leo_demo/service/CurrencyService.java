package com.cathaybk.tpi_leo_demo.service;

import com.cathaybk.tpi_leo_demo.dto.CurrencyRequest;
import com.cathaybk.tpi_leo_demo.dto.CurrencyResponse;
import com.cathaybk.tpi_leo_demo.entity.Currency;
import com.cathaybk.tpi_leo_demo.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<CurrencyResponse> findAll() {
        return currencyRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyResponse> findById(String code) {
        return currencyRepository.findById(code)
                .map(this::convertToDto);
    }

    public CurrencyResponse save(CurrencyRequest currencyRequest) {
        Currency currency = convertToEntity(currencyRequest);
        return convertToDto(currencyRepository.save(currency));
    }

    public CurrencyResponse update(String code, CurrencyRequest currencyRequest) {
        return currencyRepository.findById(code)
                .map(existingCurrency -> {
                    existingCurrency.setName(currencyRequest.getName());
                    existingCurrency.setRate(currencyRequest.getRate());
                    return convertToDto(currencyRepository.save(existingCurrency));
                })
                .orElseThrow(() -> new RuntimeException("Currency not found with code " + code)); // 這裡可以拋出自定義的例外
    }

    public void deleteById(String code) {
        currencyRepository.deleteById(code);
    }

    private CurrencyResponse convertToDto(Currency currency) {
        CurrencyResponse dto = new CurrencyResponse();
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        dto.setRate(currency.getRate());
        return dto;
    }

    private Currency convertToEntity(CurrencyRequest currencyRequest) {
        Currency currency = new Currency();
        currency.setCode(currencyRequest.getCode());
        currency.setName(currencyRequest.getName());
        currency.setRate(currencyRequest.getRate());
        return currency;
    }
}
