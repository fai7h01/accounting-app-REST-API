package com.cydeo.service.impl;

import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;


//    @Override
//    public Map<String, BigDecimal> getSummaryNumbers() {
//        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
//        BigDecimal totalCost = invoiceService.countTotal(InvoiceType.PURCHASE);
//        BigDecimal totalSales = invoiceService.countTotal(InvoiceType.SALES);
//        BigDecimal profitLoss = invoiceService.sumProfitLoss();
//
//        summaryNumbersMap.put("totalCost", totalCost);
//        summaryNumbersMap.put("totalSales", totalSales);
//        summaryNumbersMap.put("profitLoss", profitLoss);
//        return summaryNumbersMap;
//    }


}
