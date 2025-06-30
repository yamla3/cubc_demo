package com.cathaybk.tpi_leo_demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Bpi {
    @JsonProperty("USD")
    private CurrencyData usd;
    @JsonProperty("GBP")
    private CurrencyData gbp;
    @JsonProperty("EUR")
    private CurrencyData eur;
}