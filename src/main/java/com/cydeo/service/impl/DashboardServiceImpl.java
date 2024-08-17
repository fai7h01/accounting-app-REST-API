package com.cydeo.service.impl;

import com.cydeo.client.CurrencyClient;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CurrencyClient client;

    public DashboardServiceImpl(CurrencyClient client) {
        this.client = client;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        return Map.of();
    }

    @Override
    public CurrencyDto listUsdExchangeRate() {
        return client.getUsdExchangeRate().getUsd();
    }
}
