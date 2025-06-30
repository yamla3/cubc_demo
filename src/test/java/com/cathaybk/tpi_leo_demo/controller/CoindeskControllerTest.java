package com.cathaybk.tpi_leo_demo.controller;

import com.cathaybk.tpi_leo_demo.dto.Bpi;
import com.cathaybk.tpi_leo_demo.dto.CoindeskResponse;
import com.cathaybk.tpi_leo_demo.dto.ConvertedBitcoinPriceResponse;
import com.cathaybk.tpi_leo_demo.dto.ConvertedCurrencyInfo;
import com.cathaybk.tpi_leo_demo.dto.CurrencyData;
import com.cathaybk.tpi_leo_demo.dto.CurrencyResponse;
import com.cathaybk.tpi_leo_demo.dto.Time;
import com.cathaybk.tpi_leo_demo.service.CoindeskService;
import com.cathaybk.tpi_leo_demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoindeskController.class)
public class CoindeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoindeskService coindeskService;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void testCoindeskApi() throws Exception {
        // Mock Coindesk API response
        CoindeskResponse mockResponse = new CoindeskResponse();
        Time time = new Time();
        time.setUpdated("Jun 30, 2025 12:00:00 UTC");
        mockResponse.setTime(time);
        mockResponse.setDisclaimer("This data was produced from the CoinDesk Bitcoin Price Index (USD).");
        mockResponse.setChartName("Bitcoin");

        Bpi bpi = new Bpi();
        CurrencyData usd = new CurrencyData();
        usd.setCode("USD");
        usd.setSymbol("&#36;");
        usd.setRate("50000.00");
        usd.setDescription("United States Dollar");
        usd.setRateFloat(50000.00f);
        bpi.setUsd(usd);

        mockResponse.setBpi(bpi);

        when(coindeskService.getCoindeskData()).thenReturn(mockResponse);

        // Perform GET request and validate response
        mockMvc.perform(get("/coindesk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time.updated").value("Jun 30, 2025 12:00:00 UTC"))
                .andExpect(jsonPath("$.disclaimer").value("This data was produced from the CoinDesk Bitcoin Price Index (USD)."))
                .andExpect(jsonPath("$.chartName").value("Bitcoin"))
                .andExpect(jsonPath("$.bpi.USD.code").value("USD"))
                .andExpect(jsonPath("$.bpi.USD.rate").value("50000.00"));
    }

    @Test
    public void testGetConvertedCoindesk() throws Exception {
        // Mock Coindesk API response
        CoindeskResponse mockResponse = new CoindeskResponse();
        Time time = new Time();
        time.setUpdated("Jun 30, 2025 12:00:00 UTC");
        mockResponse.setTime(time);

        Bpi bpi = new Bpi();
        CurrencyData usd = new CurrencyData();
        usd.setCode("USD");
        usd.setRate("50000.00");
        bpi.setUsd(usd);

        CurrencyData gbp = new CurrencyData();
        gbp.setCode("GBP");
        gbp.setRate("40000.00");
        bpi.setGbp(gbp);

        CurrencyData eur = new CurrencyData();
        eur.setCode("EUR");
        eur.setRate("60000.00");
        bpi.setEur(eur);

        mockResponse.setBpi(bpi);

        when(coindeskService.getCoindeskData()).thenReturn(mockResponse);

        // Mock database response
        when(currencyService.findById("USD")).thenReturn(Optional.of(new CurrencyResponse("USD", "美金", new BigDecimal("1.0"))));
        when(currencyService.findById("GBP")).thenReturn(Optional.of(new CurrencyResponse("GBP", "英鎊", new BigDecimal("0.8"))));
        when(currencyService.findById("EUR")).thenReturn(Optional.of(new CurrencyResponse("EUR", "歐元", new BigDecimal("0.9"))));

        // Perform GET request and validate response
        mockMvc.perform(get("/bitcoin-prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedTime").value("2025/06/30 20:00:00")) // 格式化後的日期，UTC+8
                .andExpect(jsonPath("$.bpi[0].code").value("USD"))
                .andExpect(jsonPath("$.bpi[0].name").value("美金"))
                .andExpect(jsonPath("$.bpi[0].rate").value(50000.00))
                .andExpect(jsonPath("$.bpi[1].code").value("GBP"))
                .andExpect(jsonPath("$.bpi[1].name").value("英鎊"))
                .andExpect(jsonPath("$.bpi[1].rate").value(40000.00))
                .andExpect(jsonPath("$.bpi[2].code").value("EUR"))
                .andExpect(jsonPath("$.bpi[2].name").value("歐元"))
                .andExpect(jsonPath("$.bpi[2].rate").value(60000.00));
    }
}


