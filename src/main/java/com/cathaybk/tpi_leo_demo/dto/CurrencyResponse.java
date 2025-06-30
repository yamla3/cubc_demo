package com.cathaybk.tpi_leo_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
    private String code;
    private String name;
    private BigDecimal rate;
}
