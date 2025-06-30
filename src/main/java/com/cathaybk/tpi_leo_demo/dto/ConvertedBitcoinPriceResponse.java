package com.cathaybk.tpi_leo_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertedBitcoinPriceResponse {
    private String updatedTime;
    private Map<String, Map<String, Object>> bpi;
}
