package com.cathaybk.tpi_leo_demo.repository;

import com.cathaybk.tpi_leo_demo.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
