package com.cathaybk.tpi_leo_demo.controller;

import com.cathaybk.tpi_leo_demo.dto.ConvertedBitcoinPriceResponse;
import com.cathaybk.tpi_leo_demo.dto.CoindeskResponse;
import com.cathaybk.tpi_leo_demo.service.CoindeskService;
import com.cathaybk.tpi_leo_demo.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        // 2. 建立 BPI Map
        Map<String, Map<String, Object>> bpi = new HashMap<>();
        // 處理 USD
        processCurrency(bpi, "USD", coindeskResponse.getBpi().getUsd());
        // 處理 GBP
        processCurrency(bpi, "GBP", coindeskResponse.getBpi().getGbp());
        // 處理 EUR
        processCurrency(bpi, "EUR", coindeskResponse.getBpi().getEur());
        response.setBpi(bpi);
        return response;
    }

    private void processCurrency(Map<String, Map<String, Object>> bpiMap, String currencyCode, com.cathaybk.tpi_leo_demo.dto.CurrencyData data) {
        Map<String, Object> currencyInfo = new HashMap<>();
        currencyInfo.put("code", data.getCode());
        currencyInfo.put("rate", data.getRate());

        currencyService.findById(currencyCode)
                .ifPresent(currency -> currencyInfo.put("name", currency.getName()));
        bpiMap.put(currencyCode, currencyInfo);
    }
}

