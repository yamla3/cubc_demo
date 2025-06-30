package com.cathaybk.tpi_leo_demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyData {
    private String code;
    private String symbol;
    private String rate;
    private String description;
    @JsonProperty("rate_float")
    private float rateFloat;
}