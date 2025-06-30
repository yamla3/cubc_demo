package com.cathaybk.tpi_leo_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoindeskResponse {
    private Time time;
    private String disclaimer;
    private String chartName;
    private Bpi bpi;
}