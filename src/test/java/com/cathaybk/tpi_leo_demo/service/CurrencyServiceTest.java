package com.cathaybk.tpi_leo_demo.service;

import com.cathaybk.tpi_leo_demo.dto.CurrencyRequest;
import com.cathaybk.tpi_leo_demo.dto.CurrencyResponse;
import com.cathaybk.tpi_leo_demo.entity.Currency;
import com.cathaybk.tpi_leo_demo.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency usdCurrency;
    private Currency gbpCurrency;
    private CurrencyRequest usdRequest;
    private CurrencyResponse usdResponse;

    @BeforeEach
    void setUp() {
        usdCurrency = new Currency("USD", "美金", new BigDecimal("1.0"));
        gbpCurrency = new Currency("GBP", "英鎊", new BigDecimal("0.8"));
        usdRequest = new CurrencyRequest("USD", "美金", new BigDecimal("1.0"));
        usdResponse = new CurrencyResponse("USD", "美金", new BigDecimal("1.0"));
    }

    @Test
    void findAll() {
        when(currencyRepository.findAll()).thenReturn(Arrays.asList(usdCurrency, gbpCurrency));

        List<CurrencyResponse> result = currencyService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCode());
        assertEquals("美金", result.get(0).getName());
        assertEquals(new BigDecimal("1.0"), result.get(0).getRate());
    }

    @Test
    void findByIdFound() {
        when(currencyRepository.findById("USD")).thenReturn(Optional.of(usdCurrency));

        Optional<CurrencyResponse> result = currencyService.findById("USD");

        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getCode());
    }

    @Test
    void findByIdNotFound() {
        when(currencyRepository.findById("XYZ")).thenReturn(Optional.empty());

        Optional<CurrencyResponse> result = currencyService.findById("XYZ");

        assertFalse(result.isPresent());
    }

    @Test
    void save() {
        when(currencyRepository.save(any(Currency.class))).thenReturn(usdCurrency);

        CurrencyResponse result = currencyService.save(usdRequest);

        assertNotNull(result);
        assertEquals("USD", result.getCode());
    }

    @Test
    void updateFound() {
        Currency updatedCurrency = new Currency("USD", "美金更新", new BigDecimal("1.05"));
        CurrencyRequest updateRequest = new CurrencyRequest("USD", "美金更新", new BigDecimal("1.05"));

        when(currencyRepository.findById("USD")).thenReturn(Optional.of(usdCurrency));
        when(currencyRepository.save(any(Currency.class))).thenReturn(updatedCurrency);

        CurrencyResponse result = currencyService.update("USD", updateRequest);

        assertNotNull(result);
        assertEquals("美金更新", result.getName());
        assertEquals(new BigDecimal("1.05"), result.getRate());
    }

    @Test
    void updateNotFound() {
        CurrencyRequest updateRequest = new CurrencyRequest("XYZ", "未知幣別", new BigDecimal("0.0"));

        when(currencyRepository.findById("XYZ")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            currencyService.update("XYZ", updateRequest);
        });

        assertEquals("Currency not found with code XYZ", exception.getMessage());
    }

    @Test
    void deleteById() {
        doNothing().when(currencyRepository).deleteById("USD");

        currencyService.deleteById("USD");

        verify(currencyRepository, times(1)).deleteById("USD");
    }
}
