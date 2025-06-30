package com.cathaybk.tpi_leo_demo.controller;

import com.cathaybk.tpi_leo_demo.dto.CurrencyRequest;
import com.cathaybk.tpi_leo_demo.dto.CurrencyResponse;
import com.cathaybk.tpi_leo_demo.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCurrencies() throws Exception {
        CurrencyResponse usd = new CurrencyResponse("USD", "美金", new BigDecimal("1.0"));
        CurrencyResponse gbp = new CurrencyResponse("GBP", "英鎊", new BigDecimal("0.8"));

        when(currencyService.findAll()).thenReturn(Arrays.asList(usd, gbp));

        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].name").value("美金"))
                .andExpect(jsonPath("$[0].rate").value(1.0))
                .andExpect(jsonPath("$[1].code").value("GBP"))
                .andExpect(jsonPath("$[1].name").value("英鎊"))
                .andExpect(jsonPath("$[1].rate").value(0.8));
    }

    @Test
    public void testGetCurrency() throws Exception {
        CurrencyResponse usd = new CurrencyResponse("USD", "美金", new BigDecimal("1.0"));

        when(currencyService.findById("USD")).thenReturn(Optional.of(usd));

        mockMvc.perform(get("/currencies/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("美金"))
                .andExpect(jsonPath("$.rate").value(1.0));
    }

    @Test
    public void testGetCurrencyNotFound() throws Exception {
        when(currencyService.findById("XYZ")).thenReturn(Optional.empty());

        mockMvc.perform(get("/currencies/XYZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCurrency() throws Exception {
        CurrencyRequest jpyRequest = new CurrencyRequest("JPY", "日圓", new BigDecimal("100.0"));
        CurrencyResponse jpyResponse = new CurrencyResponse("JPY", "日圓", new BigDecimal("100.0"));

        when(currencyService.save(any(CurrencyRequest.class))).thenReturn(jpyResponse);

        mockMvc.perform(post("/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jpyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JPY"))
                .andExpect(jsonPath("$.name").value("日圓"))
                .andExpect(jsonPath("$.rate").value(100.0));
    }

    @Test
    public void testUpdateCurrency() throws Exception {
        CurrencyRequest jpyRequest = new CurrencyRequest("JPY", "日幣", new BigDecimal("105.0"));
        CurrencyResponse jpyResponse = new CurrencyResponse("JPY", "日幣", new BigDecimal("105.0"));

        when(currencyService.update(eq("JPY"), any(CurrencyRequest.class))).thenReturn(jpyResponse);

        mockMvc.perform(put("/currencies/JPY")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jpyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("JPY"))
                .andExpect(jsonPath("$.name").value("日幣"))
                .andExpect(jsonPath("$.rate").value(105.0));
    }

    @Test
    public void testUpdateCurrencyNotFound() throws Exception {
        CurrencyRequest xyzRequest = new CurrencyRequest("XYZ", "未知幣別", new BigDecimal("0.0"));

        when(currencyService.update(eq("XYZ"), any(CurrencyRequest.class)))
                .thenThrow(new RuntimeException("Currency not found with code XYZ"));

        mockMvc.perform(put("/currencies/XYZ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(xyzRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCurrency() throws Exception {
        mockMvc.perform(delete("/currencies/USD"))
                .andExpect(status().isOk());
    }
}

