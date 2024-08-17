package com.cydeo.service.impl;

import com.cydeo.client.CurrencyExchangeRateClient;
import com.cydeo.dto.common.CurrencyDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CurrencyExchangeRateClient client;
    private final InvoiceService invoiceService;

    public DashboardServiceImpl(CurrencyExchangeRateClient client, InvoiceService invoiceService) {
        this.client = client;
        this.invoiceService = invoiceService;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbers = new HashMap<>();
        BigDecimal totalCost = invoiceService.countTotal(InvoiceType.PURCHASE);
        BigDecimal totalSales = invoiceService.countTotal(InvoiceType.SALES);
        BigDecimal profitLoss = invoiceService.sumProfitLoss();
        summaryNumbers.put("total_cost", totalCost);
        summaryNumbers.put("total_sales", totalSales);
        summaryNumbers.put("profit_loss", profitLoss);
        return summaryNumbers;
    }

    @Override
    public CurrencyDto listUsdExchangeRate() {
        return client.getUsdExchangeRate().getUsd();
    }
}
