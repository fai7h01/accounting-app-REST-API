package com.cydeo.service;


import com.cydeo.dto.Currency.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers();
}
