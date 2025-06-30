package com.cathaybk.tpi_leo_demo.service;

import com.cathaybk.tpi_leo_demo.dto.Bpi;
import com.cathaybk.tpi_leo_demo.dto.CoindeskResponse;
import com.cathaybk.tpi_leo_demo.dto.CurrencyData;
import com.cathaybk.tpi_leo_demo.dto.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CoindeskServiceTest {

    @InjectMocks
    private CoindeskService coindeskService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this); // @ExtendWith(MockitoExtension.class) handles this
    }

    @Test
    void getCoindeskData() {
        // Mock the response from RestTemplate
        CoindeskResponse mockResponse = new CoindeskResponse();
        Time time = new Time();
        time.setUpdated("Sep 1, 2024 12:00:00 UTC");
        mockResponse.setTime(time);
        mockResponse.setDisclaimer("Mock Disclaimer");
        mockResponse.setChartName("Mock Chart");

        Bpi bpi = new Bpi();
        CurrencyData usdData = new CurrencyData();
        usdData.setCode("USD");
        usdData.setRate("50000.00");
        bpi.setUsd(usdData);
        mockResponse.setBpi(bpi);

        when(restTemplate.getForObject("https://kengp3.github.io/blog/coindesk.json", CoindeskResponse.class))
                .thenReturn(mockResponse);

        // Call the service method
        CoindeskResponse result = coindeskService.getCoindeskData();

        // Assertions
        assertNotNull(result);
        assertEquals(mockResponse.getTime().getUpdated(), result.getTime().getUpdated());
        assertEquals(mockResponse.getDisclaimer(), result.getDisclaimer());
        assertEquals(mockResponse.getChartName(), result.getChartName());
        assertNotNull(result.getBpi());
        assertEquals(mockResponse.getBpi().getUsd().getCode(), result.getBpi().getUsd().getCode());
        assertEquals(mockResponse.getBpi().getUsd().getRate(), result.getBpi().getUsd().getRate());
    }
}
