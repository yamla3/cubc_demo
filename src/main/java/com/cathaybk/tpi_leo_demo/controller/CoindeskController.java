package com.cathaybk.tpi_leo_demo.controller;

import com.cathaybk.tpi_leo_demo.dto.ConvertedBitcoinPriceResponse;
import com.cathaybk.tpi_leo_demo.dto.CoindeskResponse;
import com.cathaybk.tpi_leo_demo.dto.ConvertedCurrencyInfo;
import com.cathaybk.tpi_leo_demo.service.CoindeskService;
import com.cathaybk.tpi_leo_demo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class CoindeskController {

    @Autowired
    private CoindeskService coindeskService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/coindesk")
    public CoindeskResponse getCoindesk() {
        return coindeskService.getCoindeskData();
    }

    @GetMapping("/bitcoin-prices")
    public ConvertedBitcoinPriceResponse getConvertedCoindesk() {
        CoindeskResponse coindeskResponse = coindeskService.getCoindeskData();
        ConvertedBitcoinPriceResponse response = new ConvertedBitcoinPriceResponse();

        // 1. 設定更新時間
        String updatedTime = coindeskResponse.getTime().getUpdated();
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, uuuu HH:mm:ss z").withLocale(Locale.ENGLISH);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(updatedTime, inputFormatter);
            ZoneId taipeiZone = ZoneId.of("Asia/Taipei");
            ZonedDateTime taipeiZonedDateTime = zonedDateTime.withZoneSameInstant(taipeiZone);
            LocalDateTime localDateTimeInTaipei = taipeiZonedDateTime.toLocalDateTime();
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            response.setUpdatedTime(localDateTimeInTaipei.format(outputFormatter));
        } catch (DateTimeParseException e) {
            response.setUpdatedTime(updatedTime);
        }

        // 2. 建立 List<ConvertedCurrencyInfo>
        List<ConvertedCurrencyInfo> convertedCurrencies = new ArrayList<>();

        // 處理 USD
        convertedCurrencies.add(processCurrency("USD", coindeskResponse.getBpi().getUsd()));
        // 處理 GBP
        convertedCurrencies.add(processCurrency("GBP", coindeskResponse.getBpi().getGbp()));
        // 處理 EUR
        convertedCurrencies.add(processCurrency("EUR", coindeskResponse.getBpi().getEur()));

        response.setBpi(convertedCurrencies);
        return response;
    }

    private ConvertedCurrencyInfo processCurrency(String currencyCode, com.cathaybk.tpi_leo_demo.dto.CurrencyData data) {
        ConvertedCurrencyInfo currencyInfo = new ConvertedCurrencyInfo();
        currencyInfo.setCode(data.getCode());
        currencyInfo.setRate(new BigDecimal(data.getRate().replace(",", ""))); // 移除逗號並轉換為 BigDecimal

        currencyService.findById(currencyCode)
                .ifPresent(currency -> currencyInfo.setName(currency.getName()));

        return currencyInfo;
    }
}

