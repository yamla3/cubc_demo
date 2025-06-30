package com.cathaybk.tpi_leo_demo.initializer;

import com.cathaybk.tpi_leo_demo.entity.Currency;
import com.cathaybk.tpi_leo_demo.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (currencyRepository.count() == 0) { // 只有在資料庫為空時才插入資料
            currencyRepository.save(new Currency("USD", "美金", new BigDecimal("1.0")));
            currencyRepository.save(new Currency("GBP", "英鎊", new BigDecimal("0.8")));
            currencyRepository.save(new Currency("EUR", "歐元", new BigDecimal("0.9")));
        }
    }
}
